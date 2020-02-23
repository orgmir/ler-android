package app.luisramos.thecollector.di

import android.content.Context
import app.luisramos.thecollector.data.Db
import app.luisramos.thecollector.data.DefaultDatabase
import app.luisramos.thecollector.usecases.*
import kotlinx.coroutines.Dispatchers

class DefaultAppContainer(context: Context) : AppContainer {

    override val db: Db = DefaultDatabase(context, Dispatchers.IO)

    override val fetchChannelUseCase = FetchChannelUseCase()
    override val fetchFeedsUseCase = FetchFeedsUseCase(db)
    override val saveChannelUseCase = SaveChannelUseCase(db)
    override val fetchAndSaveChannelUseCase = FetchAndSaveChannelUseCase(
        fetchChannelUseCase,
        saveChannelUseCase
    )
    override val fetchFeedsFromHtmlUseCase = FetchFeedsFromHtmlUseCase()

    override val viewModelFactory =
        ViewModelProviderFactory(this)
}