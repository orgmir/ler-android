package app.luisramos.ler.di

import androidx.work.WorkerFactory
import app.luisramos.ler.ParentViewModel
import app.luisramos.ler.domain.*

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
    val deleteFeedUseCase: DeleteFeedUseCase

    val activityViewModelProviderFactory: ActivityViewModelProviderFactory
    fun getViewModelFactory(parentViewModel: ParentViewModel): ViewModelProviderFactory
}