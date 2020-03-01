package app.luisramos.thecollector.data

import android.content.Context
import app.luisramos.thecollector.Database
import app.luisramos.thecollector.data.model.FeedUpdateMode
import app.luisramos.thecollector.domain.Db
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

val dateAdapter = object : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue)
    override fun encode(value: Date): Long = value.time
}

class DefaultDatabase(
    context: Context,
    private val dbContext: CoroutineContext
) : Db {

    private val driver = AndroidSqliteDriver(
        Database.Schema,
        context,
        "collection.db"
    )
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

    override suspend fun selectAllFeeds(): Flow<List<FeedsWithCount>> =
        queryWrapper.feedQueries.feedsWithCount().asFlow().mapToList(dbContext)

    override suspend fun selectFeed(id: Long): Feed? = withContext(dbContext) {
        queryWrapper.feedQueries.selectFeed(id).executeAsOneOrNull()
    }

    override suspend fun selectAllFeedItems(feedId: Long): Flow<List<SelectAll>> =
        queryWrapper.feedItemQueries.selectAll(feedId).asFlow().mapToList(dbContext)

    override suspend fun selectFeedItemsForFeed(feedId: Long): List<FeedItem> =
        withContext(dbContext) {
            queryWrapper.feedItemQueries.selectAllWithFeedId(feedId).executeAsList()
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

    override suspend fun setFeedItemUnread(id: Long, unread: Boolean) = withContext(dbContext) {
        queryWrapper.feedItemQueries.toggleUnread(id = id, unread = unread)
    }
}