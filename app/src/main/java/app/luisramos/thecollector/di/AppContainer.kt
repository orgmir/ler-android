package app.luisramos.thecollector.di

import app.luisramos.thecollector.domain.Db
import app.luisramos.thecollector.domain.*

interface AppContainer {
    val db: Db

    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveFeedUseCase: SaveFeedUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
    val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase
    val fetchFeedItemsUseCase: FetchFeedItemsUseCase

    val viewModelFactory: ViewModelProviderFactory
}