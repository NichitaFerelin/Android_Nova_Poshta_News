plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id(libs.plugins.sqldelight.get().pluginId)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.uiTooling)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.animationGraphics)

                implementation(libs.kotlin.coroutines)

                implementation(libs.koin.core)
                implementation(libs.koin.ktor)
                implementation(libs.koin.test)
                implementation(libs.koin.test.junit4)

                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.mock)

                implementation(libs.sqldelight.coroutines)
            }
        }
        val androidMain by getting
        val jvmMain by getting
    }
}

android {
    namespace = "com.ferelin.shared"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}

sqldelight {
    database("NovaPoshtaNewsDatabase") {
        packageName = "com.ferelin.novaposhtanews"
        sourceFolders = listOf("sqldelight")
    }
}
