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

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
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
