plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // Add this line for Firebase
}

android {
    namespace = "com.example.activity1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.activity1"
        minSdk = 28 // Updated to API level 28 (Android 9.0)
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")  // Firebase Auth
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")  // Firebase Realtime Database

    // Notification dependencies
    implementation("androidx.core:core-ktx:1.8.0")  // For NotificationCompat

    // WorkManager for background tasks (optional for periodic tasks)
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // Gson dependency for JSON serialization/deserialization
    implementation("com.google.code.gson:gson:2.8.9")

    // Other test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}



