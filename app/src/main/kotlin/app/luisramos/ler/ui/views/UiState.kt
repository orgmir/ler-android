package app.luisramos.ler.ui.views

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Error(val msg: String) : UiState<Nothing>()
    data class Success<T>(val data: List<T>) : UiState<T>()
}

val <T> UiState<T>.data get() = (this as? UiState.Success)?.data