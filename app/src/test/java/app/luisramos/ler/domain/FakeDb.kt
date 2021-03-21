package app.luisramos.ler.domain

import app.luisramos.ler.data.*
import app.luisramos.ler.data.model.FeedUpdateMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

class FakeDb : Db {

    private val feedMap = mutableMapOf<Long, Feed>()
    private val feedItemMap = mutableMapOf<Long, FeedItem>()

    private var currId = 0L

    private fun generateId() = ++currId

    override suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long {
        val feed = Feed(
            id = generateId(),
            title = title,
            link = link,
            updateLink = updateLink,
            description = description,
            updateMode = updateMode,
            updateTimeInterval = 3600,
            notify = false,
            updatedAt = updatedAt,
            createdAt = Date()
        )
        feedMap[feed.id] = feed
        return feed.id
    }

    override suspend fun updateFeed(
        id: Long,
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode
    ): Long {
        feedMap[id] = feedMap[id]?.copy(
            title = title,
            link = link,
            updateLink = updateLink,
            description = description,
            updateMode = updateMode,
        ) ?: return -1L
        return id
    }

    override suspend fun deleteFeed(id: Long) {
        feedMap.remove(id)
    }

    override suspend fun selectAllFeeds(): List<Feed> = feedMap.values.toList()

    override suspend fun selectAllFeedsWithCount(): Flow<List<FeedsWithCount>> = flowOf(
        feedMap.values.toList().map {
            FeedsWithCount(
                id = it.id,
                itemsCount = 0.0,
                title = it.title,
                titleOrder = it.title
            )
        }
    )

    override suspend fun findFeedById(id: Long): Feed? = feedMap[id]

    override suspend fun findFeedByUpdateLink(updateLink: String): Feed? =
        feedMap.values.firstOrNull { it.updateLink.equals(updateLink, ignoreCase = true) }

    override suspend fun toggleFeedNotify(id: Long) {
        feedMap[id] = feedMap[id]?.run { copy(notify = notify?.not() ?: false) } ?: return
    }

    override suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long
    ): Long {
        val feedItem = FeedItem(
            id = generateId(),
            title = title,
            description = description,
            link = link,
            unread = false,
            publishedAt = publishedAt,
            updatedAt = updatedAt,
            createdAt = Date(),
            feedId = feedId
        )
        feedItemMap[feedItem.id] = feedItem
        return feedItem.id
    }

    override suspend fun updateFeedItem(
        id: Long,
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date
    ) {
        feedItemMap[id] = feedItemMap[id]?.copy(
            title = title,
            description = description,
            link = link,
            publishedAt = publishedAt,
            updatedAt = updatedAt
        ) ?: return
    }

    override suspend fun deleteFeedItemsByFeedId(feedId: Long) {
        feedItemMap.values.filter { it.feedId == feedId }.forEach {
            feedItemMap.remove(it.id)
        }
    }

    override suspend fun setFeedItemUnread(id: Long, unread: Boolean) {
        feedItemMap[id] = feedItemMap[id]?.copy(unread = unread) ?: return
    }

    override suspend fun setFeedItemsUnreadForFeedId(feedId: Long, unread: Boolean) {
        feedItemMap.values.filter { it.feedId == feedId }.forEach {
            setFeedItemUnread(it.id, unread)
        }
    }

    override suspend fun selectAllFeedItems(feedId: Long, showRead: Long): Flow<List<SelectAll>> =
        flowOf(feedItemMap.values.filter { (feedId == -1L || it.feedId == feedId) && (showRead == 1L && it.unread == true) }
            .map {
                SelectAll(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    link = it.link,
                    unread = it.unread,
                    publishedAt = it.publishedAt,
                    updatedAt = it.updatedAt,
                    createdAt = it.createdAt,
                    feedId = it.feedId,
                    feedTitle = feedMap[feedId]?.title.orEmpty(),
                    feedNotify = false
                )
            })

    override suspend fun findFeedItemsIdsByFeedId(feedId: Long): List<FindAllIdsByFeedId> =
        feedItemMap.values
            .filter { it.feedId == feedId }
            .map { FindAllIdsByFeedId(it.id, it.link) }

}