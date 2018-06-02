package com.yulocus.eventplayer.base

import android.os.Bundle
import android.support.annotation.LayoutRes

abstract class MvpActivity<P: MvpPresenter> : BaseActivity(), MvpView {

    protected lateinit var presenter: P

    @LayoutRes
    abstract override fun bindLayoutId(): Int
    abstract override fun initView()
    abstract override fun initListener()
    abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = createPresenter()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }
}