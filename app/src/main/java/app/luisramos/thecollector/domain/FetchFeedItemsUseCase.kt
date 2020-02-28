package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.SelectAll

class FetchFeedItemsUseCase(
    private val db: Db
) {
    suspend fun fetch(): Result<List<SelectAll>> =
        Result.success(db.selectAllFeedItems())
}