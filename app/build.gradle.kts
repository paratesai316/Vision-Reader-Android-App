plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.saip.visionreader"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.saip.visionreader"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":sdk"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // PASTE THE CORRECTED DEPENDENCIES HERE 👇

    // CameraX dependencies for camera feed and analysis
    val camerax_version = "1.3.1"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")

    // ML Kit Text Recognition
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    // Add this dependency for ML Kit hardware acceleration
    implementation("com.google.android.gms:play-services-tflite-gpu:16.2.0")
    // Add this with your other dependencies
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    // The OpenCV dependency is now provided by the module you imported, not this file.
}