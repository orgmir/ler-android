package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.Feed

class FetchFeedsUseCase(
    val db: Db
) {
    suspend fun fetchFeeds(): Result<List<Feed>> =
        Result.success(db.selectAllFeeds())
}