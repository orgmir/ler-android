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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.di.viewModels
import app.luisramos.ler.domain.work.UPDATE_WORK_ID
import app.luisramos.ler.domain.work.enqueueFeedSyncWork
import app.luisramos.ler.domain.work.enqueueRefreshFeedsWork
import app.luisramos.ler.ui.event.observeEvent
import app.luisramos.ler.ui.screen.Screen
import app.luisramos.ler.ui.screen.activity
import app.luisramos.ler.ui.screen.onCreateOptionsMenu
import app.luisramos.ler.ui.views.UiState

class FeedListScreen : Screen() {
    override fun createView(container: ViewGroup): View =
        FeedItemsListView(container.context).apply {
            val viewModel: FeedItemsListViewModel = viewModels(context)
            setupView(viewModel)
            setupViewModel(viewModel)

            viewModel.loadData()
        }

    private fun FeedItemsListView.setupView(viewModel: FeedItemsListViewModel) {
        setupMenu(viewModel)

        val swipeActionCallback = SwipeActionsCallback(context, adapter) {
            viewModel.toggleUnread(it)
        }
        val itemTouchHelper = ItemTouchHelper(swipeActionCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.onItemClick = {
            viewModel.tappedItem(it)
        }

        swipeRefreshLayout.apply {
            setOnRefreshListener {
                context.enqueueRefreshFeedsWork()
            }

            WorkManager.getInstance(context)
                .getWorkInfosForUniqueWorkLiveData(UPDATE_WORK_ID)
                .observe(this) {
                    val workInfo = it.firstOrNull()
                    when (workInfo?.state) {
                        WorkInfo.State.RUNNING -> if (!isRefreshing) {
                            isRefreshing = true
                        }
                        else -> if (isRefreshing) {
                            isRefreshing = false
                        }
                    }
                }
        }
    }

    private fun FeedItemsListView.setupMenu(viewModel: FeedItemsListViewModel) {
        onCreateOptionsMenu { menu ->
            activity.menuInflater.inflate(R.menu.menu_main, menu)

            menu.findItem(R.id.menu_notify_me).apply {
                isVisible = viewModel.isNotifyMenuOptionVisible.value ?: false
                isChecked = viewModel.isNotifyMenuOptionChecked.value ?: false
                val iconRes = if (isChecked) {
                    R.drawable.ic_baseline_notifications_active_24
                } else {
                    R.drawable.ic_baseline_notifications_off_24
                }
                icon = ContextCompat.getDrawable(context, iconRes)

                setOnMenuItemClickListener {
                    viewModel.toggleNotifyMe()
                    true
                }
            }

            menu.findItem(R.id.menu_hide_read).apply {
                isChecked = viewModel.showReadFeedItems.value ?: true
                val iconRes = if (isChecked) {
                    R.drawable.ic_baseline_visibility_off_24
                } else {
                    R.drawable.ic_baseline_visibility_24
                }
                icon = ContextCompat.getDrawable(context, iconRes)

                setOnMenuItemClickListener {
                    viewModel.toggleUnreadFilter()
                    true
                }
            }
            menu.findItem(R.id.menu_delete_feed).apply {
                isVisible = viewModel.isDeleteMenuOptionVisible.value ?: false
                setOnMenuItemClickListener {
                    viewModel.tapDeleteFeed()
                    true
                }
            }

            menu.findItem(R.id.menu_mark_as_read).setOnMenuItemClickListener {
                viewModel.markAllAsRead()
                true
            }

            menu.findItem(R.id.menu_refresh).setOnMenuItemClickListener {
                context.enqueueRefreshFeedsWork()
                true
            }

            menu.findItem(R.id.menu_settings).setOnMenuItemClickListener {
                viewModel.onSettingsMenuClicked()
                true
            }
        }
    }

    private fun FeedItemsListView.setupViewModel(viewModel: FeedItemsListViewModel) {
        viewModel.uiState.observe(this) {
            emptyView.isVisible = (it as? UiState.Success)?.data.isNullOrEmpty()
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
        viewModel.shouldUpdateMenuOptions.observe(this) {
            (context as? Activity)?.apply { post { invalidateOptionsMenu() } }
        }
        viewModel.updateListPosition.observe(this) {
            adapter.notifyItemChanged(it)
        }
        viewModel.showDeleteConfirmation.observeEvent(this) {
            showDeleteConfirmationDialog(it, viewModel)
        }
    }

    private fun FeedItemsListView.showDeleteConfirmationDialog(
        title: String,
        viewModel: FeedItemsListViewModel
    ) {
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