package app.luisramos.thecollector

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.thecollector.data.Db
import app.luisramos.thecollector.data.DefaultDatabase
import app.luisramos.thecollector.ui.main.MainViewModel
import app.luisramos.thecollector.usecases.FetchAndSaveChannelUseCase
import app.luisramos.thecollector.usecases.FetchChannelUseCase
import app.luisramos.thecollector.usecases.FetchFeedsUseCase
import app.luisramos.thecollector.usecases.SaveChannelUseCase
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class App : Application() {

    val appContainer: AppContainer = DefaultAppContainer(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

interface AppContainer {
    val db: Db

    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveChannelUseCase: SaveChannelUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase

    val viewModelFactory: ViewModelProviderFactory
}


class DefaultAppContainer(context: Context) : AppContainer {

    override val db: Db = DefaultDatabase(context, Dispatchers.IO)


    override val fetchChannelUseCase = FetchChannelUseCase()
    override val fetchFeedsUseCase = FetchFeedsUseCase(db)
    override val saveChannelUseCase = SaveChannelUseCase(db)
    override val fetchAndSaveChannelUseCase =
        FetchAndSaveChannelUseCase(fetchChannelUseCase, saveChannelUseCase)

    override val viewModelFactory = ViewModelProviderFactory(this)
}


class ViewModelProviderFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(
                    appContainer.fetchFeedsUseCase,
                    appContainer.fetchAndSaveChannelUseCase
                ) as T
            else -> modelClass.newInstance()
        }

}