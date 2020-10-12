package app.luisramos.ler.domain

import timber.log.Timber

interface RefreshFeedsUseCase {
    suspend fun refreshFeedsUseCase(updateProgressCallback: suspend (Float) -> Unit): Result<Boolean>
}

class DefaultRefreshFeedsUseCase(
    private val db: Db,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) : RefreshFeedsUseCase {
    override suspend fun refreshFeedsUseCase(updateProgressCallback: suspend (Float) -> Unit): Result<Boolean> {
        val feeds = db.selectAllFeeds()
        val feedCount = feeds.size.toFloat()
        Timber.d("Updating $feedCount feeds...")

        updateProgressCallback(0f)

        var feedUpdated = 0f
        val sendProgressUpdate = suspend {
            feedUpdated++
            updateProgressCallback(feedUpdated / feedCount)
        }

        feeds.forEach { feed ->
            val result = fetchAndSaveChannelUseCase.fetchAndSaveChannel(feed.updateLink)
            result.onFailure {
                Timber.e("  failure fetching ${feed.link}")
                Timber.e(it)
            }
                .onSuccess { Timber.d("  done with ${feed.link}") }
            sendProgressUpdate()
        }

        Timber.d("Done updating feeds.")
        return Result.success(true)
    }
}