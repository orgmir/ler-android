package app.luisramos.ler.domain

interface ScheduleFeedSyncUseCase {
    fun schedule()
}

typealias EnqueueFeedSyncWork = () -> Unit

class DefaultScheduleFeedSyncUseCase(
    private val preferences: Preferences,
    private val enqueueFeedSyncWork: EnqueueFeedSyncWork
) : ScheduleFeedSyncUseCase {
    override fun schedule() {
        if (preferences.isFeedRefreshEnabled) {
            enqueueFeedSyncWork()
        }
    }
}