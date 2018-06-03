package com.yulocus.eventplayer.util

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.yulocus.eventplayer.R
import kotlinx.android.synthetic.main.layout_global_dialog.view.*

object DialogUtils {
    fun showCTADialog(activity: Activity,
                      title: String?,
                      desc: String?,
                      buttonText: String,
                      actionListener: OnDialogListener? = null,
                      dismissListener: OnDialogListener? = null) {

        val builder = AlertDialog.Builder(activity, R.style.Theme_PopupDialog)
        val dialog = builder.create()
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_global_dialog, null)
        dialog.run {
            setView(view)
            setCanceledOnTouchOutside(false)

            // setup layout
            view.text_title.text = title
            view.text_desc.text = desc

            // cancel button
            view.button_cancel.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                dismissListener?.onCancelButton()
            }

            // action button
            actionListener?.let {
                view.button_action.text = buttonText
                view.button_action.setOnClickListener {
                    if (isShowing)
                        dismiss()
                    actionListener.onActionButton()
                }
                view.button_action.visibility = View.VISIBLE
            } ?: run {
                view.button_action.visibility = View.GONE
            }

            val layoutParams = window.attributes
            layoutParams.gravity = Gravity.CENTER

            if (isShowing) dismiss()
            show()
        }
    }
}

interface OnDialogListener {
    fun onActionButton()
    fun onCancelButton()
}