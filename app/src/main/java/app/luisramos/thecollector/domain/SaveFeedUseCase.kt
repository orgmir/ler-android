package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.model.FeedModel
import app.luisramos.thecollector.data.model.FeedUpdateMode
import java.io.IOException
import java.util.*

class SaveFeedUseCase(
    private val db: Db
) {
    suspend fun saveFeed(feedModel: FeedModel): Result<Boolean> {
        val (title, link) = feedModel

        if (title.isEmpty() || link.isEmpty()) {
            return Result.failure(IOException("No title or link for channel"))
        }

        val feed = db.findFeedByUpdateLink(feedModel.feedLink)

        val feedId = if (feed != null) {
            db.updateFeed(
                id = feed.id,
                title = title,
                link = link,
                description = feedModel.description,
                updateLink = feedModel.feedLink,
                updateMode = FeedUpdateMode.NONE
            )
        } else {
            db.insertFeed(
                title = title,
                link = link,
                description = feedModel.description,
                updateLink = feedModel.feedLink,
                updateMode = FeedUpdateMode.NONE,
                updatedAt = feedModel.updated
            )
        }

        val idLinkPairs = db.findFeedItemsIdsByFeedId(feedId)
        val idMap: Map<String, Long> = idLinkPairs.fold(mutableMapOf()) { acc, value ->
            acc[value.link] = value.id
            acc
        }
        val links = idLinkPairs.map { it.link }

        val itemsToUpdate = feedModel.items.filter { links.contains(it.link) }
        val itemsToInsert = feedModel.items - itemsToUpdate

        itemsToInsert.forEach {
            db.insertFeedItem(
                title = it.title,
                description = it.description,
                link = it.link,
                publishedAt = it.published,
                updatedAt = it.updated ?: Date(),
                feedId = feedId
            )
        }
        itemsToUpdate.forEach {
            idMap[it.link]?.let { id ->
                db.updateFeedItem(
                    id = id,
                    title = it.title,
                    description = it.description,
                    link = it.link,
                    publishedAt = it.published,
                    updatedAt = it.updated ?: Date()
                )
            }
        }

        return Result.success(true)
    }
}