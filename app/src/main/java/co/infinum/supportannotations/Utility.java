package co.infinum.supportannotations;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.BinderThread;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.support.v4.content.ContextCompat;

import java.util.Random;

/**
 * Holds all examples of how to use {@code support-annotations}.
 * Examples may not be complete, but each explains main idea behind each annotations.
 */
public final class Utility {

    private static Random random = new Random();

    private Utility() {
        throw new AssertionError("cannot instantiate");
    }

    //region Nullable and NonNull

    /**
     * Does some work and may return {@code null}.
     * This is described with {@code @Nullable} annotation
     * which decorates this method. When called, tooling will
     * give a warning to usages that do not check for {@code null}.
     */
    @Nullable
    public static String mayReturnNull() {
        return random.nextBoolean() ? "not null" : null;
    }

    /**
     * Does some work and always returns a valid, non-null instance.
     * This is described with {@code NonNull} annotation
     * which decorates this method. Caller can safely assume this method
     * will never return null and that it is safe to use return object
     * without null checks.
     */
    @NonNull
    public static String neverReturnsNull() {
        return "this is never null";
    }

    /**
     * Takes a {@code @Nullable} parameter. When parameter is decorated with
     * {@code @Nullable} annotation, it describes to the caller that he
     * can safely ignore nullability of the provided value and that {@code null}
     * can be completely valid parameter.
     */
    public static void mayAcceptNull(@Nullable String nullableParam) {
        // no op
    }

    /**
     * Takes a {@code @NonNull} parameter. When parameter is decorated with
     * {@code @NonNull} annotation, it describes explicitly that this parameter
     * must not be {@code null}. If caller cannot be safe about null safety
     * for provided object, it has to check it before providing.
     */
    public static void doesNotAcceptNull(@NonNull String nonNullParam) {
        // no op
    }
    //endregion

    //region resources

    /**
     * Accepts a primitive {@code int} parameter, decorated with
     * {@code @StringRes} annotation. This annotation describes that the
     * given primitive {@code int} can only be a {@code String} resource.
     * Tooling will show an error if any other resource or random {@code int}
     * is used instead.
     */
    public static void acceptsStringResource(@StringRes int stringRes) {
        // no op
    }

    /**
     * Accepts a primitive {@code int} parameter, decorated with
     * {@code @ColorRes} annotation. This annotation describes that the
     * given primitive {@code int} can only be a {@code Color} resource.
     * This decorator is special because resolved color values are also
     * {@code int} values. However, this method does not accept resolved
     * values, but only resource references, and specifically @
     */
    public static void acceptsColorRes(@ColorRes int colorRes) {
        // no op
    }

    /**
     * Returns a primitive {@code int} value decorated with
     * {@code @IdRes} annotation. This describes to the caller
     * that it can only expect an {@code ID} resource as return
     * value, which makes it safe to use for methods that expect
     * the same type of parameter.
     */
    @IdRes
    public static int returnsIdRes() {
        return R.id.tv_hello_world;
    }

    /**
     * Accepts a primitive {@code int} value decorated with
     * {@code @IdRes} annotation. This method serves as an
     * example that return value from {@link #returnsIdRes()}
     * can be safely provided to this method since type safety
     * is hinted with decorated annotations.
     */
    public static void acceptsIdRes(@IdRes int id) {
        // no op
    }

    /**
     * Returns a primitive {@code int} value decorated with
     * {@code @AnimRes} annotation. This method serves as an
     * example that every resource type has its own {@code Def}
     * annotation for describing type safety of a parameter or
     * return value.
     * <br /><br />
     * Check for all possibilities here:
     * <a href=http://developer.android.com/reference/android/support/annotation/package-summary.html>
     *     http://developer.android.com/reference/android/support/annotation/package-summary.html
     *     </a>
     */
    @AnimRes
    public static int returnsAnimRes() {
        return R.anim.no_op;
    }
    //endregion

    //region threading

    /**
     * Does work which should only be done on the main thread.
     * This method is decorated with {@code @MainThread} annotation.
     * It should only be called on main and UI thread. If tools find
     * that the caller is on any other thread, it will be marked as error.
     */
    @MainThread
    public static void onMainThread() {
        // no op
    }

    /**
     * Does work which should only be done on the UI thread.
     * This method is decorated with {@code @UiThread} annotation.
     * Similarly to {@link #onMainThread()}, this method can be called
     * only on main and UI thread. These two threads are 99% of the time
     * the same thread. However, this may change with the Multi Window feature
     * coming with Android {@link Build.VERSION_CODES.N} where more than one
     * window can exist for the same process.
     */
    @UiThread
    public static void onUIThread() {
        // no op
    }

    /**
     * Does work which should not be called from the main or UI thread
     * because it most likely blocks the thread for longer than 16ms.
     * This means the caller should call this method from any background thread.
     * Method is decorated with {@code @WorkerThread} annotation.
     */
    @WorkerThread
    public static void onWorkerThread() {
        // no op
    }

    /**
     * Does work which should be called from the binder thread.
     * This method is decorated with {@code @BinderThread} annotation.
     * <br /><br />
     * More info about binders is available here:
     * <a href=http://developer.android.com/reference/android/support/annotation/package-summary.html>
     *     http://developer.android.com/reference/android/support/annotation/package-summary.html
     *     </a>
     */
    @BinderThread
    public static void onBinderThread() {
        // no op
    }
    //endregion

    //region RGB colors

    /**
     * Accepts a resolved color {@code int} value. Resolved color
     * value is a value that directly represents a color in
     * traditional {@code hex} format. Parameter is decorated with
     * {@code @ColorInt} annotation to signify this. Caller should now
     * be aware that only resolved color values are accepted, meaning
     * color references from resource are not allowed.
     * Any <i>normal</i> integer is allowed, as almost any value
     * can represent a color. However, it is best to use resolved color
     * either by calling {@link ContextCompat#getColor(Context, int)} for
     * resources or anything from {@link Color} class.
     */
    public static void acceptRGBColor(@ColorInt int colorInt) {

    }

    /**
     * Returns a resolved color {@code int} value. This should signify the
     * caller that returned value is not a resource, but fully resolved
     * color value.
     */
    @ColorInt
    public static int returnsRGBColor() {
        return Color.RED;
    }
    //endregion

    //region value constraints

    /**
     * Accepts an {@code int} value that cannot be less than 0
     * and more than 255. This is described with {@code @IntRange}
     * annotation, which takes {@code from} and {@code to} parameter.
     * If caller passes a value that is out of defined range, tooling
     * gives an error.
     */
    public static void accepts0to255(@IntRange(from = 0, to = 255) int value) {
        // no op
    }

    /**
     * Accepts a {@code float} value that cannot be less than 0
     * and more than 3. This is described with {@code @FloatRange}
     * annotation, which takes {@code from} and {@code to} parameter.
     * This decorator basically works like {@link IntRange}, but for
     * {@code floats}. If caller passes a value that is out of defined
     * range, tooling gives an error.
     */
    public static void acceptsFloat0To3(@FloatRange(from = 0, to = 3) float value) {
        // no op
    }

    /**
     * Accepts a {@code float} value that must be between
     * -1 and 1, but should not include these two values. This
     * is described with {@code @FloatRange} annotation, which
     * takes {@code from} and {@code to}, bit also has
     * {@code fromInclusive} and {@code toInclusive} set to false.
     */
    public static void acceptsFloatNegative1to1Exclusive(
            @FloatRange(from = -1f, fromInclusive = false, to = 1f, toInclusive = false) int value) {
        // no op
    }

    /**
     * Accepts a String {@code text} which length has to
     * be at least 1. This is described with {@code @Size} annotation
     * which parameter {@code min} is set to 1. If String with length
     * less than 1 is provided, tooling gives an error.
     */
    public static void sizeAtLeast1(@Size(min = 1) String text) {
        // no op
    }

    /**
     * Accepts a String {@code text} which length must not be
     * longer than 5. This is described with {@code @Size} annotation
     * which parameter {@code max} is set to 5. If String with length
     * more than 5 is provided, tooling gives an error.
     */
    public static void sizeAtMost5(@Size(max = 5) String text) {
        // no op
    }

    /**
     * Accepts a String {@code text} which length must be
     * exactly 10. This is described with {@code @Size} annotation
     * which parameter {@code value} is set to 10. If String with
     * length different than 10 is provided, tooling gives an error.
     */
    public static void sizeExactly10(@Size(10) String text) {
        // no op
    }

    /**
     * Accepts a String {@code text} which length must be
     * a multiple of 2. This is described with {@code @Size} annotation
     * which parameter {@code multiple} is set to 2. If String
     * with length that cannot be divided by 2 is provided,
     * tooling gives an error.
     */
    public static void sizeMultipleOf2(@Size(multiple = 2) String text) {
        // no op
    }

    /**
     * Accepts a String {@code text} which length must be
     * at least 6, at most 12 and must be multiple of 3.
     * This is described with {@code @Size} annotation which
     * parameters {@code min} is set to 6, {@code max} is set to 12
     * and {@code multiple} is set to 3.
     */
    public static void sizeCombo(@Size(min = 6, max = 12, multiple = 3) String text) {
        // no op
    }
    //endregion

    //region permissions

    /**
     * Requires {@code Manifest.permission.CAMERA} for this
     * method to run successfully. Tooling will give an error for
     * this call if requested permission is not listed in the
     * manifest file. Requested permission is described with
     * {@code RequiresPermission} annotation.
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public static void requiresNonGrantedPermission() {
        // no op
    }

    /**
     * Requires {@code Manifest.permission.ACCESS_WIFI_STATE} for
     * this method to run successfully. If this method is listed in
     * the manifest file, tooling will be satisfied and no error will
     * be displayed.
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static void requiresGrantedPermission() {
        // no op
    }

    /**
     * Requires {@code Manifest.permission.CAMERA} and
     * {@code Manifest.permission.WRITE_EXTERNAL_STORAGE} for this
     * method to run successfully. If any of listed permissions is not
     * listed in the manifest file, tooling will mark the call
     * for this method as an error.
     */
    @RequiresPermission(allOf = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    public static void requiresMultiplePermissions() {
        // no op
    }

    /**
     * Requires {@code Manifest.permission.ACCESS_FINE_LOCATION}
     * for this method to run successfully. If permission is listed
     * in the manifest file, tooling will still give an error
     * if permission has not been checked before call was made.
     * A check is required for tooling to be satisfied.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public static void requiresDangerousPermission() {
        // no op
    }

    // todo <b>anyOf</b> and <b>Read/Write</b> are used so rarely they don't even deserve the example
    // todo show from sources http://tools.android.com/tech-docs/support-annotations
    //endregion

    //region check result

    /**
     * Poorly named method that suggests, with its name,
     * that it does something useful. However, it only returns
     * a value that represents suggested work. To avoid refactoring
     * through hundreds of files, {@code @CheckResult} annotation
     * describes to the caller that there's another method that may
     * do more useful stuff, <b>if</b> the caller <b>ignores</b>
     * return value. However, if the caller wants a return value,
     * this is the method it should call and tooling will not mark
     * it as an error.
     */
    @CheckResult(suggest = "#thisDoesSomething")
    public static int thisDoesSomething() {
        return random.nextInt();
    }

    /**
     * Referenced method from {@link #thisDoesSomething()}. It is
     * named the same, but takes a parameter and returns no value.
     * In reality, this method actually does some work the name suggests.
     */
    public static void thisDoesSomething(int withThisValue) {
        // no op
    }

    //endregion

    //region visible for testing

    /**
     * Method that should usually be made private, package or protected, but is
     * elevated to make it more accessible for testing. {@code VisibleForTesting}
     * only decorates the method and currently does nothing more than to suggest
     * testing availability.
     */
    @VisibleForTesting // also applicable to classes and fields
    public static int returnValueOnlyForTests() {
        return random.nextInt();
    }
    //endregion

    //region keep

    /**
     * Method which will not be minimized when minimizing process
     * from build tools kicks in. This annotation is only a decorator
     * and does nothing more than to suggest the minimizing strategy.
     */
    @Keep // also applicable to classes
    public static void keepThisMethodWhenMinimizing() {
        // no op
    }
    //endregion


    //region helper classes
    @UiThread
    public static class ExampleUIThreadClass {
        public void doesNotHaveUIThreadAnnotation() {
            // no op
        }
    }

    @WorkerThread
    public static class ExampleWorkerThreadClass {
        public void doesNotHaveWorkerThreadAnnotation() {
            // no op
        }
    }

    public static class CallSuperExample {

        /**
         * When implementations override this method, they <b>must</b>
         * call {@code super} method for the functionality to work.
         * This is described with {@code @CallSuper} annotation.
         * If {@code super} is not called from method described with this
         * annotation, tooling will mark this as an error.
         */
        @CallSuper
        public void mustCallSuper() {
            // no op
        }
        public void doesNotNeedToCallSuper() {
            // no op
        }
    }
    //endregion
}
