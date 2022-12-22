plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.jacoco) apply true
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
    }
}

allprojects {

    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
        plugin(rootProject.libs.plugins.ktlint.get().pluginId)
    }

    detekt {
        toolVersion = rootProject.libs.plugins.detekt.get().version.toString()
        config = rootProject.files("$rootDir/config/detekt/config.yml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        reports.html.required.set(true)
        reports.txt.required.set(false)
        reports.sarif.required.set(false)
    }

    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    ktlint {
        debug.set(false)
        version.set(rootProject.libs.versions.ktlint)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)

        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    tasks.withType<JacocoReport>().configureEach {
        reports {
            xml.required.set(true)
            csv.required.set(true)
            html.required.set(true)
        }
    }
}

junitJacoco {
    excludes = mutableListOf(
        "**/*Factory*.*",
        "**/*Generated*.*",
        "**/*_di_*.*"
    )
}

jacoco {
    toolVersion = rootProject.libs.plugins.jacoco.get().version.toString()
}