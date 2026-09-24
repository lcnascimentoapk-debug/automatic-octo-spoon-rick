plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.rick.assistant"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rick.assistant"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-beta"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Jetpack Compose & Material 3
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Android Biometrics for High Impact Operations
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Room Database for Controlled User Memory
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Android Auto / Car App Library
    implementation("androidx.car.app:app:1.4.0")

    // Google Play Services Nearby Connections for Device Mesh
    implementation("com.google.android.gms:play-services-nearby:19.3.0")

    // Ktor Client for Gemini API
    implementation("io.ktor:ktor-client-android:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
}