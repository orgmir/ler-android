package app.luisramos.ler.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.ler.ParentViewModel
import app.luisramos.ler.ui.main.MainViewModel
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel
import app.luisramos.ler.ui.subscription.AddSubscriptionViewModel

class ViewModelProviderFactory(
    private val appContainer: AppContainer,
    private val parentViewModel: ParentViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(
                parentViewModel,
                appContainer.fetchFeedItemsUseCase,
                appContainer.setFeedItemUnreadUseCase,
                appContainer.deleteFeedUseCase,
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