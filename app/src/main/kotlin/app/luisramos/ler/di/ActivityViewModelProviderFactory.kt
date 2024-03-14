package app.luisramos.ler.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.ler.ui.ScaffoldViewModel

class ActivityViewModelProviderFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            ScaffoldViewModel::class.java -> ScaffoldViewModel(appContainer.fetchFeedUseCase) as T
            else -> modelClass.newInstance()
        }
}