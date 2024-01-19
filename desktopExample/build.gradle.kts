import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val library_version: String by extra

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.darkrockstudios.example.desktop"
version = library_version

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":richtexteditor"))
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "com.darkrockstudios.example.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "richtext-compose"
            packageVersion = library_version
        }
    }
}
