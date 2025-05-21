plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.itis.androidhomework.detail"
    compileSdk = 35

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }

}

dependencies {

    implementation(project(path = ":core:base"))
    implementation(project(path = ":core:navigation"))
    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:utils"))
    implementation(project(path = ":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.shimmer)
    implementation(libs.glide)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.compose.bundle)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.material3)
    implementation(libs.bundles.coil)
}