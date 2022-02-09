package com.example.runningapp.util

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper

class RecyclerViewItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener
) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    //    if (viewHolder != null) {
    //        getDefaultUIUtil().onSelected(getForegroundView(viewHolder))
    //    }
    //}

    //override fun onChildDrawOver(
    //    c: Canvas,
    //    recyclerView: RecyclerView,
    //    viewHolder: RecyclerView.ViewHolder?,
    //    dX: Float,
    //    dY: Float,
    //    actionState: Int,
    //    isCurrentlyActive: Boolean
    //) {
    //    getDefaultUIUtil().onDrawOver(
    //        c, recyclerView, viewHolder?.let { getForegroundView(it) }, dX, dY,
    //        actionState, isCurrentlyActive
    //    )
    //}

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(getForegroundView(viewHolder))
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
        getDefaultUIUtil().onDraw(
            c, recyclerView, getForegroundView(viewHolder), dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    private fun getForegroundView(viewHolder: RecyclerView.ViewHolder): View {
        try {
            return (viewHolder as SwipableViewHolder).viewForeground
        } catch (e: ClassCastException) {
            throw ClassCastException(("$viewHolder must be SwipableViewHolder"))
        }
    }
}