plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

buildscript {
    dependencies {
    }
}
