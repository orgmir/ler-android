package app.luisramos.thecollector

import android.app.Application
import app.luisramos.thecollector.di.AppContainer
import app.luisramos.thecollector.di.DefaultAppContainer
import timber.log.Timber

class App : Application() {

    val appContainer: AppContainer =
        DefaultAppContainer(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}