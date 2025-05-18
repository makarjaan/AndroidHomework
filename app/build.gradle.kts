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
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.itis.androidhomework"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = rootProject.extra.get("versionCode") as Int
        versionName = rootProject.extra.get("versionName") as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("int", "DB_VERSION", "1")
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
    implementation(project(path = ":feature:auth"))
    implementation(project(path = ":core:base"))
    implementation(project(path = ":core:data"))
    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:navigation"))

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
    implementation(libs.shimmer)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.room)
    ksp(libs.room.ksp)
    implementation(libs.room.ktx)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

}