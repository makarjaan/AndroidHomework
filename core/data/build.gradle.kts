plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gradle.secrets.plugin)
}

android {
    namespace = "ru.itis.androidhomework.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

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
        buildConfig = true
    }


    defaultConfig {
        buildConfigField(
            "String",
            "OPEN_TRIP_MAP_BASE_URL",
            "\"https://api.opentripmap.com/0.1/ru/\""
        )
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

}

dependencies {

    implementation(project(path = ":core:domain"))


    implementation(libs.room)
    ksp(libs.room.ksp)
    implementation(libs.room.ktx)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.http.loggin.interceptor)
    implementation(libs.retrofit.gson.converter)
}