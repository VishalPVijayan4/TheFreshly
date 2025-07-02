// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.0")
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("androidx.navigation.safeargs.kotlin") version "2.9.0" apply false
//    id("androidx.navigation.safeargs.kotlin")
//    id("androidx.navigation.safeargs") version "2.5.3" apply false
}