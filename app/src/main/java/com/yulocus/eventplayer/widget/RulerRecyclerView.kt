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
    private var scrollX = 0L
    private var eventX = 0f
    private var currentPosition = 0

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
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            recyclerView?.let {
                if(dx >= 0) { // to left
                    scrollX -= Math.abs(dx)
                } else { // to right
                    scrollX += Math.abs(dx)
                }

                // calculate event position
                val offset = context.resources.getDimensionPixelSize(R.dimen.height_80) - adapter.getLiveOffset() // live offset
                val position = ((scrollX + offset) / context.resources.getDimensionPixelSize(R.dimen.height_80)).toInt()
                if(position != currentPosition) {
                    eventX = 0f
                    currentPosition = position
                } else {
                    eventX += dx
                }
                val dotPadding = context.resources.getDimensionPixelSize(R.dimen.height_15)
                val view = it.layoutManager.findViewByPosition(position)
                view?.let {
                    val container = it.findViewById<LinearLayout>(R.id.ruler_event)
                    if(container.childCount > 0) {
                        container.forEachChild {
                            val point = Math.abs(eventX) + dotPadding
                            if(it.x == point) { // in range
                                Timber.d("find event point=$point, x=${it.x}")
                                it.tag?.let { callback?.setEvent(it as Alert) }
                            }
                        }
                    }
                }
            }
        }
    }

    interface EventCallback {
        fun setEvent(alert: Alert)
    }
}