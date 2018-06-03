package com.yulocus.eventplayer.presenter

import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.base.MvpPresenter
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.contract.MainContract
import com.yulocus.eventplayer.repository.RepositoryProvider
import com.yulocus.eventplayer.util.RxSchedulers
import io.reactivex.observers.DisposableObserver

class MainPresenter(private val view: MainContract.View) : MvpPresenter(view.getContext()), MainContract.Presenter {

    override fun loadEvents() {
        view.showLoading(true)
        addDisposable(RepositoryProvider.provideEventRepository()
                .loadEvents()
                .compose(RxSchedulers.applyObservableAsync())
                .subscribeWith(object : DisposableObserver<MutableList<Alert>>() {
                    override fun onNext(result: MutableList<Alert>) {
                        view.showEvents(result)
                    }

                    override fun onComplete() {
                        view.showLoading(false)
                    }

                    override fun onError(e: Throwable) {
                        view.showError(context.getString(R.string.api_error_unknown))
                    }
                })
        )
    }
}