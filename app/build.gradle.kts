import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.newsmreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.newsmreader"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("armeabi")
            // Add more ABI filters if needed
        }
    }

    buildTypes {
        release {
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



}



dependencies {

    //noinspection GradleCompatible


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs/A26-C.aar"))
    implementation(files("libs/CXCommSdk.jar"))
    implementation(files("libs/platform_sdk_v4.0.0115.jar"))
    implementation(files("libs/poi-3.7.jar"))
    implementation("androidx.activity:activity:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //External dependencies

    implementation ("nl.joery.animatedbottombar:library:1.1.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.skydoves:elasticviews:2.1.0")
    implementation ("com.github.afsalkodasseri:KalendarView:2.3")
    // Retrofit to connection to database
    implementation  ("com.squareup.retrofit2:retrofit:2.5.0")
    implementation  ("com.squareup.retrofit2:converter-gson:2.5.0")
    // okhttp   to send files like photo and media
    implementation  ("com.squareup.okhttp3:okhttp:3.11.0")
    implementation  ("com.squareup.okhttp3:logging-interceptor:3.11.0")
    implementation ("org.apache.commons:commons-text:1.9")
    /////Gif//////////////////////
    implementation  ("pl.droidsonroids.gif:android-gif-drawable:1.2.10")

    implementation ("com.github.hkk595:Resizer:v1.5")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")

    implementation ("com.pranavpandey.android:dynamic-toasts:4.2.1")

    implementation ("com.google.mlkit:barcode-scanning:17.2.0")


    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("com.google.android.gms:play-services-vision:17.0.2")
    implementation ("com.google.android.gms:play-services-tasks:18.0.2")
    // dexter runtime permissions for the location upadte
    implementation ("com.karumi:dexter:6.2.3")


}