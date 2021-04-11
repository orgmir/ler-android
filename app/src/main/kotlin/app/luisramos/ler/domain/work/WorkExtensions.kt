package app.luisramos.ler.domain.work

import android.content.Context
import androidx.work.*
import app.luisramos.ler.App
import java.util.concurrent.TimeUnit

const val UPDATE_WORK_ID = "sync"
const val LOCAL_NOTIF_WORK_ID = "new_post_notif_id"
const val LOCAL_NOTIF_TAG = "new_post_notif_tag"

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

fun Context.enqueueNewPostsLocalNotification(delay: Long) {
    val feedUpdateWork = OneTimeWorkRequestBuilder<FeedUpdateWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()
    val showLocalNotifWork = OneTimeWorkRequestBuilder<ShowNewPostsLocalNotifWorker>().build()
    val rescheduleWork = OneTimeWorkRequestBuilder<RescheduleLocalNotifNewPostsWorker>().build()
    WorkManager.getInstance(this)
        .beginUniqueWork(LOCAL_NOTIF_WORK_ID, ExistingWorkPolicy.REPLACE, feedUpdateWork)
        .then(showLocalNotifWork)
        .then(rescheduleWork)
        .enqueue()
}

fun Context.cancelNewPostsLocalNotification() {
    WorkManager.getInstance(this)
        .cancelUniqueWork(LOCAL_NOTIF_WORK_ID)
}