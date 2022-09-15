val compose_version: String by extra
val library_version: String by extra

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "com.darkrockstudios.example.richtexteditor"
version = library_version

dependencies {
    implementation(project(":richtexteditor"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.5.1")

    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.darkrockstudios.example.richtexteditor"
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                File("proguard-rules.pro")
            )
        }
    }
    packagingOptions {
        resources {
            excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}