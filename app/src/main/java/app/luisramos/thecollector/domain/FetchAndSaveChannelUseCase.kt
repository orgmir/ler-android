package app.luisramos.thecollector.domain

class FetchAndSaveChannelUseCase(
    private val fetchChannelUseCase: FetchChannelUseCase,
    private val saveFeedUseCase: SaveFeedUseCase
) {
    suspend fun fetchAndSaveChannel(url: String): Result<Boolean> {
        val channelResult = fetchChannelUseCase.fetchChannel(url)

        val channel = channelResult.getOrElse {
            return Result.failure(it)
        }

        return saveFeedUseCase.saveFeed(channel)
    }
}