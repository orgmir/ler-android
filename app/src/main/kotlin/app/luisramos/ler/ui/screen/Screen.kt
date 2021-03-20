package app.luisramos.ler.ui.screen

import android.view.View
import android.view.ViewGroup
import java.io.Serializable

abstract class Screen : Serializable {
    abstract fun createView(container: ViewGroup): View
}