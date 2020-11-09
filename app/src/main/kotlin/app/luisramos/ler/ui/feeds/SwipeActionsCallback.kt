package app.luisramos.ler.ui.feeds

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R

class SwipeActionsCallback(
    context: Context,
    val adapter: FeedItemAdapter,
    val onItemSwipedCallback: (Int) -> Unit
) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.START
    ) {

    private val markReadIcon = ContextCompat.getDrawable(
        context,
        R.drawable.ic_check
    )
    private val paint = Paint().apply {
        color = ContextCompat.getColor(
            context,
            R.color.swipe_action_background
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        onItemSwipedCallback(position)
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