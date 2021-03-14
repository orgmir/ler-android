buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Build.Gradle)
        classpath(Dependencies.Build.Kotlin)
        classpath(Dependencies.Build.Sqldelight)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}