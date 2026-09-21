object Versions {
    object Build {
        const val CompileSdk = 36
        const val MinSdk = 26
        const val TargetSdk = 36
    }

    const val Kotlin = "2.4.20"
    const val Sqldelight = "2.4.0"
    const val Coroutines = "1.11.0"
    const val WorkManager = "2.11.2"
}

object AppVersion {

    private const val Major = 1
    private const val Minor = 1
    private const val Patch = 3
    private val Build get() = System.getProperty("buildNumber")?.toIntOrNull() ?: 0

    val Name get() = "$Major.$Minor.$Patch"
    val FullName get() = "$Name.$Build"
    val Code get() = Major * 1000000 + Minor * 10000 + Patch * 100 + Build
}

object Dependencies {

    object Build {
        const val Sqldelight = "app.cash.sqldelight"
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
        const val Work = "androidx.work:work-runtime-ktx:${Versions.WorkManager}"
    }

    object SqlDelight {
        const val Android = "app.cash.sqldelight:android-driver:${Versions.Sqldelight}"
        const val Coroutines =
            "app.cash.sqldelight:coroutines-extensions:${Versions.Sqldelight}"
    }

    object Test {
        const val JUnit = "junit:junit:4.13"
        const val Coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Coroutines}"
        const val Kroclin = "dev.luisramos.kroclin:snapshot:0.2.0"
        const val Truth = "com.google.truth:truth:1.0.1"
        const val Mockito = "org.mockito:mockito-core:3.5.13"
        const val Barista = "com.schibsted.spain:barista:3.9.0"
        const val Screengrab = "tools.fastlane:screengrab:2.0.0"
        const val Radiography = "com.squareup.radiography:radiography:2.3.0"
        const val WorkManager = "androidx.work:work-testing:${Versions.WorkManager}"

        object AndroidX {
            const val Core = "androidx.test:core-ktx:1.7.0"
            const val JUnit = "androidx.test.ext:junit:1.3.0"
            const val Espresso = "androidx.test.espresso:espresso-core:3.7.0"
        }
    }
}