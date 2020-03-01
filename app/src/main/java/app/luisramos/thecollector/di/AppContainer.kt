package app.luisramos.thecollector.di

import app.luisramos.thecollector.ParentViewModel
import app.luisramos.thecollector.domain.*

interface AppContainer {
    val db: Db

    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveFeedUseCase: SaveFeedUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
    val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase
    val fetchFeedItemsUseCase: FetchFeedItemsUseCase
    val setFeedItemUnreadUseCase: SetUnreadFeedItemUseCase
    val fetchFeedUseCase: FetchFeedUseCase

    val activityViewModelProviderFactory: ActivityViewModelProviderFactory
    fun getViewModelFactory(parentViewModel: ParentViewModel): ViewModelProviderFactory
}