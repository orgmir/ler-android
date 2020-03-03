package app.luisramos.ler.domain

import app.luisramos.ler.data.Feed

class FetchFeedUseCase(
    private val db: Db
) {
    suspend fun fetch(id: Long): Feed? = db.findFeedById(id)
}