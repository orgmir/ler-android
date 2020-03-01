package app.luisramos.thecollector.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.luisramos.thecollector.domain.Db
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class FeedUpdateWorker(
    val db: Db,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        // get all feeds that need updating
        // fetch all feeds
        // update DB with feeds
        Timber.d("DOING SOME WOOOOOOOOORK")
        Result.success()
    }
}