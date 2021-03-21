package app.luisramos.ler.ui.screen

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.luisramos.ler.di.viewModels

class TestScreen(val title: String) : Screen() {
    lateinit var testViewModel: TestViewModel
    override fun createView(container: ViewGroup): View =
        TextView(container.context).apply {
            testViewModel = viewModels(context)
            text = title
        }
}