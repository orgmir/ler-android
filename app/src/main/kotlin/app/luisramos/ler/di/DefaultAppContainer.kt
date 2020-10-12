package app.luisramos.ler.di

import android.content.Context
import androidx.work.WorkerFactory
import app.luisramos.ler.data.DefaultDatabase
import app.luisramos.ler.domain.*
import app.luisramos.ler.ui.ScaffoldViewModel
import kotlinx.coroutines.Dispatchers

class DefaultAppContainer(context: Context) : AppContainer {

    override val db: Db = DefaultDatabase(context, Dispatchers.IO)

    override val preferences: Preferences =
        DefaultPreferences(context.getSharedPreferences("default", Context.MODE_PRIVATE))

    override val fetchChannelUseCase: FetchChannelUseCase = DefaultFetchChannelUseCase()
    override val fetchFeedsUseCase: FetchFeedsUseCase = DefaultFetchFeedsUseCase(db)
    override val saveFeedUseCase: SaveFeedUseCase = DefaultSaveFeedUseCase(db)
    override val fetchAndSaveChannelUseCase = DefaultFetchAndSaveChannelUseCase(
        fetchChannelUseCase,
        saveFeedUseCase
    )
    override val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase =
        DefaultFetchFeedsFromHtmlUseCase()
    override val fetchFeedItemsUseCase = FetchFeedItemsUseCase(db)
    override val setFeedItemUnreadUseCase = SetUnreadFeedItemUseCase(db)
    override val fetchFeedUseCase: FetchFeedUseCase = DefaultFetchFeedUseCase(db)
    override val refreshFeedsUseCase: RefreshFeedsUseCase =
        DefaultRefreshFeedsUseCase(db, fetchAndSaveChannelUseCase)
    override val deleteFeedUseCase = DeleteFeedUseCase(db)

    override val workerFactory: WorkerFactory = DefaultWorkerFactory(this)

    private var _viewModelFactory: ViewModelProviderFactory? = null
    override fun getViewModelFactory(parentViewModel: ScaffoldViewModel): ViewModelProviderFactory {
        if (_viewModelFactory == null) {
            _viewModelFactory = ViewModelProviderFactory(this, parentViewModel)
        }
        return _viewModelFactory!!
    }

    override val activityViewModelProviderFactory = ActivityViewModelProviderFactory(this)
}