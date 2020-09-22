package app.luisramos.ler.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.luisramos.ler.R
import app.luisramos.ler.UPDATE_WORK_ID
import app.luisramos.ler.enqueueFeedSyncWork
import app.luisramos.ler.ui.base.BaseFragment
import app.luisramos.ler.ui.event.observeEvent
import app.luisramos.ler.ui.main.MainViewModel.UiState
import app.luisramos.ler.ui.subscription.AddSubscriptionFragment
import kotlinx.android.synthetic.main.fragment_main.*

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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val swipeActionCallback = SwipeActionsCallback(requireContext(), adapter) {
            viewModel.toggleUnread(it)
        }
        val itemTouchHelper = ItemTouchHelper(swipeActionCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.onItemClick = {
            viewModel.tappedItem(it)
        }

        swipeRefreshLayout.setOnRefreshListener {
            requireContext().enqueueFeedSyncWork()
        }

        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(UPDATE_WORK_ID)
            .observe(viewLifecycleOwner, Observer {
                val workInfo = it.firstOrNull()
                when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> if (!swipeRefreshLayout.isRefreshing) {
                        swipeRefreshLayout.isRefreshing = true
                    }
                    else -> if (swipeRefreshLayout.isRefreshing) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            })

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
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
        viewModel.showReadFeedItems.observe(viewLifecycleOwner, Observer {
            activity?.invalidateOptionsMenu()
        })
        viewModel.updateListPosition.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(it)
        })
        viewModel.goToExternalBrowser.observeEvent(viewLifecycleOwner) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
        viewModel.isDeleteMenuOptionVisible.observe(viewLifecycleOwner, Observer {
            activity?.invalidateOptionsMenu()
        })
        viewModel.showDeleteConfirmation.observeEvent(viewLifecycleOwner) {
            showDeleteConfirmationDialog(it)
        }
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationIcon(R.drawable.ic_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        val showRead = viewModel.showReadFeedItems.value ?: false
        menu.findItem(R.id.menu_hide_read)?.isChecked = !showRead

        val showDelete = viewModel.isDeleteMenuOptionVisible.value ?: false
        menu.findItem(R.id.menu_delete_feed).isVisible = showDelete
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_add_subscription -> {
            showAddSubscriptionDialog()
            true
        }
        R.id.menu_hide_read -> {
            viewModel.toggleUnreadFilter()
            true
        }
        R.id.menu_mark_as_read -> {
            viewModel.markAllAsRead()
            true
        }
        R.id.menu_delete_feed -> {
            viewModel.tapDeleteFeed()
            true
        }
        R.id.menu_refresh -> {
            requireContext().enqueueFeedSyncWork()
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

    private fun showDeleteConfirmationDialog(title: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete $title?")
            .setMessage(R.string.delete_confirmation_msg)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteSelectedFeed()
            }
            .show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton?.setBackgroundResource(R.color.delete_button_background)
        val buttonColor =
            ResourcesCompat.getColor(resources, R.color.delete_button_text_color, null)
        positiveButton?.setTextColor(buttonColor)
    }
}

fun Boolean.toggleGone(): Int = if (this) View.VISIBLE else View.GONE

