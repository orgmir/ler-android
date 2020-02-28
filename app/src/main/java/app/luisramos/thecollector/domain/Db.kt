package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.Feed
import app.luisramos.thecollector.data.FeedItem
import app.luisramos.thecollector.data.SelectAll
import app.luisramos.thecollector.data.model.FeedUpdateMode
import java.util.*

interface Db {
    suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long

    suspend fun selectAllFeeds(): List<Feed>

    suspend fun selectAllFeedItems(): List<SelectAll>
    suspend fun selectFeedItemsForFeed(feedId: Long): List<FeedItem>
    suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long
    ): Long
}