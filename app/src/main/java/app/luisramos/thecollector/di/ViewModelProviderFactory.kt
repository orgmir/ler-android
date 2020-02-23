package app.luisramos.thecollector.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.thecollector.ui.main.AddSubscriptionViewModel
import app.luisramos.thecollector.ui.main.MainViewModel

class ViewModelProviderFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(appContainer.fetchFeedsUseCase) as T
            AddSubscriptionViewModel::class.java -> AddSubscriptionViewModel(
                appContainer.fetchFeedsFromHtmlUseCase,
                appContainer.fetchAndSaveChannelUseCase
            ) as T
            else -> modelClass.newInstance()
        }
}