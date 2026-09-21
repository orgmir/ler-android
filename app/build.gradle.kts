import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("com.android.application")
    id(Dependencies.Build.Sqldelight)
}

val keystoreProperties = File("keystore.properties").run {
    if (exists()) {
        loadProperties("keystore.properties")
    } else {
        Properties()
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    compileSdk = Versions.Build.CompileSdk

    namespace = "app.luisramos.ler"
    defaultConfig {
        applicationId = "app.luisramos.ler"
        minSdk = Versions.Build.MinSdk
        targetSdk = Versions.Build.TargetSdk
        versionCode = AppVersion.Code
        versionName = AppVersion.Name

        testInstrumentationRunner = "app.luisramos.ler.TestRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
            storeFile = file(keystoreProperties["storeFile"].toString())
            storePassword = keystoreProperties["storePassword"].toString()
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "kotlin/**",
                "**/*.kotlin_metadata",
                "META-INF/*.kotlin_module",
                "META-INF/*.version",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        )
    }
}

sqldelight {
    databases {
        create("LerDatabase") {
            packageName.set("app.luisramos.ler")
            schemaOutputDirectory.set(file("src/main/sqldelight/schema"))
            verifyMigrations.set(true)
        }
    }

}

dependencies {
    implementation(Dependencies.Kotlin)
    implementation(Dependencies.AndroidX.Core)
    implementation(Dependencies.AndroidX.RecyclerView)
    implementation(Dependencies.AndroidX.ViewModel)
    implementation(Dependencies.AndroidX.LiveData)
    implementation(Dependencies.AndroidX.Activity)
    implementation(Dependencies.AndroidX.Fragment)
    implementation(Dependencies.AndroidX.SwipeRefresh)
    implementation(Dependencies.AndroidX.Work)
    implementation(Dependencies.Material)
    implementation(Dependencies.SqlDelight.Android)
    implementation(Dependencies.SqlDelight.Coroutines)
    implementation(Dependencies.OkHttp)
    implementation(Dependencies.Coroutines)
    implementation(Dependencies.Timber)
    implementation(Dependencies.Jsoup)
    implementation(Dependencies.AppCenter)
    implementation(Dependencies.Contour)

    testImplementation(Dependencies.Test.JUnit)
    testImplementation(Dependencies.Test.Coroutines)
    testImplementation(Dependencies.Test.Kroclin)
    testImplementation(Dependencies.Test.Truth)
    testImplementation(Dependencies.Test.Mockito)

    androidTestImplementation(Dependencies.Test.Truth)
    androidTestImplementation(Dependencies.Test.Barista)
    androidTestImplementation(Dependencies.Test.AndroidX.Core)
    androidTestImplementation(Dependencies.Test.AndroidX.JUnit)
    androidTestImplementation(Dependencies.Test.AndroidX.Espresso)
    androidTestImplementation(Dependencies.Test.Screengrab)
    androidTestImplementation(Dependencies.Test.Radiography)
    androidTestImplementation(Dependencies.Test.Coroutines)
    androidTestImplementation(Dependencies.Test.WorkManager)
}
