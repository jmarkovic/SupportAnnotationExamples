# SupportAnnotationExamples
Examples for many available annotations in Support Annotation package, complete with comments and javadoc explaining how to use them and when.

## Sources

Examples have been compiled from

* [tools site](http://tools.android.com/tech-docs/support-annotations)
* [android docs](http://developer.android.com/tools/debugging/annotations.html)

## Structure

Examples of how to use support annotations are all created inside
[Utility.java](https://github.com/jmarkovic/SupportAnnotationExamples/blob/master/app%2Fsrc%2Fmain%2Fjava%2Fco%2Finfinum%2Fsupportannotations%2FUtility.java)
class. Each topic is written inside a region block which should make it more organized.
Each method has javadoc written to explain what should be expected with real implementation.

Examples of how support annotations work is done in
[MainActivity.java](https://github.com/jmarkovic/SupportAnnotationExamples/blob/master/app%2Fsrc%2Fmain%2Fjava%2Fco%2Finfinum%2Fsupportannotations%2FMainActivity.java)
. Similar to how code is organized in `Utility.java`, different topics are written inside region blocks.
Normal comments are used to explain each step.

### Almost all annotations are used

List of annotations used in these examples:

* Nullable
* NonNull
* StringRes
* ColorRes
* IdRes
* MainThread
* UiThread
* WorkerThread
* BinderThread
* ColorInt
* IntRange
* FloatRange
* Size
* RequiresPermission
* CallSuper
* CheckResult
* VisibleForTesting
* Keep

Annotations that have no example:

* Other (Resource)Def annotations - since use case of any resource annotation is the same, not all have been used for an
example.
* IntDef*
* StringDef*

> * `IntDef` and `StringDef` are a huge topic for them selves. They help enforce _type safety_ on method parameters
and return values that require an `int` or `String` type, similar to resource annotations. Unlike resource annotations,
user of support annotations defines a definite list of **constant** values that can be used. These are more commonly
known as [MagicConstants](https://infinum.co/the-capsized-eight/articles/magic-constants-in-android-development).
