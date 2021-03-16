package app.luisramos.ler.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.feeds.FeedItemsListViewModel
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel
import app.luisramos.ler.ui.subscription.AddSubscriptionViewModel

class ViewModelProviderFactory(
    private val appContainer: AppContainer,
    private val parentViewModel: ScaffoldViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            FeedItemsListViewModel::class.java -> FeedItemsListViewModel(
                parentViewModel,
                appContainer.fetchFeedItemsUseCase,
                appContainer.setFeedItemUnreadUseCase,
                appContainer.deleteFeedUseCase,
                appContainer.toggleNotifyMeFeedUseCase,
                appContainer.preferences
            ) as T
            SideMenuViewModel::class.java -> SideMenuViewModel(
                parentViewModel,
                appContainer.fetchFeedsUseCase
            ) as T
            AddSubscriptionViewModel::class.java -> AddSubscriptionViewModel(
                appContainer.fetchFeedsFromHtmlUseCase,
                appContainer.fetchAndSaveChannelUseCase
            ) as T
            else -> modelClass.newInstance()
        }
}