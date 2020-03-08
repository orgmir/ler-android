package app.luisramos.ler.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.luisramos.ler.domain.RefreshFeedsUseCase
import kotlinx.coroutines.coroutineScope

class FeedUpdateWorker(
    private val refreshFeedsUseCase: RefreshFeedsUseCase,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result = coroutineScope {
        refreshFeedsUseCase.refreshFeedsUseCase {
            setProgress(workDataOf(Progress to it))
        }
        Result.success()
    }
}