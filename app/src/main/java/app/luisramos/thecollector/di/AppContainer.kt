package app.luisramos.thecollector.di

import app.luisramos.thecollector.data.Db
import app.luisramos.thecollector.usecases.*

interface AppContainer {
    val db: Db

    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveChannelUseCase: SaveChannelUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
    val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase

    val viewModelFactory: ViewModelProviderFactory
}