package app.luisramos.thecollector.usecases

class FetchAndSaveChannelUseCase(
    private val fetchChannelUseCase: FetchChannelUseCase,
    private val saveChannelUseCase: SaveChannelUseCase
) {
    suspend fun fetchAndSaveChannel(url: String): Result<Boolean> {
        val channelResult = fetchChannelUseCase.fetchChannel(url)

        val channel = channelResult.getOrElse {
            return Result.failure(it)
        }

        return saveChannelUseCase.saveChannel(channel)
    }
}