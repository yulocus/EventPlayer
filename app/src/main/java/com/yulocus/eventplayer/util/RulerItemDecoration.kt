package com.yulocus.eventplayer.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class RulerItemDecoration(): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        parent?.let {
            val totalWidth = it.width
            // first item
            if(it.getChildAdapterPosition(view) == 0) {
                var firstPadding = totalWidth / 2
                firstPadding = Math.max(0, firstPadding)
                outRect?.set(0, 0, firstPadding, 0)
            }

            // last item
            if(it.getChildAdapterPosition(view) == it.adapter.itemCount - 1 && it.adapter.itemCount > 1) {
                var lastPadding = totalWidth / 2
                lastPadding = Math.max(0, lastPadding)
                outRect?.set(lastPadding, 0, 0, 0)
            }
        }
    }
}