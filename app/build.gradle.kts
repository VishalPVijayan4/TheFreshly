plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.vishalpvijayan.thefreshly"
    compileSdk = 34

    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.vishalpvijayan.thefreshly"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    buildTypes {
        debug {
            // This makes the DEBUG field true for debug build
        }

        release {
            // This makes the DEBUG field false for release builds
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // Paging 3
    implementation("androidx.paging:paging-runtime:3.3.0")


    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.compose.ui:ui-graphics-android:1.6.4")
    implementation("androidx.compose.foundation:foundation-android:1.6.4")
    val coroutines = ("1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    implementation("androidx.work:work-runtime-ktx:2.7.1")

    val coil = ("2.5.0")
    implementation("io.coil-kt:coil:$coil")

    val viewmodel = ("2.7.0")
    val activity = ("1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$viewmodel")
    implementation("androidx.activity:activity-ktx:$activity")

    // Material Design Components
    implementation("com.google.android.material:material:1.11.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")


    val navigation = ("2.7.6")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation")

    // Room
    val room = ("2.6.1")
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    implementation("androidx.hilt:hilt-common:1.1.0")
    ksp("androidx.room:room-compiler:$room")

    //glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    ksp("com.github.bumptech.glide:compiler:4.14.2")

    // Dagger-Hilt
    val hilt = ("2.48.1")
    implementation("androidx.hilt:hilt-work:1.1.0")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    implementation("com.google.dagger:hilt-android:$hilt")
    ksp("com.google.dagger:hilt-compiler:$hilt")
    ksp("com.google.dagger:hilt-android-compiler:$hilt")

    //cardview
    val cardview = ("1.0.0")
    implementation("androidx.cardview:cardview:$cardview")

    //splash
    val splash = ("1.0.1")
    implementation("androidx.core:core-splashscreen:$splash")


    val corektx = ("1.12.0")
    val appcompat = ("1.6.1")
    val material = ("1.11.0")
    val constraintlayout = ("2.1.4")

    implementation("androidx.core:core-ktx:$corektx")
    implementation("androidx.appcompat:appcompat:$appcompat")
    implementation("com.google.android.material:material:$material")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayout")

    implementation ("androidx.datastore:datastore:1.1.7")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation ("androidx.datastore:datastore-core:1.1.7")

//    implementation("com.github.Romancha:android-material-play-pause-view-button:2.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}