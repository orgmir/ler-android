package app.luisramos.ler.domain.work

import android.content.Context
import androidx.work.*
import app.luisramos.ler.App
import java.util.concurrent.TimeUnit

const val UPDATE_WORK_ID = "sync"

fun App.configureWorkManager(workerFactory: WorkerFactory) {
    val config = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
    WorkManager.initialize(this, config)
}

fun Context.enqueueFeedSyncWork(refreshData: Boolean = false) {
    val policy = if (refreshData) {
        ExistingPeriodicWorkPolicy.REPLACE
    } else {
        ExistingPeriodicWorkPolicy.KEEP
    }
    WorkManager.getInstance(this)
        .enqueueUniquePeriodicWork(
            UPDATE_WORK_ID,
            policy,
            PeriodicWorkRequestBuilder<FeedUpdateWorker>(15, TimeUnit.MINUTES).build()
        )
}