package app.luisramos.ler.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import app.luisramos.ler.App
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.R
import app.luisramos.ler.di.AppContainer

open class BaseFragment : Fragment() {

    val appContainer: AppContainer
        get() = (requireActivity().application as App).appContainer
    val parentViewModel: ScaffoldViewModel by activityViewModels()

    val toolbar by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

    inline fun <reified T : ViewModel> viewModels() = viewModels<T> {
        appContainer.getViewModelFactory(parentViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}