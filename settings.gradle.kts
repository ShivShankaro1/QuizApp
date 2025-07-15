pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        // KSP plugin — MATCH this with Kotlin version you're using (e.g., 1.9.24)
        id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QuizApp"
include(":app")
