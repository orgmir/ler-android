package app.luisramos.ler.domain.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.luisramos.ler.domain.ShowNewPostsLocalNotifUseCase

class ShowNewPostsLocalNotifWorker(
    val useCase: ShowNewPostsLocalNotifUseCase,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        useCase.showLocalNotif()
        return Result.success()
    }
}
