plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.quickbird.android_example"
        minSdk = 21
        targetSdk = 33
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
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    namespace = "com.quickbird.android_example"
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")

    implementation("androidx.activity:activity-compose:1.7.0")

    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.4.0")

    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.core:core-splashscreen:1.0.0")

    testImplementation(project(":snapshot"))
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(project(":snapshot"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0")
}
