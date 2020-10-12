package app.luisramos.ler.domain

import app.luisramos.ler.data.Feed
import app.luisramos.ler.data.model.FeedUpdateMode
import java.util.*

interface FetchFeedUseCase {
    suspend fun fetch(id: Long): Feed?
}

class DefaultFetchFeedUseCase(
    private val db: Db
) : FetchFeedUseCase {
    override suspend fun fetch(id: Long): Feed? = db.findFeedById(id)
}

class FakeFetchFeedUseCase(
    var mockFeed: Feed? = Feed(
        id = 1,
        title = "title",
        link = "link",
        updateLink = "udapteLink",
        description = null,
        updateMode = FeedUpdateMode.NONE,
        updateTimeInterval = 0,
        updatedAt = null,
        createdAt = Date()
    )
) : FetchFeedUseCase {
    override suspend fun fetch(id: Long): Feed? = mockFeed
}