val compose_version: String by extra
val library_version: String by extra
val android_compile_sdk: String by extra
val android_target_sdk: String by extra
val android_min_sdk: String by extra

plugins {
	id("org.jetbrains.compose")
	id("com.android.application")
	kotlin("android")
}

group = "com.darkrockstudios.example.richtexteditor"
version = library_version

dependencies {
	implementation(project(":richtexteditor"))
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.8.2")

	implementation("androidx.compose.ui:ui:$compose_version")
	implementation("androidx.compose.material:material:$compose_version")
	implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
}

android {
	namespace = "com.darkrockstudios.example.richtexteditor"
	compileSdk = android_compile_sdk.toInt()
	defaultConfig {
		applicationId = "com.darkrockstudios.example.richtexteditor"
		minSdk = android_min_sdk.toInt()
		lint.targetSdk = android_target_sdk.toInt()
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
	packaging {
		resources {
			excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
		}
	}
}