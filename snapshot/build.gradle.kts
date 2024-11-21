@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    `maven-publish`
}

kotlin {
    jvm()
    android {
        publishLibraryVariants("release")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val jvmTest by getting
        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                implementation("androidx.compose.ui:ui-test-junit4:1.4.0")
            }
        }

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
    }
    namespace = "com.quickbird.snapshot"
}

publishing {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        mavenLocal()
    }
}
