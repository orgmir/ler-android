package app.luisramos.ler.ui.sidemenu

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.postDelayed
import app.luisramos.ler.di.observe
import app.luisramos.ler.di.viewModels
import app.luisramos.ler.ui.ScaffoldView
import app.luisramos.ler.ui.screen.Screen
import app.luisramos.ler.ui.views.UiState

class SideMenuScreen : Screen() {

    override fun createView(container: ViewGroup): View = SideMenuView(container.context).apply {
        val viewModel: SideMenuViewModel by container.viewModels()

        adapter.onItemClick = { viewModel.onItemTapped(it) }

        addSubscriptionButton.setOnClickListener {
            (container as ScaffoldView).closeDrawer()
            // wait for the side menu animation to end...
            postDelayed(ScaffoldView.ANIM_DURATION) {
                viewModel.addSubscriptionButtonClicked()
            }
        }

        viewModel.uiState.observe(this) {
            when (it) {
                is UiState.Error ->
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                is UiState.Success ->
                    adapter.submitList(it.data)
            }
        }
    }
}