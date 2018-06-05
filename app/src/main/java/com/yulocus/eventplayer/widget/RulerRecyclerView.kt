package com.yulocus.eventplayer.widget

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.adapter.RulerAdapter
import com.yulocus.eventplayer.bean.Alert
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
        layoutManager = manager

        // set adapter
        setAdapter(adapter)

        // screen width
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val screenWidth = size.x
        val screenHeight = size.y

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
                    val position = (scrollX / context.resources.getDimensionPixelSize(R.dimen.height_80)).toInt()
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
                                if(it.x + PADDING == eventX || it.x - PADDING == eventX) {
                                    callback?.setResult(it.tag as Alert)
                                }
                            }
                        }
                    }
                }
            }
        })

//        manager.scrollToPositionWithOffset((adapter.itemCount + 1) / 2, width / 2)
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