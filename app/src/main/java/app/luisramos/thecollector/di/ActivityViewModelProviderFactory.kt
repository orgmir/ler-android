package app.luisramos.thecollector.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.thecollector.ParentViewModel

class ActivityViewModelProviderFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            ParentViewModel::class.java -> ParentViewModel(appContainer.fetchFeedUseCase) as T
            else -> modelClass.newInstance()
        }
}