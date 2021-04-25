package app.luisramos.ler

import android.app.Application
import app.luisramos.ler.di.AppContainer
import app.luisramos.ler.di.DefaultAppContainer
import app.luisramos.ler.domain.work.configureWorkManager
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
        createNotificationChannel()
        scheduleFeedSyncWork()
        scheduleNewPostsLocalNotification()
    }

    protected open fun createAppContainer(): AppContainer = DefaultAppContainer(this)

    /*
     * Make sure that we always schedule the notification on app start,
     * so new app installs will have the notification registered by default.
     * Scheduling the notif replaces an existing scheduled work, if it already
     * exists, so it should be good to do this every app start
     */
    private fun scheduleNewPostsLocalNotification() {
        appContainer.apply {
            if (preferences.isNewPostNotificationEnabled) {
                val (hour, minute) = newPostsNotificationPreferencesUseCase.notifyHourMinute
                scheduleNewPostsNotifUseCase.schedule(hour, minute)
            }
        }
    }

    private fun scheduleFeedSyncWork() {
        appContainer.apply {
            scheduleFeedSyncUseCase.schedule()
        }
    }
}