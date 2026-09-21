import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    // AS will auto update version
    id("com.android.application") version "9.4.1" apply false
    id(Dependencies.Build.Sqldelight) version Versions.Sqldelight apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("checkPreCI") {
    description = "Check pre CI config"

    val keystorePropsFile = File("keystore.properties")
    if (keystorePropsFile.exists()) {
        println("> keystore.properties found")
        val keystore = loadProperties("keystore.properties")
        val signingPath = keystore.getProperty("storeFile")
        val signingFile = File(signingPath)
        if (signingFile.exists()) {
            println("> Signing file found")
        } else {
            throw GradleException("Signing file not found at path ${keystore["storeFile"]}")
        }
    } else {
        throw GradleException("File not found keystore.properties at $projectDir")
    }
}
