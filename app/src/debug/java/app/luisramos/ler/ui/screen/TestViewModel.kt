package app.luisramos.ler.ui.screen

import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
    var wasCleared = false
    override fun onCleared() {
        wasCleared = true
        super.onCleared()
    }
}