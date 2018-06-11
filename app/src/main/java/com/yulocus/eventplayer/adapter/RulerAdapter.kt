package com.yulocus.eventplayer.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.bean.Alert
import kotlinx.android.synthetic.main.layout_ruler_item.view.*
import org.jetbrains.anko.firstChild
import org.jetbrains.anko.forEachChild
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*

class RulerAdapter(private val context: Context): RecyclerView.Adapter<RulerAdapter.ViewHolder>() {

    companion object {
        private const val MINUTE_INTERVAL = 60 // 60 minutes
        private const val TIME_INTERVAL = 3600000.toLong() // 1 hour
    }

    private val alerts: MutableList<Alert> = ArrayList()
    private var rulerCount = 0

    init {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.DATE, -30) // limit 30 days
        rulerCount = ((startTime.timeInMillis - endTime.timeInMillis) / TIME_INTERVAL).toInt() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_ruler_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rulerCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun addEvents(list: MutableList<Alert>) {
        alerts.addAll(list)
        notifyDataSetChanged()
    }

    fun scrollToClosestEvent(recyclerView: RecyclerView, position: Int, currentX: Float) {
        val itemWidth = context.resources.getDimensionPixelSize(R.dimen.height_80)
        val middle = getSmallestOffset(recyclerView, position, currentX)
        val left = itemWidth - getSmallestOffset(recyclerView, position + 1, currentX)
        val right = itemWidth - getSmallestOffset(recyclerView, position - 1, currentX)
        when {
            left > 0f && Math.abs(left) < Math.abs(middle) -> recyclerView.smoothScrollBy(-left.toInt(), 0)
            right > 0 && Math.abs(right) < Math.abs(middle) -> recyclerView.scrollBy(right.toInt(), 0)
            middle == 0f -> {
                when {
                    left > 0f && Math.abs(left) < Math.abs(right) -> recyclerView.smoothScrollBy(-left.toInt(), 0)
                    right > 0f && Math.abs(right) < Math.abs(left) -> recyclerView.smoothScrollBy(right.toInt(), 0)
                    else -> recyclerView.smoothScrollBy(0, 0)
                }
            }
            else -> recyclerView.smoothScrollBy(middle.toInt(), 0)
        }
    }

    private fun getSmallestOffset(recyclerView: RecyclerView, position: Int, currentX: Float): Float {
        var changeOffset = 0f
        var direction = false
        val view = recyclerView.layoutManager.findViewByPosition(position)
        view?.let {
            val container = it.findViewById<LinearLayout>(R.id.ruler_event)
            if(container.childCount > 0) {
                Timber.d("getSmallestOffset(), event count=${container.childCount}")
                container.forEachChild {
                    val offset = if(currentX > it.x) {
                        currentX - it.x
                    } else {
                        it.x - currentX
                    }

                    direction = Math.abs(it.x) > Math.abs(currentX)
                    if(changeOffset == 0f || Math.abs(changeOffset) > Math.abs(offset)) {
                        changeOffset = offset
                    }
                }
            }
        }

        Timber.d("getSmallestOffset(), direction=$direction, changeOffset=$changeOffset")
        return if(direction) changeOffset else -changeOffset
    }

    fun getLiveOffset(): Float {
        val itemWidth = context.resources.getDimensionPixelSize(R.dimen.height_80)
        val liveTime = Calendar.getInstance()
        val liveMinute = liveTime.get(Calendar.MINUTE)
        val dotPadding = context.resources.getDimensionPixelSize(R.dimen.height_10)
        return (itemWidth / MINUTE_INTERVAL * liveMinute - dotPadding / 2).toFloat()
    }

    private fun getEventOffset(alert: Alert): Float {
        val itemWidth = context.resources.getDimensionPixelSize(R.dimen.height_80)
        val eventTime = Calendar.getInstance()
        // set event time
        eventTime.timeInMillis = alert.ts
        val eventMinute = eventTime.get(Calendar.MINUTE)
        val dotPadding = context.resources.getDimensionPixelSize(R.dimen.height_10)
        return (itemWidth - itemWidth / MINUTE_INTERVAL * eventMinute - dotPadding / 2).toFloat()
    }

    private fun getEventDotLayoutParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(
                context.resources.getDimensionPixelSize(R.dimen.height_10),
                context.resources.getDimensionPixelSize(R.dimen.height_10)
        )
        params.gravity = Gravity.TOP and Gravity.CENTER_HORIZONTAL
        return params
    }

    private fun drawLiveDot(itemView: View) {
        with(itemView) {
            val liveDot = LayoutInflater.from(context).inflate(R.layout.layout_live_dot, null)
            liveDot.layoutParams = getEventDotLayoutParams()
            liveDot.x = getLiveOffset()
            liveDot.y = (context.resources.getDimensionPixelSize(R.dimen.height_5)).toFloat()
            ruler_event.addView(liveDot)
        }
    }

    private fun drawEventDot(itemView: View, alert: Alert) {
        with(itemView) {
            val eventDot = LayoutInflater.from(context).inflate(R.layout.layout_event_dot, null)
            eventDot.layoutParams = getEventDotLayoutParams()
            eventDot.x = getEventOffset(alert)
            eventDot.y = (context.resources.getDimensionPixelSize(R.dimen.height_5)).toFloat()
            eventDot.tag = alert
            ruler_event.addView(eventDot)

            Timber.d("eventX=${eventDot.x}")
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            with(itemView) {
                // set item width
                val itemWidth = context.resources.getDimensionPixelSize(R.dimen.height_80)
                layoutParams = RelativeLayout.LayoutParams(itemWidth, itemWidth)

                // set time range of this item
                val startTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                startTime.add(Calendar.HOUR, -position + 1)
                endTime.add(Calendar.HOUR, -position)

                // draw scale
                val df = DecimalFormat("#00")
                val unit = if(startTime.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                val label = "${df.format(startTime.get(Calendar.HOUR_OF_DAY))} $unit"
                ruler_label.text = label
                ruler_label.visibility = View.VISIBLE

                // remove all events first
                ruler_event.removeAllViews()

                // draw live
                if(position == 0) {
                    drawLiveDot(this)
                }

                // draw event
                if(alerts.size > 0) {
                    alerts.forEach {
                        // 1527809697 - 07:34
                        // 1527805971 - 06:32
                        // 1527802365 - 05:32
                        // 1527798758 - 20:32
                        if (it.ts <= startTime.timeInMillis / 1000L && it.ts > endTime.timeInMillis / 1000L) { // in range
                            drawEventDot(this, it)
                            Timber.d("eventPosition=$position")
                        }
                    }
                }
            }
        }
    }
}