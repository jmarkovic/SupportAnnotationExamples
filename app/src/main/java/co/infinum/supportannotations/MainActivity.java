package co.infinum.supportannotations;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

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
import static co.infinum.supportannotations.Utility.returnCheckResult;
import static co.infinum.supportannotations.Utility.returnsAnimRes;
import static co.infinum.supportannotations.Utility.returnsIdRes;
import static co.infinum.supportannotations.Utility.returnsRGBColor;
import static co.infinum.supportannotations.Utility.sizeAtLeast1;
import static co.infinum.supportannotations.Utility.sizeAtMost5;
import static co.infinum.supportannotations.Utility.sizeCombo;
import static co.infinum.supportannotations.Utility.sizeExactly10;
import static co.infinum.supportannotations.Utility.sizeMutlipleOf2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region NonNull and Nullable returns
        // NonNull and Nullable returns
        String maybeNull = mayReturnNull();
        print(maybeNull.length()); // null warning, may crash app with NullPointerException

        String neverNull = neverReturnsNull();
        print(neverNull.length());

        // NonNull and Nullable parameters
        mayAcceptNull(null);

        doesNotAcceptNull(null); // null warning
        //endregion

        //region android resources
        // android resources
        acceptsStringResource(R.string.app_name);

        acceptsStringResource(R.id.tv_hello_world); // string resource expected, not ID

        acceptsColorRes(R.color.colorPrimary);

        acceptsColorRes(Color.RED); // it is color, but not color resource as expected

        int id = returnsIdRes();
        acceptsIdRes(id); // no warning

        acceptsIdRes(returnsAnimRes()); // check goes through methods, not just variables
        //endregion

        //region threading
        // threading
        onMainThread();

        onUIThread(); // both main and ui thread annotation work for the same thread as those are interchangeable

        onWorkerThread(); // should not run on main thread

        onBinderThread(); // should not run on main thread

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                onMainThread(); // may crash

                onUIThread(); // will crash, no UI work available on any thread other than UI

                onWorkerThread();

                onBinderThread(); // still doesn't accept binder

                return null;
            }
        };

        /*todo NOTE binder class example at the bottom */

        //endregion

        //region threading on class
        // threading on class
        final Utility.ExampleUIThreadClass uiThreadObject = new Utility.ExampleUIThreadClass();
        uiThreadObject.doesNotHaveUIThreadAnnotation();

        final Utility.ExampleWorkerThreadClass workerThreadObject = new Utility.ExampleWorkerThreadClass();
        workerThreadObject.doesNotHaveWorkerTheadAnnotation(); // still knows

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                workerThreadObject.doesNotHaveWorkerTheadAnnotation(); // now it works

                uiThreadObject.doesNotHaveUIThreadAnnotation(); // it knows

                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                workerThreadObject.doesNotHaveWorkerTheadAnnotation();

                uiThreadObject.doesNotHaveUIThreadAnnotation();
            }
        };
        //endregion

        //region RGB colors
        // RGB colors
        acceptRGBColor(Color.RED); // accepts color values instead color resources

        acceptRGBColor(R.color.colorPrimary); // this is a resource, must be actual color

        acceptRGBColor(ContextCompat.getColor(this, R.color.colorPrimary)); // resolves into a color

        int color = returnsRGBColor();

        acceptRGBColor(color); // works because it is a resolved color
        //endregion

        //region value constraints - ranges
        // value constraints - ranges
        accepts0to255(120);

        accepts0to255(10000); // value out of range

        acceptsFloat0To3(2);

        acceptsFloat0To3(3);

        acceptsFloat0To3(5); // value out of range

        acceptsFloatNegative1to1Exclusive(0);

        acceptsFloatNegative1to1Exclusive(1); // value is exclusive
        //endregion

        //region value constraints - array and list size (any length measurement, including String)
        // value constraints - array and list size (any length measurement, including String)
        sizeAtLeast1("Hello");

        sizeAtLeast1(""); // size constraint not satisfied

        sizeAtMost5("Hello");

        sizeAtMost5("Hello World"); // size constraint not satisfied

        sizeExactly10("HelloWorld"); // no space

        sizeExactly10("Hello World"); // with space

        sizeMutlipleOf2("HelloWorld"); // length is 10

        sizeMutlipleOf2("Hello World"); // length is 11

        sizeCombo("ElCapitan"); // 9 long

        sizeCombo("Hello World"); // not multiple of 3

        sizeCombo("Ice"); // not at least 6 long

        sizeCombo("AndroidStudio20"); // 15 long, which is over 12
        //endregion

        //region permissions
        // permissions
        requiresGrantedPermission(); // set in manifest

        requiresNonGrantedPermission(); // not set in manifest

        requiresMultiplePermissions(); // multiple permissions not set in manifest

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
        requiresDangerousPermission();
        //endregion

        //region Call super
        // Call Super
        new Utility.CallSuperExample() {
            @Override
            public void mustCallSuper() {
                super.mustCallSuper();
            }
            @Override
            public void doesNotNeedToCallSuper() {
                // no op
            }
        };

        new Utility.CallSuperExample() {
            @Override
            public void mustCallSuper() {
                // no op
            }
        };
        //endregion

        //region check result
        // Check result
        returnCheckResult();

        int result = returnCheckResult();
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
