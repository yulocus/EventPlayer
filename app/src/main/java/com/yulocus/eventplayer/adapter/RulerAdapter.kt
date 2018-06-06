package com.yulocus.eventplayer.adapter

import android.content.Context
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
import java.text.DecimalFormat
import java.util.*

class RulerAdapter(private val context: Context): RecyclerView.Adapter<RulerAdapter.ViewHolder>() {

    companion object {
        private const val TIME_INTERVAL = 3600000.toLong() // 1 hour
    }

    private val alerts: MutableList<Alert> = ArrayList()
    private var rulerCount = 0

    init {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.DATE, -30)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            with(itemView) {
                // set item width
                layoutParams = RelativeLayout.LayoutParams(context.resources.getDimensionPixelSize(R.dimen.height_80), context.resources.getDimensionPixelSize(R.dimen.height_80))

                // set time range of this item
                val startTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                startTime.add(Calendar.HOUR, -position)
                endTime.add(Calendar.HOUR, -position-1)

                // draw scale
                when(position) {
                    0 -> {
                        ruler_label.text = context.resources.getString(R.string.live)
                        ruler_label.visibility = View.VISIBLE
                    }
                    else -> {
                        val df = DecimalFormat("#00")
                        val unit = if(startTime.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                        val label = "${df.format(startTime.get(Calendar.HOUR_OF_DAY))} $unit"
                        ruler_label.text = label
                        ruler_label.visibility = View.VISIBLE
                    }
                }

                // build event
                if(alerts.size > 0) {
                    ruler_event.removeAllViews()
                    alerts.forEach {
                        if (it.ts <= startTime.timeInMillis / 1000L && it.ts > endTime.timeInMillis / 1000L) { // in range
                            val eventTime = Calendar.getInstance()
                            eventTime.timeInMillis = it.ts
                            val eventMinute = eventTime.get(Calendar.MINUTE)
                            val eventX = context.resources.getDimensionPixelSize(R.dimen.height_80) / 60 * eventMinute
                            val eventY = context.resources.getDimensionPixelSize(R.dimen.height_5)
                            val eventDot = LayoutInflater.from(context).inflate(R.layout.layout_event_dot, null)
                            val params = LinearLayout.LayoutParams(
                                    context.resources.getDimensionPixelSize(R.dimen.height_10),
                                    context.resources.getDimensionPixelSize(R.dimen.height_10)
                            )
                            params.gravity = Gravity.TOP and Gravity.CENTER_HORIZONTAL
                            eventDot.layoutParams = params
                            eventDot.x = eventX.toFloat()
                            eventDot.y = eventY.toFloat()
                            eventDot.tag = it
                            ruler_event.addView(eventDot)
                        }
                    }
                }
            }
        }
    }
}