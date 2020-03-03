package app.luisramos.thecollector.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import app.luisramos.thecollector.work.FeedUpdateWorker

class DefaultWorkerFactory(
    private val appContainer: AppContainer
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        FeedUpdateWorker::class.java.name -> FeedUpdateWorker(
            appContainer.refreshFeedsUseCase,
            appContext,
            workerParameters
        )
        else -> null
    }
}