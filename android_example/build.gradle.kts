plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.quickbird.android_example"
        minSdk = 21
        targetSdk = 32
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")

    implementation("androidx.activity:activity-compose:1.5.0")

    implementation("androidx.compose.ui:ui:1.2.0-rc03")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0-rc03")
    implementation("androidx.compose.ui:ui-tooling:1.2.0-rc03")
    implementation("androidx.compose.material:material:1.2.0-rc03")
    implementation("androidx.compose.material:material-icons-extended:1.2.0-rc03")
    implementation("androidx.compose.foundation:foundation:1.2.0-rc03")

    implementation("androidx.navigation:navigation-compose:2.5.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.android.material:material:1.7.0-alpha02")
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")

    testImplementation(project(":snapshot"))
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(project(":snapshot"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.0-rc01")
}
