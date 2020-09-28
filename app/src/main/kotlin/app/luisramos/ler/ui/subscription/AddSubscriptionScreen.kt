package app.luisramos.ler.ui.subscription

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.ui.event.observeEvent
import app.luisramos.ler.ui.navigation.Screen
import app.luisramos.ler.ui.navigation.activity
import app.luisramos.ler.ui.navigation.goBack
import app.luisramos.ler.ui.navigation.onScreenExiting
import app.luisramos.ler.ui.views.focusAndShowKeyboard
import app.luisramos.ler.ui.views.hideKeyboard

class AddSubscriptionScreen(
    private val url: String? = null
) : Screen() {
    override fun createView(container: ViewGroup): View =
        AddSubscriptionView(container.context).apply {
            setupView()
            setupViewModel()

            onScreenExiting {
                hideKeyboard()
            }
        }

    private fun AddSubscriptionView.setupView() {
        activity.title = resources.getString(R.string.add_subscription)

        editTextView.setText(url)
        editTextView.setOnEditorActionListener { _, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (event == null || event.action == KeyEvent.ACTION_UP) {
                        viewModel.fetchFeeds(editTextView.text.toString())
                    }
                    true
                }
                else -> false
            }
        }


        adapter.onItemClick = { item ->
            val (_, link) = item
            viewModel.addSubscription(link)
        }

        val text = editTextView.text
        if (text?.isNotEmpty() == true) {
            viewModel.fetchFeeds(text.toString())
        }
        editTextView.focusAndShowKeyboard()
    }

    private fun AddSubscriptionView.setupViewModel() {
        viewModel.uiState.observe(this) {
            swipeRefreshLayout.isRefreshing = it is AddSubscriptionViewModel.UiState.Loading
            when (it) {
                is AddSubscriptionViewModel.UiState.ShowError -> Toast.makeText(
                    context,
                    it.errorMsg,
                    Toast.LENGTH_SHORT
                ).show()
                is AddSubscriptionViewModel.UiState.Loaded -> adapter.items = it.items
            }
        }

        viewModel.goBack.observeEvent(this) {
            Toast.makeText(context, "Subscription added", Toast.LENGTH_SHORT).show()
            goBack()
        }
    }
}