package com.yulocus.eventplayer.widget

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.WindowManager
import com.yulocus.eventplayer.adapter.RulerAdapter
import com.yulocus.eventplayer.bean.Alert

class RulerRecyclerView(context: Context, attrs: AttributeSet?): RecyclerView(context, attrs) {

    companion object {
        private const val INTERVAL = 60
    }

    private val adapter by lazy { RulerAdapter(context) }
    private var callback: RulerResultCallback? = null

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
        val width = size.x

        addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollX += dx
                callback?.setResult(scrollX / INTERVAL)
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
        fun setResult(result: Int)
    }
}