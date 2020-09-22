package app.luisramos.ler.data

import android.content.Context
import app.luisramos.ler.Database
import app.luisramos.ler.data.model.FeedUpdateMode
import app.luisramos.ler.domain.Db
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
        updateLink: String,
        updateMode: FeedUpdateMode,
        updatedAt: Date
    ): Long =
        withContext(dbContext) {
            queryWrapper.feedQueries.insertFeed(
                title = title,
                link = link,
                description = description,
                updateLink = updateLink,
                updateMode = updateMode,
                updateTimeInterval = 3600,
                updatedAt = updatedAt,
                createdAt = Date()
            )
            queryWrapper.feedQueries.lastInsertRowId().executeAsOne()
        }

    override suspend fun updateFeed(
        id: Long,
        title: String,
        link: String,
        description: String?,
        updateLink: String,
        updateMode: FeedUpdateMode
    ): Long = withContext(dbContext) {
        queryWrapper.feedQueries.updateFeed(
            id = id,
            title = title,
            link = link,
            description = description,
            updateLink = updateLink,
            updateMode = updateMode,
            updateTimeInterval = 3600,
            updatedAt = Date()
        )
        id
    }

    override suspend fun deleteFeed(id: Long) = withContext(dbContext) {
        queryWrapper.feedQueries.deleteFeedById(id)
    }

    override suspend fun selectAllFeeds(): List<Feed> = withContext(dbContext) {
        queryWrapper.feedQueries.selectAll().executeAsList()
    }

    override suspend fun selectAllFeedsWithCount(): Flow<List<FeedsWithCount>> =
        queryWrapper.feedQueries.feedsWithCount().asFlow().mapToList(dbContext)

    override suspend fun findFeedById(id: Long): Feed? = withContext(dbContext) {
        queryWrapper.feedQueries.findFeedById(id).executeAsOneOrNull()
    }

    override suspend fun findFeedByUpdateLink(updateLink: String): Feed? = withContext(dbContext) {
        queryWrapper.feedQueries.findFeedByUpdateLink(updateLink).executeAsOneOrNull()
    }

    override suspend fun selectAllFeedItems(feedId: Long, showRead: Long): Flow<List<SelectAll>> =
        queryWrapper.feedItemQueries.selectAll(feedId, showRead).asFlow().mapToList(dbContext)

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
            unread = 1,
            publishedAt = publishedAt,
            updatedAt = updatedAt,
            createdAt = Date(),
            feedId = feedId
        )
        queryWrapper.feedItemQueries.lastInsertRowId().executeAsOne()
    }

    override suspend fun updateFeedItem(
        id: Long,
        title: String,
        description: String?,
        link: String,
        publishedAt: Date,
        updatedAt: Date
    ) = withContext(dbContext) {
        queryWrapper.feedItemQueries.updateFeedItem(
            id = id,
            title = title,
            description = description,
            link = link,
            publishedAt = publishedAt,
            updatedAt = updatedAt
        )
    }

    override suspend fun deleteFeedItemWithFeedId(feedId: Long) = withContext(dbContext) {
        queryWrapper.feedItemQueries.deleteFeedItemsByFeedId(feedId)
    }

    override suspend fun setFeedItemUnread(id: Long, unread: Boolean) = withContext(dbContext) {
        queryWrapper.feedItemQueries.toggleUnread(id = id, unread = unread.toLong())
    }

    override suspend fun setFeedItemsUnreadForFeedId(feedId: Long, unread: Boolean) =
        withContext(dbContext) {
            queryWrapper.feedItemQueries.updateFeedItemUnreadWithFeedId(unread.toLong(), feedId)
        }

    override suspend fun findFeedItemsIdsByFeedId(feedId: Long): List<FindAllIdsByFeedId> =
        withContext(dbContext) {
            queryWrapper.feedItemQueries.findAllIdsByFeedId(feedId).executeAsList()
        }
}

fun Boolean.toLong(): Long = if (this) 1L else 0L
fun Long.toBoolean(): Boolean = this != 0L