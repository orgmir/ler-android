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
import kotlin.coroutines.coroutineContext

interface Db {
    suspend fun insertFeed(title: String, url: String, updateMode: FeedUpdateMode): Long
    suspend fun selectAllFeeds(): List<Feed>
}

val dateAdapter = object : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue)
    override fun encode(value: Date): Long = value.time
}

class DefaultDatabase(context: Context, coroutineContext: CoroutineContext) : Db {

    private val driver = AndroidSqliteDriver(Database.Schema, context, "collection.db")
    private val queryWrapper = Database(
        driver = driver,
        feedAdapter = Feed.Adapter(
            updateModeAdapter = EnumColumnAdapter(),
            createdAtAdapter = dateAdapter,
            updatedAtAdapter = dateAdapter,
            publishedAtAdapter = dateAdapter
        ),
        feedItemAdapter = FeedItem.Adapter(
            publishedAtAdapter = dateAdapter,
            updatedAtAdapter = dateAdapter,
            createdAtAdapter = dateAdapter
        )
    )


    override suspend fun insertFeed(title: String, link: String, updateMode: FeedUpdateMode): Long =
        withContext(coroutineContext) {
            queryWrapper.feedQueries.insertFeed(
                title = title,
                url = url,
                updateMode = updateMode,
                updateTimeInterval = 3600,
                createdAt = Date()
            )
            queryWrapper.feedQueries.lastInsertRowId().executeAsOne()
        }

    override suspend fun selectAllFeeds(): List<Feed> = withContext(coroutineContext) {
        queryWrapper.feedQueries.selectAll().executeAsList()
    }
}
