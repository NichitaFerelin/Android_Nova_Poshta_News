plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.get().pluginId)
    alias(libs.plugins.ksp)
    id(libs.plugins.sqldelight.get().pluginId)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.serialization)
    id(libs.plugins.google.services.get().pluginId)
    id(libs.plugins.firebase.crashlytics.get().pluginId)
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

            buildConfigField("String", "BASE_MD_WEBSITE_URL", "\"https://novaposhta.md/\"")
            buildConfigField("String", "BASE_MD_API_URL", "\"https://srv2.novaposhta.md\"")
            buildConfigField("String", "BASE_UA_API_URL", "\"https://novaposhta.ua\"")
        }
        release {
            buildConfigField("String", "BASE_MD_WEBSITE_URL", "\"https://novaposhta.md/\"")
            buildConfigField("String", "BASE_MD_API_URL", "\"https://srv2.novaposhta.md\"")
            buildConfigField("String", "BASE_UA_API_URL", "\"https://novaposhta.ua\"")
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            unitTests.isReturnDefaultValues = true
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompiler.get()
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.material)

    implementation(libs.kotlin.stdlib)
    implementation(libs.bundles.kotlin.coroutines)

    implementation(libs.jacoco)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.bundles.compose)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.koin.android)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.sqldelight)
    implementation(libs.bundles.datastore)

    implementation(libs.jsoup)

    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)
    implementation(libs.bundles.firebase)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.bundles.koin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.ktor.mock)
    testImplementation(libs.sqldelight.sqllite.driver)
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
