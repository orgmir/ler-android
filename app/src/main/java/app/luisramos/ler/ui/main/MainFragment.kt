package app.luisramos.ler.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R
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
//        val itemTouchHelper = ItemTouchHelper(SwipeActionsCallback(requireContext(), adapter))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.onItemClick = {
            viewModel.tappedItem(it)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            // TODO reload feed data
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
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationIcon(R.drawable.ic_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        val showRead = viewModel.showReadFeedItems.value ?: false
        menu.findItem(R.id.hide_read)?.isChecked = !showRead
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.add_subscription -> {
            showAddSubscriptionDialog()
            true
        }
        R.id.hide_read -> {
            viewModel.toggleUnreadFilter()
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

class SwipeActionsCallback(
    context: Context,
    val adapter: FeedItemAdapter
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {

    val markReadIcon = ContextCompat.getDrawable(context, R.drawable.ic_check)
    val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.very_light_grey)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // do something
        adapter.notifyItemChanged(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        markReadIcon ?: return
        viewHolder.itemView.run {
            val iconMargin = (height - markReadIcon.intrinsicHeight) / 2
            val iconTop = top + (height - markReadIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + markReadIcon.intrinsicHeight

            if (dX < 0) {
                val iconLeft = right - iconMargin - markReadIcon.intrinsicWidth
                val iconRight = right - iconMargin
                markReadIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                c.drawRect(
                    right + dX,
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    paint
                )
                markReadIcon.draw(c)
            }
        }
    }

}