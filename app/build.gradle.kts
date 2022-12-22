plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    id("jacoco")
}

apply(from = "../jacoco.gradle.kts")

android {
    namespace = "com.ferelin.novaposhtanews"
    compileSdk = 33

    defaultConfig {
        applicationId  = "com.ferelin.novaposhtanews"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.1"
    }
    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            unitTests.isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.material)
    implementation(libs.kotlin.stdlib)
    implementation(libs.jacoco)

    testImplementation(libs.junit)
}

jacoco {
    toolVersion = libs.jacoco.get().versionConstraint.toString()
}