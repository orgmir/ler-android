package app.luisramos.ler.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.feeds.FeedItemsListViewModel
import app.luisramos.ler.ui.settings.SettingsViewModel
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel
import app.luisramos.ler.ui.subscription.AddSubscriptionViewModel

class ViewModelProviderFactory(
    private val appContainer: AppContainer,
    private val parentViewModel: ScaffoldViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = appContainer.run {
        when (modelClass) {
            FeedItemsListViewModel::class.java -> FeedItemsListViewModel(
                parentViewModel,
                fetchFeedItemsUseCase,
                setFeedItemUnreadUseCase,
                deleteFeedUseCase,
                toggleNotifyMeFeedUseCase,
                navigation,
                preferences
            ) as T
            SideMenuViewModel::class.java -> SideMenuViewModel(
                parentViewModel,
                fetchFeedsUseCase,
                navigation
            ) as T
            AddSubscriptionViewModel::class.java -> AddSubscriptionViewModel(
                parentViewModel,
                fetchFeedsFromHtmlUseCase,
                fetchAndSaveChannelUseCase,
                navigation
            ) as T
            SettingsViewModel::class.java -> SettingsViewModel(
                parentViewModel,
                saveNotifyTimePrefUseCase,
                navigation,
                preferences
            ) as T
            else -> modelClass.newInstance()
        }
    }
}