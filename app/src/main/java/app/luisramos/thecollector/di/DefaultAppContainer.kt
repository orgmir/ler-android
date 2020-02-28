package app.luisramos.thecollector.di

import android.content.Context
import app.luisramos.thecollector.data.DefaultDatabase
import app.luisramos.thecollector.domain.*
import kotlinx.coroutines.Dispatchers

class DefaultAppContainer(context: Context) : AppContainer {

    override val db: Db = DefaultDatabase(context, Dispatchers.IO)

    override val fetchChannelUseCase = FetchChannelUseCase()
    override val fetchFeedsUseCase = FetchFeedsUseCase(db)
    override val saveFeedUseCase = SaveFeedUseCase(db)
    override val fetchAndSaveChannelUseCase = FetchAndSaveChannelUseCase(
        fetchChannelUseCase,
        saveFeedUseCase
    )
    override val fetchFeedsFromHtmlUseCase = FetchFeedsFromHtmlUseCase()
    override val fetchFeedItemsUseCase = FetchFeedItemsUseCase(db)
    override val setFeedItemUnreadUseCase = SetUnreadFeedItemUseCase(db)

    override val viewModelFactory = ViewModelProviderFactory(this)
}