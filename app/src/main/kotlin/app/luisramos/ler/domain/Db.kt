package app.luisramos.ler.domain

import app.luisramos.ler.data.Feed
import app.luisramos.ler.data.FeedsWithCount
import app.luisramos.ler.data.FindAllIdsByFeedId
import app.luisramos.ler.data.SelectAll
import app.luisramos.ler.data.model.FeedUpdateMode
import kotlinx.coroutines.flow.Flow
import java.util.*

interface Db {
    suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode,
        updatedAt: Date,
        createdAt: Date = Date()
    ): Long

    suspend fun updateFeed(
        id: Long,
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode
    ): Long

    suspend fun deleteFeed(id: Long)
    suspend fun selectAllFeeds(): List<Feed>
    suspend fun selectAllFeedsWithCount(): Flow<List<FeedsWithCount>>
    suspend fun findFeedById(id: Long): Feed?
    suspend fun findFeedByUpdateLink(updateLink: String): Feed?
    suspend fun toggleFeedNotify(id: Long)
    suspend fun selectAllNotifyFeedTitles(createdAfter: Date): List<String>

    suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long,
        createdAt: Date = Date(),
    ): Long

    suspend fun updateFeedItem(
        id: Long,
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date
    )

    suspend fun deleteFeedItemsByFeedId(feedId: Long)

    suspend fun setFeedItemUnread(id: Long, unread: Boolean)
    suspend fun setFeedItemsUnreadForFeedId(feedId: Long, unread: Boolean)
    suspend fun selectAllFeedItems(feedId: Long, showRead: Long): Flow<List<SelectAll>>
    suspend fun findFeedItemsIdsByFeedId(feedId: Long): List<FindAllIdsByFeedId>
}