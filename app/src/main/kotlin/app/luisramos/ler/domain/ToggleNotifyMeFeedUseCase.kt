package app.luisramos.ler.domain

interface ToggleNotifyMeFeedUseCase {
    suspend fun toggleNotifyMe(feedId: Long)
}

class DefaultToggleNotifyMeFeedUseCase(
    private val db: Db
) : ToggleNotifyMeFeedUseCase {
    override suspend fun toggleNotifyMe(feedId: Long) {
        db.toggleFeedNotify(feedId)
    }
}