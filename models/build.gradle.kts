plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlinx.serialization)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    js {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.server.resources)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {

            }
        }

        val desktopMain by getting {
            dependencies {

            }
        }

        val jsMain by getting {
            dependencies {
            }
        }

        val iosMain by getting {
            dependencies {

            }
        }

    }
}

android {
    namespace = "ua.ukma.edu.danki.models"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
}
