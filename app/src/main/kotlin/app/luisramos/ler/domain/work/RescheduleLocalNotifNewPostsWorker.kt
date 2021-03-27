package app.luisramos.ler.domain.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.luisramos.ler.domain.SaveNotifyTimePrefUseCase
import app.luisramos.ler.domain.ScheduleNewPostsNotifUseCase

class RescheduleLocalNotifNewPostsWorker(
    private val saveNotifUseCase: SaveNotifyTimePrefUseCase,
    private val scheduleNotifUseCase: ScheduleNewPostsNotifUseCase,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val (hour, minute) = saveNotifUseCase.notifyHourMinute
        scheduleNotifUseCase.schedule(hour, minute)
        return Result.success()
    }
}