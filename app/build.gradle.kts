plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.get().pluginId)
    alias(libs.plugins.ksp)
    id(libs.plugins.sqldelight.get().pluginId)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.ferelin.novaposhtanews"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ferelin.novaposhtanews"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.1.0"

        project.version = versionName!!
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
    implementation(libs.bundles.kotlin.coroutines)

    implementation(libs.jacoco)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.koin.android)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.sqldelight)
    implementation(libs.bundles.datastore)

    testImplementation(libs.junit)
    testImplementation(libs.bundles.koin.test)
    testImplementation(libs.mockk)
}

tasks.create("CI_VERSION_NAME") {
    print(project.version)
}

sqldelight {
    database("NovaPoshtaNewsDatabase") {
        packageName = "com.ferelin.novaposhtanews"
    }
}

protobuf {

    protoc {
        artifact = libs.protoc.artifact.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }
}
