package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.Feed
import app.luisramos.thecollector.data.FeedItem
import app.luisramos.thecollector.data.FeedsWithCount
import app.luisramos.thecollector.data.SelectAll
import app.luisramos.thecollector.data.model.FeedUpdateMode
import kotlinx.coroutines.flow.Flow
import java.util.*

interface Db {
    suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long

    suspend fun selectAllFeeds(): Flow<List<FeedsWithCount>>
    suspend fun selectFeed(id: Long): Feed?

    suspend fun selectAllFeedItems(feedId: Long): Flow<List<SelectAll>>
    suspend fun selectFeedItemsForFeed(feedId: Long): List<FeedItem>
    suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long
    ): Long

    suspend fun setFeedItemUnread(id: Long, unread: Boolean)
}