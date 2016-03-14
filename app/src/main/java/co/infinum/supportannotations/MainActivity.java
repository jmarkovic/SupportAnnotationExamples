package co.infinum.supportannotations;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import co.infinum.supportannotations.Utility.ExampleUIThreadClass;
import co.infinum.supportannotations.Utility.ExampleWorkerThreadClass;

import static co.infinum.supportannotations.Utility.acceptRGBColor;
import static co.infinum.supportannotations.Utility.accepts0to255;
import static co.infinum.supportannotations.Utility.acceptsColorRes;
import static co.infinum.supportannotations.Utility.acceptsFloat0To3;
import static co.infinum.supportannotations.Utility.acceptsFloatNegative1to1Exclusive;
import static co.infinum.supportannotations.Utility.acceptsIdRes;
import static co.infinum.supportannotations.Utility.acceptsStringResource;
import static co.infinum.supportannotations.Utility.doesNotAcceptNull;
import static co.infinum.supportannotations.Utility.mayAcceptNull;
import static co.infinum.supportannotations.Utility.mayReturnNull;
import static co.infinum.supportannotations.Utility.neverReturnsNull;
import static co.infinum.supportannotations.Utility.onBinderThread;
import static co.infinum.supportannotations.Utility.onMainThread;
import static co.infinum.supportannotations.Utility.onUIThread;
import static co.infinum.supportannotations.Utility.onWorkerThread;
import static co.infinum.supportannotations.Utility.requiresDangerousPermission;
import static co.infinum.supportannotations.Utility.requiresGrantedPermission;
import static co.infinum.supportannotations.Utility.requiresMultiplePermissions;
import static co.infinum.supportannotations.Utility.requiresNonGrantedPermission;
import static co.infinum.supportannotations.Utility.returnsAnimRes;
import static co.infinum.supportannotations.Utility.returnsIdRes;
import static co.infinum.supportannotations.Utility.returnsRGBColor;
import static co.infinum.supportannotations.Utility.sizeAtLeast1;
import static co.infinum.supportannotations.Utility.sizeAtMost5;
import static co.infinum.supportannotations.Utility.sizeCombo;
import static co.infinum.supportannotations.Utility.sizeExactly10;
import static co.infinum.supportannotations.Utility.sizeMultipleOf2;
import static co.infinum.supportannotations.Utility.thisDoesSomething;


/**
 * http://tools.android.com/tech-docs/support-annotations
 * http://developer.android.com/tools/debugging/annotations.html
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region NonNull and Nullable returns
        /* When receiving a return value from method that has @Nullable annotation,
         * Android Studio will be cautious and warn about possible null value. */
        String maybeNull = mayReturnNull();
        print(maybeNull.length()); // null warning, may crash app with NullPointerException
        /* To satisfy this lint check, @Nullable values have to be null checked before
         * accessing them. */
        String maybeNull2 = mayReturnNull();
        if (maybeNull2 != null) {
            print(maybeNull.length()); // no warning, nullability has been checked
        }


        /* However, if a method returns @NonNull value, caller can safely assume value
         * will never be null and avoid null checks. */
        String neverNull = neverReturnsNull();
        print(neverNull.length()); // no check required, we can safely assume null is not valid here

        /* Methods can also specify that certain parameters can be null with @Nullable.
         * Null is then perfectly valid.  */
        mayAcceptNull(null);

        /* Same goes with @NonNull. Methods can defend them selves and say null should
         * never be passed. Caller is then warned. */
        String notInitialized = null;
        doesNotAcceptNull(notInitialized); // null warning
        //endregion

        //region android resources
        /* If a method accepts an integer, and that integer should be an Android resource,
         * ResourceDef annotation should be used, specifically the resource which is needed. */
        acceptsStringResource(R.string.app_name); // requires string resource

        acceptsStringResource(125000); // it is an int, but not string resource

        acceptsStringResource(R.id.tv_hello_world); // string resource expected, not ID

        /* Anything that is a resource in Android can be specified. This exaple is for colors. */
        acceptsColorRes(R.color.colorPrimary);

        acceptsColorRes(Color.RED); // it is an int and a color, but not color resource as expected

        /* Return values can be decorated as well, not only parameters. This implicitly
         * decorates a variable, which gives us the ability to provide this variable to a method that expects
         * the resource of the same type. */
        int id = returnsIdRes(); // returns an id resource
        acceptsIdRes(id); // method wants only id resources, no error is given

        /* To prove this actually works, below will give an error. */
        acceptsIdRes(returnsAnimRes()); // check goes through methods, not just variables
        //endregion

        //region threading
        /* Threading can be complicated in Android. More so if some methods can only work
         * on UI thread or on a worker thread. Methods that are explicit on where
         * they work must be decorated with appropriate annotation */
        onMainThread(); // no issue, main and UI thread are the same in this example

        onUIThread(); // both main and ui thread annotation work for the same thread as those are interchangeable

        onWorkerThread(); // should not run on main thread

        onBinderThread(); // should not run on main thread

        /* Worker thread method does not work in above example because it is called from the main/ui thread.
         * Example below creates an async task with background thread implementation. */
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                onMainThread(); // may crash

                onUIThread(); // will crash, no UI work available on any thread other than UI

                onWorkerThread(); // finally works because the thread is now a worker thread

                onBinderThread(); // still doesn't accept binder

                return null;
            }
        };

        /*todo NOTE binder class example at the bottom */

        //endregion

        //region threading on class
        /* Threading decorators can be applied on a whole class. Every method in that class
         * is expected to be run on a thread that corresponds to the class annotation. */
        final ExampleUIThreadClass uiThreadObject = new ExampleUIThreadClass(); // annotated with UiThread
        uiThreadObject.doesNotHaveUIThreadAnnotation(); // works on ui thread

        final ExampleWorkerThreadClass workerThreadObject = new ExampleWorkerThreadClass(); // annotated with WorkerThread
        workerThreadObject.doesNotHaveWorkerThreadAnnotation(); // does not work on UiThread, even though method is not annotated

        /* To prove this in the same way as for methods, calling the same methods from async task yields
         * different results than when called from the main thread. */
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                workerThreadObject.doesNotHaveWorkerThreadAnnotation(); // method from worker class works in background thread now

                uiThreadObject.doesNotHaveUIThreadAnnotation(); // method from ui class will not work and will throw an exception

                return null;
            }

            /* Even though this is an async task, onProgressUpdate works on main thread. */
            @Override
            protected void onProgressUpdate(Void... values) {
                workerThreadObject.doesNotHaveWorkerThreadAnnotation();

                uiThreadObject.doesNotHaveUIThreadAnnotation();
            }
        };
        //endregion

        //region RGB colors
        /* RGB colors are resolved colors in int form. When method requires a parameter to be an RGB color,
         * caller can no longer pass a reference to Android resource before resolving it. */
        acceptRGBColor(Color.RED); // accepts color values instead color resources

        acceptRGBColor(R.color.colorPrimary); // this is a resource, must be actual color

        acceptRGBColor(12); // this is a valid color, converts to 0x0000000C

        acceptRGBColor(0xFF00FF00); // resolves to green

        acceptRGBColor(ContextCompat.getColor(this, R.color.colorPrimary)); // resolves into a color

        /* Same as before, method can also return RGB color. Tools are smart enough to implicitly
         * check the value when provided to another method later on. */
        int color = returnsRGBColor(); // returns an RGB color

        acceptRGBColor(color); // works because it is a resolved color
        //endregion

        //region value constraints - ranges
        /* Sometimes values do not represent resources or other things, but are just plain values.
         * And sometimes these values must be constrained to certain ranges. Providing a value
         * outside of a given range gives an error. */
        accepts0to255(120); // value is between 0 and 255

        accepts0to255(10_000); // value out of range

        acceptsFloat0To3(2.5f); // value is between 0 and 3

        acceptsFloat0To3(3.0f); // value is exactly 3, which is still accepted

        acceptsFloat0To3(5.25f); // value out of range

        /* Float ranges are a bit different than int ranges because method can specify if the bordering
         * values are inclusive or exclusive to declared range. Example below excludes bordering values. */
        acceptsFloatNegative1to1Exclusive(0);

        acceptsFloatNegative1to1Exclusive(1); // even though range goes from -1 to 1, both values are marked as not inclusive
        //endregion

        //region value constraints - array and list size (any length measurement, including String)
        /* In similar fashion, lengths can be constrained for objects that support lengths, like Collections, arrays and Strings. */
        sizeAtLeast1("Hello");

        sizeAtLeast1(""); // size constraint not satisfied, asked for length at least 1, but 0 was given

        sizeAtMost5("Hello");

        sizeAtMost5("Hello World"); // size constraint not satisfied, asked for length at most 5, but 11 was given

        sizeExactly10("HelloWorld"); // no space equals to length of 10, exactly how much is requested

        sizeExactly10("Hello World"); // with space equals to length of 11, which is no more exact length as requested

        sizeMultipleOf2("HelloWorld"); // length is 10, and must be multiple of 2

        sizeMultipleOf2("Hello World"); // length is 11, and must be multiple of 2

        /* Size attributes can be combined. Similar to ranges, max and min can be set simultaneously. Additionally,
         * multiple can be set as well. */
        sizeCombo("ElCapitan"); // 9 long - satisfies minimum of 6, maximum of 12 and multiple of 3

        sizeCombo("Hello World"); // 11 long, not multiple of 3

        sizeCombo("Ice"); // not at least 6 long

        sizeCombo("AndroidStudio20"); // 15 long, which is over 12
        //endregion

        //region permissions
        /* Some methods may do work that require additional permissions. This may not be obvious, and
         * sometimes may not even be documented. Tools can show an error for these methods
         * if annotated with @RequestPermission annotation. */
        requiresGrantedPermission(); // Requires a permission, but it is already set in manifest. No error.

        requiresNonGrantedPermission(); // Requires a permission which is not set in manifest. Shows an error.

        requiresMultiplePermissions(); // Requires multiple permissions which are not set in manifest. Shows an error.

        // generated check
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        requiresDangerousPermission(); // Requires a permission which is set to manifest, but is considered dangerous.
                                       // Must be checked, or caller risks SecurityException.
        //endregion

        //region Call super
        /* When writing implementations of some other classes, overriding existing method implementations is a common thing to do.
         * However, very often it is not obvious if super method should be called. By decorating each method that requires
         * a call to super with @CallSuper, tools can show an error for missing super call. */
        new Utility.CallSuperExample() {
            @Override
            public void mustCallSuper() { // implementation of this method must call super
                super.mustCallSuper(); // which it does, no error shown
            }
            @Override
            public void doesNotNeedToCallSuper() { // implementation does not care for super call
                // no op
            }
        };

        new Utility.CallSuperExample() {
            @Override
            public void mustCallSuper() { // error shown for missing super call
                // no op
            }
        };
        //endregion

        //region check result
        /* Occasionally, although rare, APIs may have inconsistent or downright misleading names for accessor methods.
         * For example, method name may suggest it does some work or triggers work somewhere else, but instead it only
         * returns a value. There may be another method that does work that is implied by the calling method.
         * To notify the caller about this problem, @CheckResult notification can specify which exact method
         * can be related to the calling method. */
        thisDoesSomething(); // This method does nothing special, it only returns a value.
                             // However, name suggests otherwise. This call is then marked as an error.

        int result = thisDoesSomething(); // Same call is no longer an error because the return value is not ignored.
        thisDoesSomething(result);
        //endregion

    }

    /**
     * Prints the String value of provided {@code object}.
     */
    private void print(Object object) {
        System.out.println(String.valueOf(object));
    }

    public static class ExampleBinder extends Binder {
        public void binderWork() {
            onBinderThread(); // finally accepts binder
        }
    }
}
