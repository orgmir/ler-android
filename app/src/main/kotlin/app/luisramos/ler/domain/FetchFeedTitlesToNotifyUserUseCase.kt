package app.luisramos.ler.domain

import java.util.*

interface FetchFeedTitlesToNotifyUserUseCase {
    suspend fun fetchFeedTitlesWithNewPosts(): List<String>
}

class DefaultFetchFeedTitlesToNotifyUserUseCase(
    private val db: Db
) : FetchFeedTitlesToNotifyUserUseCase {
    override suspend fun fetchFeedTitlesWithNewPosts(): List<String> {
        val lastUpdateTimestamp = Calendar.getInstance(Locale.getDefault()).run {
            add(Calendar.DAY_OF_MONTH, -1)
            time
        }
        return db.selectAllNotifyFeedTitles(lastUpdateTimestamp)
    }
}
