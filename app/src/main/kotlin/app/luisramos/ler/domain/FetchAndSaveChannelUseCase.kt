package app.luisramos.ler.domain

interface FetchAndSaveChannelUseCase {
    suspend fun fetchAndSaveChannel(url: String): Result<Boolean>
}

class DefaultFetchAndSaveChannelUseCase(
    private val fetchChannelUseCase: FetchChannelUseCase,
    private val saveFeedUseCase: SaveFeedUseCase
) : FetchAndSaveChannelUseCase {
    override suspend fun fetchAndSaveChannel(url: String): Result<Boolean> {
        val channelResult = fetchChannelUseCase.fetchChannel(url)

        val channel = channelResult.getOrElse {
            return Result.failure(it)
        }

        return saveFeedUseCase.saveFeed(channel)
    }
}

class FakeFetchAndSaveChannelUseCase : FetchAndSaveChannelUseCase {
    var didCallFetchAndSave = emptyArray<String>()
    override suspend fun fetchAndSaveChannel(url: String): Result<Boolean> {
        didCallFetchAndSave += url
        return Result.success(true)
    }
}