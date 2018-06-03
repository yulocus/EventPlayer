package com.yulocus.eventplayer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.bean.Alert
import kotlinx.android.synthetic.main.layout_tick_item.view.*
import java.util.*

class EventsAdapter(private val listener: EventsAdapter.ActionListener?)
    : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    enum class Tick { MINUTE, TEN_MINUTE, ONE_HOUR, ALERT, LIVE }

    private val alerts: MutableList<Alert> = ArrayList()
    private val scales: MutableList<Tick> = ArrayList()

    interface ActionListener {
        fun onItemClickListener(alert: Alert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tick_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, listener)
    }

    override fun getItemCount(): Int = alerts.size

    fun addEvents(list: MutableList<Alert>) {
        val nowTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        var tick = 0
        endTime.add(Calendar.DAY_OF_WEEK, -3) // 3 days ago
        while (nowTime.timeInMillis > endTime.timeInMillis) {
            when {
                tick == 0 -> scales.add(Tick.LIVE)
                tick % 60 == 0 -> scales.add(Tick.ONE_HOUR)
                tick % 10 == 0 -> scales.add(Tick.TEN_MINUTE)
                else -> scales.add(Tick.MINUTE)
            }
            nowTime.add(Calendar.MINUTE, -1) // 1 minute interval
            tick++
        }
        alerts.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, listener: ActionListener?) {
            with(itemView) {
                val params = time_tick.layoutParams
                when(scales[position]) {
                    Tick.LIVE -> {
                        params.height = context.resources.getDimensionPixelSize(R.dimen.height_40)
                        time_tick.layoutParams = params
                        time_tick.visibility = View.VISIBLE
                    }
                    Tick.ONE_HOUR -> {
                        params.height = context.resources.getDimensionPixelSize(R.dimen.height_30)
                        time_tick.layoutParams = params
                        time_tick.visibility = View.VISIBLE
                    }
                    Tick.TEN_MINUTE -> {
                        params.height = context.resources.getDimensionPixelSize(R.dimen.height_20)
                        time_tick.layoutParams = params
                        time_tick.visibility = View.VISIBLE
                    }
                    else -> {
                        time_tick.visibility = View.GONE
                    }
                }
            }
        }
    }
}