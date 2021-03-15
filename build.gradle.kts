import org.jetbrains.kotlin.konan.properties.loadProperties

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
        jcenter()
    }
}

tasks.register("checkPreCI") {
    description = "Check pre CI config"

    val keystorePropsFile = File("keystore.properties")
    if (keystorePropsFile.exists()) {
        println("> keystore.properties found")
        val keystore = loadProperties("keystore.properties")
        val signingFile = File(keystore["storeFile"].toString())
        if (signingFile.exists()) {
            println("> signing file found")
        } else {
            println("Error: signing file not found")
        }
    } else {
        println("Error: keystore.properties not found")
    }
}
