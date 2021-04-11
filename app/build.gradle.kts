import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.squareup.sqldelight")
}

val keystoreProperties = File("keystore.properties").run {
    if (exists()) {
        loadProperties("keystore.properties")
    } else {
        Properties()
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdkVersion(Versions.Build.CompileSdk)

    defaultConfig {
        applicationId = "app.luisramos.ler"

        minSdkVersion(Versions.Build.MinSdk)
        targetSdkVersion(Versions.Build.TargetSdk)

        versionCode(AppVersion.Code)
        versionName(AppVersion.Name)

        testInstrumentationRunner = "app.luisramos.ler.TestRunner"
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

    sourceSets["main"].java {
        srcDir("src/main/kotlin")
    }

    sourceSets["test"].java {
        srcDir("src/sharedTest/java")
    }

    sourceSets["androidTest"].java {
        srcDir("src/sharedTest/java")
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

    sqldelight {
        database("LerDatabase") {
            packageName = "app.luisramos.ler"
            schemaOutputDirectory = file("src/main/sqldelight/schema")
            verifyMigrations = true
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOfNotNull(
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}

dependencies {
    implementation(Dependencies.Kotlin)
    implementation(Dependencies.AndroidX.Core)
    implementation(Dependencies.AndroidX.ConstraintLayout)
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
