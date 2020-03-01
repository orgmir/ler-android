package app.luisramos.thecollector.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.luisramos.thecollector.R
import app.luisramos.thecollector.ui.base.BaseFragment
import app.luisramos.thecollector.ui.event.observeEvent
import app.luisramos.thecollector.ui.main.MainViewModel.UiState
import app.luisramos.thecollector.ui.subscription.AddSubscriptionFragment
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FeedItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.onItemClick = {
            viewModel.tappedItem(it)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
//            viewModel.loadData()
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it == UiState.Loading
            emptyView.visibility = (it as? UiState.Success)?.data.isNullOrEmpty().toggleGone()
            when (it) {
                is UiState.Error ->
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong: ${it.msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                is UiState.Success -> adapter.items = it.data
            }
        })
        viewModel.updateListPosition.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(it)
        })
        viewModel.goToExternalBrowser.observeEvent(viewLifecycleOwner) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationIcon(R.drawable.ic_menu_white_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.add_subscription -> {
            showAddSubscriptionDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showAddSubscriptionDialog() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, AddSubscriptionFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}

fun Boolean.toggleGone(): Int = if (this) View.VISIBLE else View.GONE