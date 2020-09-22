package app.luisramos.ler

import android.app.Application
import android.content.Context
import androidx.work.*
import app.luisramos.ler.di.AppContainer
import app.luisramos.ler.di.DefaultAppContainer
import app.luisramos.ler.domain.work.FeedUpdateWorker
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val UPDATE_WORK_ID = "sync"

class App : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        AppCenter.start(this, "264ee049-5bbc-4cf2-92e5-720ef223f22d", Crashes::class.java)

        appContainer = DefaultAppContainer(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        configureWorkManager()

        enqueueFeedSyncWork()
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(appContainer.workerFactory)
            .build()
        WorkManager.initialize(this, config)
    }
}

fun Context.enqueueFeedSyncWork() {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)
        .build()

    val updateRequest =
        PeriodicWorkRequestBuilder<FeedUpdateWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

    WorkManager.getInstance(this)
        .enqueueUniquePeriodicWork(
            UPDATE_WORK_ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            updateRequest
        )
}