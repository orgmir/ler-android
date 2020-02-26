package app.luisramos.thecollector.data

import android.content.Context
import app.luisramos.thecollector.Database
import app.luisramos.thecollector.data.model.FeedUpdateMode
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

interface Db {
    suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long

    suspend fun selectAllFeeds(): List<Feed>

    suspend fun selectAllFeedItems(): List<FeedItem>
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

val dateAdapter = object : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue)
    override fun encode(value: Date): Long = value.time
}

class DefaultDatabase(
    context: Context,
    private val dbContext: CoroutineContext
) : Db {

    private val driver = AndroidSqliteDriver(Database.Schema, context, "collection.db")
    private val queryWrapper = Database(
        driver = driver,
        feedAdapter = Feed.Adapter(
            updateModeAdapter = EnumColumnAdapter(),
            createdAtAdapter = dateAdapter,
            updatedAtAdapter = dateAdapter
        ),
        feedItemAdapter = FeedItem.Adapter(
            publishedAtAdapter = dateAdapter,
            updatedAtAdapter = dateAdapter,
            createdAtAdapter = dateAdapter
        )
    )

    override suspend fun insertFeed(
        title: String,
        link: String,
        description: String?,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long =
        withContext(dbContext) {
            queryWrapper.feedQueries.insertFeed(
                title = title,
                link = link,
                description = description,
                updateMode = updateMode,
                updateTimeInterval = 3600,
                updatedAt = updatedAt,
                createdAt = Date()
            )
            queryWrapper.feedQueries.lastInsertRowId().executeAsOne()
        }

    override suspend fun selectAllFeeds(): List<Feed> = withContext(dbContext) {
        queryWrapper.feedQueries.selectAll().executeAsList()
    }

    override suspend fun selectAllFeedItems(): List<FeedItem> = withContext(dbContext) {
        queryWrapper.feedItemQueries.selectAll().executeAsList()
    }

    override suspend fun selectFeedItemsForFeed(feedId: Long): List<FeedItem> =
        withContext(dbContext) {
            queryWrapper.feedItemQueries.selectAllForFeedId(feedId).executeAsList()
        }

    override suspend fun insertFeedItem(
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date,
        feedId: Long
    ): Long = withContext(dbContext) {
        queryWrapper.feedItemQueries.insertFeedItem(
            title = title,
            description = description,
            link = link,
            unread = true,
            publishedAt = publishedAt,
            updatedAt = updatedAt,
            createdAt = Date(),
            feedId = feedId
        )
        queryWrapper.feedItemQueries.lastInsertRowId().executeAsOne()
    }
}
