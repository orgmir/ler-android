package app.luisramos.ler.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import java.util.*

class NavigatingActivityViewModel : ViewModel() {
    private val storesMap = HashMap<Screen, ViewModelStore>()

    fun getStore(screen: Screen) =
        storesMap[screen] ?: ViewModelStore().also { storesMap[screen] = it }

    fun clearStore(screen: Screen) {
        storesMap[screen]?.clear()
        storesMap.remove(screen)
    }

    override fun onCleared() {
        storesMap.values.forEach { it.clear() }
        storesMap.clear()
        super.onCleared()
    }
}