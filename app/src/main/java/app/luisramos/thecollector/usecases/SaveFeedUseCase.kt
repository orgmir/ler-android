package app.luisramos.thecollector.usecases

import app.luisramos.thecollector.data.Db
import app.luisramos.thecollector.data.model.FeedModel
import app.luisramos.thecollector.data.model.FeedUpdateMode
import java.io.IOException
import java.util.*

class SaveFeedUseCase(
    private val db: Db
) {
    suspend fun saveFeed(feed: FeedModel): Result<Boolean> {
        val (title, link) = feed

        if (title.isEmpty() || link.isEmpty()) {
            return Result.failure(IOException("No title or link for channel"))
        }

        val feedId = db.insertFeed(
            title = title,
            link = link,
            description = feed.description,
            updateMode = FeedUpdateMode.NONE,
            updatedAt = feed.updated
        )

        feed.items.forEach {
            db.insertFeedItem(
                title = it.title,
                description = it.description,
                link = it.link,
                publishedAt = it.published,
                updatedAt = it.updated ?: Date(),
                feedId = feedId
            )
        }

        return Result.success(true)
    }
}