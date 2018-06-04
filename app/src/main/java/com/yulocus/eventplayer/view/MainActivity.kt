package com.yulocus.eventplayer.view

import android.content.Context
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.base.MvpActivity
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.contract.MainContract
import com.yulocus.eventplayer.presenter.MainPresenter
import com.yulocus.eventplayer.util.StatusBarUtils
import com.yulocus.eventplayer.widget.RulerRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    override fun bindLayoutId(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter = MainPresenter(this)

    override fun setupStatusBar() = StatusBarUtils.hideStatusBar(this)

    override fun getContext(): Context = this

    override fun initView() {
        // toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        recycler_view.setCallback(object: RulerRecyclerView.RulerResultCallback{
            override fun setResult(result: Int) {
                Timber.d("result=$result")
            }
        })

        // load events
//        presenter.loadEvents()
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
//        adapter.addEvents(list)
    }

}
