package co.infinum.supportannotations;

import android.Manifest;
import android.graphics.Color;
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

import java.util.Random;

/**
 * Created by jmarkovic on 13/03/16.
 */
public final class Utility {

    private static Random random = new Random();

    private Utility() {
        throw new AssertionError("cannot instantiate");
    }

    //region Nullable and NonNull
    @Nullable
    public static String mayReturnNull() {
        return random.nextBoolean() ? "not null" : null;
    }

    @NonNull
    public static String neverReturnsNull() {
        return "this is never null";
    }

    public static void mayAcceptNull(@Nullable String nullableParam) {
        // no op
    }

    public static void doesNotAcceptNull(@NonNull String nonNullParam) {
        // no op
    }
    //endregion

    //region resources
    public static void acceptsStringResource(@StringRes int stringRes) {
        // no op
    }

    public static void acceptsColorRes(@ColorRes int colorRes) {
        // no op
    }

    @IdRes
    public static int returnsIdRes() {
        return R.id.tv_hello_world;
    }

    public static void acceptsIdRes(@IdRes int id) {
        // no op
    }

    @AnimRes
    public static int returnsAnimRes() {
        return R.anim.no_op;
    }
    //endregion

    //region threading
    @MainThread
    public static void onMainThread() {
        // no op
    }

    @UiThread
    public static void onUIThread() {
        // no op
    }

    @WorkerThread
    public static void onWorkerThread() {
        // no op
    }

    @BinderThread
    public static void onBinderThread() {
        // no op
    }
    //endregion

    //region RGB colors
    public static void acceptRGBColor(@ColorInt int colorInt) {

    }

    @ColorInt
    public static int returnsRGBColor() {
        return Color.RED;
    }
    //endregion

    //region value constraints
    public static void accepts0to255(@IntRange(from = 0, to = 255) int value) {
        // no op
    }

    public static void acceptsFloat0To3(@FloatRange(from = 0, to = 3) float value) {
        // no op
    }

    public static void acceptsFloatNegative1to1Exclusive(
            @FloatRange(from = -1f, fromInclusive = false, to = 1f, toInclusive = false) int value) {
        // no op
    }

    public static void sizeAtLeast1(@Size(min = 1) String text) {
        // no op
    }

    public static void sizeAtMost5(@Size(max = 5) String text) {
        // no op
    }

    public static void sizeExactly10(@Size(10) String text) {
        // no op
    }

    public static void sizeMultipleOf2(@Size(multiple = 2) String text) {
        // no op
    }

    public static void sizeCombo(@Size(min = 6, max = 12, multiple = 3) String text) {
        // no op
    }
    //endregion

    //region permissions
    @RequiresPermission(Manifest.permission.CAMERA)
    public static void requiresNonGrantedPermission() {
        // no op
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static void requiresGrantedPermission() {
        // no op
    }

    @RequiresPermission(allOf = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    public static void requiresMultiplePermissions() {
        // no op
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public static void requiresDangerousPermission() {
        // no op
    }

    // any of and Read/Write are used so rarely they don't even deserve the example
    // show from sources
    //endregion

    //region check result
    public static void doingSomethingWithResult(int value) {
        // no op
    }

    @CheckResult(suggest = "#doingSomethingWithResult")
    public static int returnCheckResult() {
        return random.nextInt();
    }
    //endregion

    //region visible for testing
    @VisibleForTesting // also applicable to classes and fields
    public static int returnValueOnlyForTests() {
        return random.nextInt();
    }
    //endregion

    //region keep
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
