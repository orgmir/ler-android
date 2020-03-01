package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.Feed

class FetchFeedUseCase(
    private val db: Db
) {
    suspend fun fetch(id: Long): Feed? = db.selectFeed(id)
}