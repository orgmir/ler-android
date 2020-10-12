package app.luisramos.ler.domain.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.luisramos.ler.domain.RefreshFeedsUseCase

class FeedUpdateWorker(
    private val refreshFeedsUseCase: RefreshFeedsUseCase,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result {
        refreshFeedsUseCase.refreshFeedsUseCase {
            setProgress(workDataOf(Progress to it))
        }
        return Result.success()
    }
}