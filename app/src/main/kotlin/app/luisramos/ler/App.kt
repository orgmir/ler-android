package app.luisramos.ler

import android.app.Application
import app.luisramos.ler.di.AppContainer
import app.luisramos.ler.di.DefaultAppContainer
import app.luisramos.ler.domain.work.configureWorkManager
import app.luisramos.ler.domain.work.enqueueFeedSyncWork
import app.luisramos.ler.ui.notifications.createNotificationChannel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes
import timber.log.Timber

open class App : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        AppCenter.start(this, "264ee049-5bbc-4cf2-92e5-720ef223f22d", Crashes::class.java)

        appContainer = createAppContainer()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        configureWorkManager(appContainer.workerFactory)
        enqueueFeedSyncWork()

        createNotificationChannel()

        // TODO enqueue local notif?
    }

    protected open fun createAppContainer(): AppContainer = DefaultAppContainer(this)
}