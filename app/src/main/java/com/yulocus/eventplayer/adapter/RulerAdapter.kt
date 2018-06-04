package com.yulocus.eventplayer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yulocus.eventplayer.R
import kotlinx.android.synthetic.main.layout_ruler_item.view.*
import java.util.*

class RulerAdapter(private val context: Context): RecyclerView.Adapter<RulerAdapter.ViewHolder>() {

    companion object {
        private const val TIME_INTERVAL = 60000.toLong() // 1 minute
    }

    private var rulerCount = 0

    init {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.DATE, -1)
        rulerCount = ((startTime.timeInMillis - endTime.timeInMillis) / TIME_INTERVAL).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_ruler_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rulerCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            with(itemView) {
                when {
                    position == 0 -> {
                        ruler_label.text = "Live"
                        ruler_label.visibility = View.VISIBLE
                    }
                    position > 1 -> {
                        ruler_label.text = "Hour"
                        ruler_label.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}