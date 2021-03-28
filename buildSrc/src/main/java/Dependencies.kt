object Versions {
    object Build {
        const val CompileSdk = 30
        const val MinSdk = 26
        const val TargetSdk = 30
    }

    const val Kotlin = "1.4.31"
    const val Sqldelight = "1.4.4"
    const val Coroutines = "1.4.3"
}

object AppVersion {

    private const val Major = 1
    private const val Minor = 1
    private const val Patch = 0
    private val Build get() = System.getProperty("buildNumber")?.toInt() ?: 0

    val Name get() = "$Major.$Minor.$Patch"
    val FullName get() = "$Name.$Build"
    val Code get() = Major * 1000000 + Minor * 10000 + Patch * 100 + Build
}

object Dependencies {

    object Build {
        const val Gradle = "com.android.tools.build:gradle:7.0.0-alpha12"
        const val Kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
        const val Sqldelight = "com.squareup.sqldelight:gradle-plugin:${Versions.Sqldelight}"
    }

    const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin}"
    const val Material = "com.google.android.material:material:1.3.0-alpha03"
    const val OkHttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Coroutines}"
    const val Timber = "com.jakewharton.timber:timber:4.7.1"
    const val Jsoup = "org.jsoup:jsoup:1.12.2"
    const val AppCenter = "com.microsoft.appcenter:appcenter-crashes:3.0.0"
    const val Contour = "app.cash.contour:contour:1.1.0"

    object AndroidX {
        const val Core = "androidx.core:core-ktx:1.3.2"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.2"
        const val RecyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha06"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
        const val Activity = "androidx.activity:activity-ktx:1.1.0"
        const val Fragment = "androidx.fragment:fragment-ktx:1.2.5"
        const val SwipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val Work = "androidx.work:work-runtime-ktx:2.4.0"
    }

    object SqlDelight {
        const val Android = "com.squareup.sqldelight:android-driver:${Versions.Sqldelight}"
        const val Coroutines =
            "com.squareup.sqldelight:coroutines-extensions:${Versions.Sqldelight}"
    }

    object Test {
        const val JUnit = "junit:junit:4.13"
        const val Coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Coroutines}"
        const val Kroclin = "dev.luisramos.kroclin:snapshot:0.2.0"
        const val Truth = "com.google.truth:truth:1.0.1"
        const val Mockito = "org.mockito:mockito-core:3.5.13"
        const val Barista = "com.schibsted.spain:barista:3.8.0"
        const val Screengrab = "tools.fastlane:screengrab:2.0.0"
        const val Radiography = "com.squareup.radiography:radiography:2.3.0"

        object AndroidX {
            const val Core = "androidx.test:core-ktx:1.3.0"
            const val JUnit = "androidx.test.ext:junit:1.1.2"
            const val Espresso = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }
}