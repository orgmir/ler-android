package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.SelectAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchFeedItemsUseCase(
    private val db: Db
) {
    suspend fun fetch(feedId: Long): Flow<Result<List<SelectAll>>> =
        db.selectAllFeedItems(feedId).map { Result.success(it) }
}