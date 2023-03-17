plugins {
    kotlin("jvm")
    alias(libs.plugins.compose)
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.test)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.test)
    implementation(libs.koin.test.junit4)

    implementation(libs.ktor.core)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.mock)
    implementation(libs.ktor.java)

    implementation(libs.sqldelight.coroutines)
    implementation(libs.sqldelight.java)

    implementation(libs.bundles.slf4j)
}

compose.desktop {
    application {
        mainClass = "AppKt"
    }
}
