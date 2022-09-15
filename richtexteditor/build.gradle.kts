import org.jetbrains.compose.compose
import java.net.URI

val library_version: String by extra
val kotlin_version = extra["kotlin.version"] as String

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("maven-publish")
    id("signing")
}

val readableName = "RichText Editor Compose"
val repoUrl = "https://github.com/Wavesonics/richtext-compose-multiplatform"
group = "com.darkrockstudios"
description = "A multiplatform compose widget for editing rich text"
version = library_version

extra.apply {
    set("isReleaseVersion", !(version as String).endsWith("SNAPSHOT"))
}

kotlin {
    android {
        publishLibraryVariants("release")
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
                api("com.arkivanov.essenty:parcelable:0.6.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.ui.unit.ExperimentalUnitApi")
        }
    }

    val publicationsFromMainHost =
        listOf(jvm("desktop"), android()).map { it.name } + "kotlinMultiplatform"

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {
        repositories {
            /*
            maven {
                name = "GitHubPackages"
                url = URI("https://maven.pkg.github.com/wavesonics/richtext-compose-multiplatform")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
            */
            maven {
                val releaseRepo =
                    URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotRepo =
                    URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (extra["isReleaseVersion"] == true) releaseRepo else snapshotRepo
                credentials {
                    username = System.getenv("OSSRH_USERNAME") ?: "Unknown user"
                    password = System.getenv("OSSRH_PASSWORD") ?: "Unknown password"
                }
            }
        }
        publications {
            publications.withType<MavenPublication> {
                artifact(javadocJar.get())

                pom {
                    name.set(readableName)
                    description.set(project.description)
                    inceptionYear.set("2022")
                    url.set(repoUrl)
                    developers {
                        developer {
                            name.set("Adam Brown")
                            id.set("Wavesonics")
                        }
                    }
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/Wavesonics/richtext-compose-multiplatform.git")
                        developerConnection.set("scm:git:ssh://git@github.com/Wavesonics/richtext-compose-multiplatform.git")
                        url.set("https://github.com/Wavesonics/richtext-compose-multiplatform")
                    }
                }
            }

            // Filter which targets get published
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                //.configureEach { onlyIf { findProperty("isMainHost") == "true" } }*
            }
        }
    }
}

signing {
    val signingKey: String? = System.getenv("SIGNING_KEY")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(null, signingKey, signingPassword)
        sign(publishing.publications)
    } else {
        println("No signing credentials provided. Skipping Signing.")
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}