package app.luisramos.ler.domain

import timber.log.Timber

class RefreshFeedsUseCase(
    private val db: Db,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) {
    suspend fun refreshFeedsUseCase(): Result<Boolean> {
        val feeds = db.selectAllFeeds()
        Timber.d("Updating ${feeds.size} feeds...")

        feeds.map { feed ->
            fetchAndSaveChannelUseCase.fetchAndSaveChannel(feed.updateLink)
                .onFailure {
                    Timber.e("  failure fetching ${feed.link}")
                    Timber.e(it)
                }
                .onSuccess { Timber.d("  done with ${feed.link}") }
        }

        Timber.d("Done updating feeds.")
        return Result.success(true)
    }
}