# Keep all BuildConfig classes (matches com.vishalpvijayan.thefreshly.BuildConfig)
-keep class **.BuildConfig { *; }

# Keep all Razorpay classes
-keep class com.razorpay.** { *; }
-dontwarn com.razorpay.**

# Keep payment callback methods
-keepclasseswithmembers class * {
    public void onPayment*(...);
}

# Keep annotations and interfaces
-keepattributes JavascriptInterface
-keepattributes *Annotation*

# General optimizations
-optimizations !method/inlining/*
