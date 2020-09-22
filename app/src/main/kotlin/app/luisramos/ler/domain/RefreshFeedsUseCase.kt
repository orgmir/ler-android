package app.luisramos.ler.domain

import timber.log.Timber

class RefreshFeedsUseCase(
    private val db: Db,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) {
    suspend fun refreshFeedsUseCase(updateProgressCallback: suspend (Float) -> Unit): Result<Boolean> {
        val feeds = db.selectAllFeeds()
        val feedCount = feeds.size.toFloat()
        Timber.d("Updating $feedCount feeds...")

        var feedUpdated = 0f
        val sendProgressUpdate = suspend {
            feedUpdated++
            updateProgressCallback(feedUpdated / feedCount)
        }

        feeds.forEach { feed ->
            fetchAndSaveChannelUseCase.fetchAndSaveChannel(feed.updateLink)
                .onFailure {
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