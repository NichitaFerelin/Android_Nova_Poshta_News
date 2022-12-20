plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.material)
    implementation(libs.kotlin.stdlib)
}

tasks.create("CI_VERSION_NAME") {
    print(project.version)
}