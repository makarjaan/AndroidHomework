plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.gradle.secrets.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.itis.androidhomework"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.itis.androidhomework"
        minSdk = 25
        targetSdk = 35
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
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "OPEN_TRIP_MAP_BASE_URL",
            "\"https://api.opentripmap.com/0.1/ru/\""
        )
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.glide)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.http.loggin.interceptor)
    implementation(libs.androidx.fragment)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

}