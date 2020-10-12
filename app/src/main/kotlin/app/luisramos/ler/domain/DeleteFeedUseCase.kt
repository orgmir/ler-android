package app.luisramos.ler.domain

class DeleteFeedUseCase(private val db: Db) {
    suspend fun deleteFeed(id: Long) {
        db.deleteFeed(id)
        db.deleteFeedItemsByFeedId(id)
    }
}