package app.luisramos.ler.ui.feeds

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.enqueueFeedSyncWork
import app.luisramos.ler.ui.event.observeEvent
import app.luisramos.ler.ui.navigation.Screen
import app.luisramos.ler.ui.navigation.activity
import app.luisramos.ler.ui.navigation.goTo
import app.luisramos.ler.ui.navigation.onCreateOptionsMenu
import app.luisramos.ler.ui.subscription.AddSubscriptionScreen
import app.luisramos.ler.ui.views.UiState
import app.luisramos.ler.ui.views.toggleGone

class FeedListScreen : Screen() {
    override fun createView(container: ViewGroup): View =
        FeedItemsListView(container.context).apply {

            onCreateOptionsMenu { menu ->
                activity.menuInflater.inflate(R.menu.menu_main, menu)

                menu.findItem(R.id.menu_hide_read).apply {
                    isChecked = viewModel.showReadFeedItems.value ?: false
                    setOnMenuItemClickListener {
                        viewModel.toggleUnreadFilter()
                        true
                    }
                }
                menu.findItem(R.id.menu_delete_feed).apply {
                    isVisible = viewModel.isDeleteMenuOptionVisible.value?.not() ?: false
                    setOnMenuItemClickListener {
                        viewModel.tapDeleteFeed()
                        true
                    }
                }
                menu.findItem(R.id.menu_add_subscription).setOnMenuItemClickListener {
                    showAddSubscriptionDialog()
                    true
                }

                menu.findItem(R.id.menu_mark_as_read).setOnMenuItemClickListener {
                    viewModel.markAllAsRead()
                    true
                }

                menu.findItem(R.id.menu_refresh).setOnMenuItemClickListener {
                    context.enqueueFeedSyncWork(refreshData = true)
                    true
                }
            }

            setupView()
            setupViewModel()
        }

    private fun FeedItemsListView.setupView() {
        val swipeActionCallback = SwipeActionsCallback(context, adapter) {
            viewModel.toggleUnread(it)
        }
        val itemTouchHelper = ItemTouchHelper(swipeActionCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.onItemClick = {
            viewModel.tappedItem(it)
        }
    }

    private fun FeedItemsListView.setupViewModel() {
        viewModel.uiState.observe(this) {
            emptyView.visibility =
                (it as? UiState.Success)?.data.isNullOrEmpty()
                    .toggleGone()
            when (it) {
                is UiState.Loading -> { /* No need to do anything, loading is in toolbar */
                }
                is UiState.Error ->
                    Toast.makeText(
                        context,
                        "Something went wrong: ${it.msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                is UiState.Success -> adapter.submitList(it.data)
            }
        }
        viewModel.showReadFeedItems.observe(this) {
            (context as? Activity)?.invalidateOptionsMenu()
        }
        viewModel.isDeleteMenuOptionVisible.observe(this) {
            (context as? Activity)?.invalidateOptionsMenu()
        }
        viewModel.updateListPosition.observe(this) {
            adapter.notifyItemChanged(it)
        }
        viewModel.goToExternalBrowser.observeEvent(this) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            ContextCompat.startActivity(context, intent, null)
        }
        viewModel.showDeleteConfirmation.observeEvent(this) {
            showDeleteConfirmationDialog(it)
        }
    }

    private fun View.showAddSubscriptionDialog() {
        goTo(AddSubscriptionScreen())
    }

    private fun FeedItemsListView.showDeleteConfirmationDialog(title: String) {
        val alertDialog = AlertDialog.Builder(context)
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