package com.yulocus.eventplayer.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.adapter.EventsAdapter
import com.yulocus.eventplayer.base.MvpActivity
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.contract.MainContract
import com.yulocus.eventplayer.presenter.MainPresenter
import com.yulocus.eventplayer.util.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    private val adapter by lazy {
        EventsAdapter(object: EventsAdapter.ActionListener {
            override fun onItemClickListener(alert: Alert) {
                Timber.d(alert.title)
            }
        })
    }

    override fun bindLayoutId(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter = MainPresenter(this)

    override fun setupStatusBar() = StatusBarUtils.hideStatusBar(this)

    override fun getContext(): Context = this

    override fun initView() {
        // toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = adapter

        // load events
        presenter.loadEvents()
    }

    override fun initListener() {
    }

    override fun showLoading(isLoading: Boolean) {
        Timber.d("showLoading(), isLoading=$isLoading")
    }

    override fun showError(message: String) {
        Timber.e("showError(), message=$message")
    }

    override fun showEvents(list: MutableList<Alert>) {
        adapter.addEvents(list)
    }

}
