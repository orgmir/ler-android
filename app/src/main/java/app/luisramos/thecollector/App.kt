package app.luisramos.thecollector

import android.app.Application
import androidx.work.*
import app.luisramos.thecollector.di.AppContainer
import app.luisramos.thecollector.di.DefaultAppContainer
import app.luisramos.thecollector.work.FeedUpdateWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val UPDATE_WORK_ID = "sync"

class App : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = DefaultAppContainer(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        configureWorkManager()

        enqueueSyncWork()
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(appContainer.workerFactory)
            .build()
        WorkManager.initialize(this, config)
    }

    private fun enqueueSyncWork() {
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
}