package app.luisramos.ler.domain

import app.luisramos.ler.data.FeedsWithCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

interface FetchFeedsUseCase {
    suspend fun fetchFeeds(): Flow<Result<List<FeedsWithCount>>>
}

class DefaultFetchFeedsUseCase(
    val db: Db
) : FetchFeedsUseCase {
    override suspend fun fetchFeeds(): Flow<Result<List<FeedsWithCount>>> =
        db.selectAllFeedsWithCount().map { Result.success(it) }
}

class FakeFetchFeedsUseCase(
    var mockFlow: Flow<Result<List<FeedsWithCount>>> = emptyFlow()
) : FetchFeedsUseCase {
    override suspend fun fetchFeeds(): Flow<Result<List<FeedsWithCount>>> = mockFlow
}