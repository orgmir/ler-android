package app.luisramos.thecollector.domain

class SetUnreadFeedItemUseCase(
    private val db: Db
) {
    suspend fun setUnread(id: Long, unread: Boolean) {
        db.setFeedItemUnread(id, unread)
    }
}