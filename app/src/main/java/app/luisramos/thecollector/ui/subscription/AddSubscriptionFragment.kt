package app.luisramos.thecollector.ui.subscription

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.luisramos.thecollector.R
import app.luisramos.thecollector.ui.base.BaseFragment
import app.luisramos.thecollector.ui.event.observeEvent
import app.luisramos.thecollector.ui.subscription.AddSubscriptionViewModel.UiState
import kotlinx.android.synthetic.main.fragment_add_subscription.*
import kotlinx.android.synthetic.main.fragment_main.recyclerView

class AddSubscriptionFragment : BaseFragment() {

    companion object {
        fun newInstance(url: String? = null) = AddSubscriptionFragment().apply {
            url?.let { arguments = bundleOf("url" to it) }
        }
    }

    private val viewModel: AddSubscriptionViewModel by viewModels()
    private val adapter = StackedLabelsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_add_subscription, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        editText.setText(arguments?.getString("url"))
        editText.setOnEditorActionListener { _, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (event == null || event.action == KeyEvent.ACTION_UP) {
                        viewModel.fetchFeeds(editText.text.toString())
                    }
                    true
                }
                else -> false
            }
        }
        editText.focusAndShowKeyboard()

        adapter.onItemClick = { item ->
            val (_, link) = item
            viewModel.addSubscription(link)
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it is UiState.Loading
            when (it) {
                is UiState.ShowError -> Toast.makeText(
                    requireContext(),
                    it.errorMsg,
                    Toast.LENGTH_SHORT
                ).show()
                is UiState.Loaded -> adapter.items = it.items
            }
        })

        viewModel.goBack.observeEvent(this) {
            Toast.makeText(requireContext(), "Subscription added", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }

        val text = editText.text
        if (text?.isNotEmpty() == true) {
            viewModel.fetchFeeds(text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(editText)
    }
}

fun Fragment.hideKeyboard(textView: TextView) {
    requireContext().getSystemService(InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(textView.windowToken, 0)
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}
