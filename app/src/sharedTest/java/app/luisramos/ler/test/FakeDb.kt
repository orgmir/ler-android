package app.luisramos.ler.test

import app.luisramos.ler.data.*
import app.luisramos.ler.data.model.FeedUpdateMode
import app.luisramos.ler.domain.Db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates.observable

class FakeDb : Db, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Unconfined

    private val feedMapValuesFlow = MutableSharedFlow<List<Feed>>()
    private val feedMap = mutableMapOf<Long, Feed>()
    private val feedItemMapValuesFlow = MutableSharedFlow<List<FeedItem>>()
    private val feedItemMap = mutableMapOf<Long, FeedItem>()

    private var currId = 0L

    private fun generateId() = ++currId

    private suspend fun emitFeedUpdate() {
        feedMapValuesFlow.emit(feedMap.values.toList())
    }

    private suspend fun emitFeedItemsUpdate() {
        feedItemMapValuesFlow.emit(feedItemMap.values.toList())
    }

    override suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode,
        updatedAt: Date,
        createdAt: Date
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
            createdAt = createdAt
        )
        feedMap[feed.id] = feed
        emitFeedUpdate()
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
        val feed = feedMap[id] ?: return -1L
        feedMap[id] = feed.copy(
            title = title,
            link = link,
            updateLink = updateLink,
            description = description,
            updateMode = updateMode,
        )
        emitFeedUpdate()
        return id
    }

    override suspend fun deleteFeed(id: Long) {
        feedMap.remove(id)
        emitFeedUpdate()
    }

    override suspend fun selectAllFeeds(): List<Feed> = feedMap.values.toList()

    override suspend fun selectAllFeedsWithCount(): Flow<List<FeedsWithCount>> =
        feedMapValuesFlow.map { list ->
            list.map {
                FeedsWithCount(
                    id = it.id,
                    itemsCount = 0.0,
                    title = it.title,
                    titleOrder = it.title
                )
            }
        }


    override suspend fun findFeedById(id: Long): Feed? = feedMap[id]

    override suspend fun findFeedByUpdateLink(updateLink: String): Feed? =
        feedMap.values.firstOrNull { it.updateLink.equals(updateLink, ignoreCase = true) }

    override suspend fun toggleFeedNotify(id: Long) {
        val feed = feedMap[id] ?: return
        feedMap[id] = feed.copy(notify = feed.notify?.not() ?: false)
        emitFeedUpdate()
    }

    override suspend fun selectAllNotifyFeedTitles(createdAfter: Date): List<String> =
        feedMap.values.filter { it.notify == true && it.createdAt > createdAfter }.map { it.title }

    override suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long,
        createdAt: Date
    ): Long {
        val feedItem = FeedItem(
            id = generateId(),
            title = title,
            description = description,
            link = link,
            unread = false,
            publishedAt = publishedAt,
            updatedAt = updatedAt,
            createdAt = createdAt,
            feedId = feedId
        )
        feedItemMap[feedItem.id] = feedItem
        emitFeedItemsUpdate()
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
        val feedItem = feedItemMap[id] ?: return
        feedItemMap[id] = feedItem.copy(
            title = title,
            description = description,
            link = link,
            publishedAt = publishedAt,
            updatedAt = updatedAt
        )
        emitFeedItemsUpdate()
    }

    override suspend fun deleteFeedItemsByFeedId(feedId: Long) {
        val values = feedItemMap.values
        values.filter { it.feedId == feedId }.forEach {
            feedItemMap.remove(it.id)
        }
        if (feedItemMap.size != values.size) {
            emitFeedItemsUpdate()
        }
    }

    override suspend fun setFeedItemUnread(id: Long, unread: Boolean) {
        val feedItem = feedItemMap[id] ?: return
        feedItemMap[id] = feedItem.copy(unread = unread)
        emitFeedItemsUpdate()
    }

    override suspend fun setFeedItemsUnreadForFeedId(feedId: Long, unread: Boolean) {
        feedItemMap.values.filter { it.feedId == feedId }.forEach {
            setFeedItemUnread(it.id, unread)
        }
        emitFeedItemsUpdate()
    }

    override suspend fun selectAllFeedItems(feedId: Long, showRead: Long): Flow<List<SelectAll>> =
        feedItemMapValuesFlow.map { items ->
            items
                .filter {
                    (feedId == -1L || it.feedId == feedId) && (showRead == 1L && it.unread == true)
                }
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
                }
        }

    override suspend fun findFeedItemsIdsByFeedId(feedId: Long): List<FindAllIdsByFeedId> =
        feedItemMap.values
            .filter { it.feedId == feedId }
            .map { FindAllIdsByFeedId(it.id, it.link) }

}