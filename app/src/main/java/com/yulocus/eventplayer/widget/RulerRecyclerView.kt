package com.yulocus.eventplayer.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.adapter.RulerAdapter
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.util.RulerItemDecoration
import org.jetbrains.anko.forEachChild
import timber.log.Timber

class RulerRecyclerView(context: Context, attrs: AttributeSet?): RecyclerView(context, attrs) {

    companion object {
        private const val INTERVAL = 60
        private const val PADDING = 5
    }

    private val adapter by lazy { RulerAdapter(context) }
    private var callback: RulerResultCallback? = null
    private var scrollX = 0L
    private var eventX = 0f
    private var currentPosition = 0

    fun initRuler(context: Context) {

        // horizontal layout manager
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        manager.reverseLayout = true
        layoutManager = manager

        // set adapter
        setAdapter(adapter)

        // screen width
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        addItemDecoration(RulerItemDecoration())

        // build ruler size
        val params = RelativeLayout.LayoutParams(context.resources.getDimensionPixelSize(R.dimen.height_80) * 6, context.resources.getDimensionPixelSize(R.dimen.height_80))
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams = params

        addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView?.let {
                    scrollX += dx
                    // calculate event position
                    val position = (Math.abs(scrollX) / context.resources.getDimensionPixelSize(R.dimen.height_80)).toInt()
                    if(position != currentPosition) {
                        eventX = 0f
                        currentPosition = position
                    } else {
                        eventX += dx
                    }
                    Timber.d("eventX=$eventX")
                    val view = it.layoutManager.findViewByPosition(position)
                    view?.let {
                        val container = findViewById<LinearLayout>(R.id.ruler_event)
                        if(container.childCount > 0) {
                            container.forEachChild {
                                if(Math.abs(it.x + PADDING) == Math.abs(eventX) || Math.abs(it.x - PADDING) == Math.abs(eventX)) {
                                    callback?.setResult(it.tag as Alert)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    fun updateEvents(list: MutableList<Alert>) {
        adapter.addEvents(list)
    }

    fun setCallback(callback: RulerResultCallback) {
        this.callback = callback
    }

    interface RulerResultCallback {
        fun setResult(alert: Alert)
    }
}