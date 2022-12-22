plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.jacoco) apply true
}

allprojects {
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