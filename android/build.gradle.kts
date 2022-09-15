plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "io.github.succlz123"
version = "0.0.1"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":compose-screen"))
    implementation("androidx.activity:activity-compose:1.6.0")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "org.succlz123.lib.android"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}