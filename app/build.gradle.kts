plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
////    id("kotlin-kapt")
//    id("org.jetbrains.kotlin.kapt")
    id("com.google.devtools.ksp")

////    id("com.google.dagger.hilt.android")
////    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.shopfun"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shopfun"
        minSdk = 24
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp(libs.androidx.room.ktx)

//    kapt(libs.androidx.room.compiler)
//    kapt (libs.androidx.room.ktx)

//    kapt "androidx.room:room-compiler:2.4.2"
//    implementation "androidx.room:room-runtime:2.4.2"
//    implementation "androidx.room:room-ktx:2.4.2"

//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0"  // or the latest version

}