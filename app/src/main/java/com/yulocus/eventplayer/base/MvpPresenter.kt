package com.yulocus.eventplayer.base

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class MvpPresenter(val context: Context): BasePresenter {

    private var compositeDisposable: CompositeDisposable? = null

    protected fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    private fun clearDisposable() {
        if(compositeDisposable != null) {
            compositeDisposable?.clear()
        }
    }

    override fun subscribe() {

    }

    override fun unsubscribe(){
        clearDisposable()
    }
}
