package app.luisramos.thecollector.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import app.luisramos.thecollector.App
import app.luisramos.thecollector.AppContainer

open class BaseFragment : Fragment() {

    val appContainer: AppContainer
        get() = (requireActivity().application as App).appContainer

    inline fun <reified T : ViewModel> viewModels() = viewModels<T> {
        appContainer.viewModelFactory
    }
}