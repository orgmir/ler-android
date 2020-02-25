package app.luisramos.thecollector.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.thecollector.ui.main.MainViewModel
import app.luisramos.thecollector.ui.sidemenu.SideMenuViewModel
import app.luisramos.thecollector.ui.subscription.AddSubscriptionViewModel

class ViewModelProviderFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(appContainer.fetchFeedsUseCase) as T
            SideMenuViewModel::class.java -> SideMenuViewModel(appContainer.fetchFeedsUseCase) as T
            AddSubscriptionViewModel::class.java -> AddSubscriptionViewModel(
                appContainer.fetchFeedsFromHtmlUseCase,
                appContainer.fetchAndSaveChannelUseCase
            ) as T
            else -> modelClass.newInstance()
        }
}