package app.luisramos.ler

import android.content.Context
import app.luisramos.ler.di.AppContainer
import app.luisramos.ler.di.DefaultAppContainer
import app.luisramos.ler.test.FakeApi
import app.luisramos.ler.test.FakeDb
import app.luisramos.ler.test.FakePreferences
import kotlinx.coroutines.Dispatchers

class TestAppContainer(
    context: Context
) : AppContainer by DefaultAppContainer(
    context = context,
    coroutineContext = Dispatchers.Unconfined,
    db = FakeDb(),
    preferences = FakePreferences(),
    api = FakeApi()
) {
    val fakeApi get() = api as FakeApi
    val fakePrefs get() = preferences as FakePreferences
    val fakeDb get() = db as FakeDb
}