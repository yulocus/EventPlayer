package com.yulocus.eventplayer.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.adapter.RulerAdapter
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.util.RulerItemDecoration
import org.jetbrains.anko.forEachChild
import timber.log.Timber

class RulerRecyclerView(context: Context, attrs: AttributeSet?): RecyclerView(context, attrs) {

    private val adapter by lazy { RulerAdapter(context) }
    private var callback: EventCallback? = null

    fun initRuler(context: Context) {
        val itemWidth = context.resources.getDimensionPixelSize(R.dimen.height_80)
        val controllerWidth = itemWidth * 6 // show 6 hours
        val controllerHeight = context.resources.getDimensionPixelSize(R.dimen.height_80)

        // horizontal layout manager
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        manager.reverseLayout = true
        layoutManager = manager

        // set adapter
        setAdapter(adapter)
        addItemDecoration(RulerItemDecoration())
        // draw and scroll to live dot
        adapter.notifyItemChanged(0)
        adapter.scrollToLive(manager)

        // build ruler size
        val params = RelativeLayout.LayoutParams(controllerWidth, controllerHeight)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams = params

        removeOnScrollListener(scrollListener)
        addOnScrollListener(scrollListener)
    }

    fun updateEvents(list: MutableList<Alert>) {
        adapter.addEvents(list)
    }

    fun setCallback(callback: EventCallback) {
        this.callback = callback
    }

    private val scrollListener = object: OnScrollListener() {
        private var scrollX = 0L
        private var eventX = 0f
        private var currentPosition = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    recyclerView?.let { checkEvent(it) }
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> callback?.stopVideo()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            recyclerView?.let {
                if(dx > 0) { // to left
                    scrollX -= Math.abs(dx)
                } else if(dx < 0) { // to right
                    scrollX += Math.abs(dx)
                }

                // calculate event position
                val offset = context.resources.getDimensionPixelSize(R.dimen.height_80) - adapter.getLiveOffset() // live offset
                val position = ((scrollX) / context.resources.getDimensionPixelSize(R.dimen.height_80)).toInt()
                if(position != currentPosition) {
                    eventX = 0f
                    currentPosition = position
                } else {
                    eventX += dx
                }
            }
        }

        private fun checkEvent(recyclerView: RecyclerView) {
            val itemWidth = context.resources.getDimensionPixelOffset(R.dimen.height_80)
            val view = recyclerView.layoutManager.findViewByPosition(currentPosition+1)
            view?.let {
                val container = it.findViewById<LinearLayout>(R.id.ruler_event)
                if(container.childCount > 0) {
                    container.forEachChild {
                        val currentX = itemWidth - Math.abs(eventX)
                        if(it.x >= currentX - 10 && it.x <= currentX + 10) { // in range
                            Timber.d("find currentX=$currentX, eventX=${it.x}")
                            it.tag?.let { callback?.setEvent(it as Alert) }
                        }
                    }
                }
            }
        }
    }

    interface EventCallback {
        fun setEvent(alert: Alert)
        fun stopVideo()
    }
}