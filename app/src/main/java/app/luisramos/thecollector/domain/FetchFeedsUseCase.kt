package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.FeedsWithCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchFeedsUseCase(
    val db: Db
) {
    suspend fun fetchFeeds(): Flow<Result<List<FeedsWithCount>>> =
        db.selectAllFeeds().map { Result.success(it) }
}