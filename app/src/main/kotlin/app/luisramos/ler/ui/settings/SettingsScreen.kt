package app.luisramos.ler.ui.settings

import android.view.View
import android.view.ViewGroup
import app.luisramos.ler.di.observe
import app.luisramos.ler.di.viewModels
import app.luisramos.ler.ui.screen.Screen

class SettingsScreen : Screen() {
    override fun createView(container: ViewGroup): View =
        SettingsView(container.context).apply {
            val viewModel: SettingsViewModel = viewModels(context)
            setupView(viewModel)
            setupViewModel(viewModel)
        }

    private fun SettingsView.setupView(viewModel: SettingsViewModel) {
        adapter.itemClickListener = { position ->
            viewModel.onItemClicked(position)
        }
    }

    private fun SettingsView.setupViewModel(viewModel: SettingsViewModel) {
        viewModel.items.observe(this) {
            adapter.submitList(it)
        }
    }
}