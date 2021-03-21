package app.luisramos.ler.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.luisramos.ler.App
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.screen.NavigatingActivity
import app.luisramos.ler.ui.screen.Screen

inline fun <reified VM : ViewModel> Screen.viewModels(context: Context): VM {
    val activity = context as NavigatingActivity
    val appContainer = (activity.application as App).appContainer
    val parentViewModel: ScaffoldViewModel by activity.appViewModels()
    val factory = appContainer.getViewModelFactory(parentViewModel)
    val store = activity.getViewModelStoreForScreen(this)
    return ViewModelProvider(store, factory).get(VM::class.java)
}