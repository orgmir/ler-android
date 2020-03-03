package app.luisramos.ler.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.luisramos.ler.domain.RefreshFeedsUseCase
import kotlinx.coroutines.coroutineScope

class FeedUpdateWorker(
    private val refreshFeedsUseCase: RefreshFeedsUseCase,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        refreshFeedsUseCase.refreshFeedsUseCase()
        Result.success()
    }
}