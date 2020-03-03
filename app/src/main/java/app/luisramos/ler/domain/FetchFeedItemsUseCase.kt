package app.luisramos.ler.domain

import app.luisramos.ler.data.SelectAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchFeedItemsUseCase(
    private val db: Db
) {
    suspend fun fetch(feedId: Long, showRead: Long): Flow<Result<List<SelectAll>>> =
        db.selectAllFeedItems(feedId, showRead).map { Result.success(it) }
}