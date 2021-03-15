package app.luisramos.ler.ui.subscription

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.di.viewModels
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
            val viewModel: AddSubscriptionViewModel by viewModels()

            setupView(viewModel)
            setupViewModel(viewModel)

            onScreenExiting {
                viewModel.resetState()
                hideKeyboard()
            }
        }

    private fun AddSubscriptionView.setupView(viewModel: AddSubscriptionViewModel) {
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
        imageButton.setOnClickListener {
            viewModel.fetchFeeds(editTextView.text.toString())
        }


        adapter.onItemClick = { position ->
            viewModel.onItemClicked(position)
        }

        val text = editTextView.text
        if (text?.isNotEmpty() == true) {
            viewModel.fetchFeeds(text.toString())
        }
        editTextView.focusAndShowKeyboard()
    }

    private fun AddSubscriptionView.setupViewModel(viewModel: AddSubscriptionViewModel) {
        viewModel.uiState.observe(this) {
            swipeRefreshLayout.isRefreshing = it is AddSubscriptionViewModel.UiState.Loading
            emptyTextView.isVisible = it is AddSubscriptionViewModel.UiState.Empty
            when (it) {
                is AddSubscriptionViewModel.UiState.ShowError -> showToast(it.message)
                is AddSubscriptionViewModel.UiState.Loaded -> adapter.submitList(it.items)
                is AddSubscriptionViewModel.UiState.Empty -> emptyTextView.setText(it.message)
                else -> {
                }
            }
        }

        viewModel.goBack.observeEvent(this) {
            showToast(R.string.add_subscription_success)
            goBack()
        }
    }

    private fun AddSubscriptionView.showToast(textRes: Int) {
        val text = resources.getString(textRes)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}