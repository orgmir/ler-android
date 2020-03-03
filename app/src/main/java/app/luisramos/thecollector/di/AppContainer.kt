package app.luisramos.thecollector.di

import androidx.work.WorkerFactory
import app.luisramos.thecollector.ParentViewModel
import app.luisramos.thecollector.domain.*

interface AppContainer {
    val db: Db

    val workerFactory: WorkerFactory

    val preferences: Preferences

    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveFeedUseCase: SaveFeedUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
    val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase
    val fetchFeedItemsUseCase: FetchFeedItemsUseCase
    val setFeedItemUnreadUseCase: SetUnreadFeedItemUseCase
    val fetchFeedUseCase: FetchFeedUseCase
    val refreshFeedsUseCase: RefreshFeedsUseCase

    val activityViewModelProviderFactory: ActivityViewModelProviderFactory
    fun getViewModelFactory(parentViewModel: ParentViewModel): ViewModelProviderFactory
}