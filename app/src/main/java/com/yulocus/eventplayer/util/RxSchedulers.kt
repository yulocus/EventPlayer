package com.yulocus.eventplayer.util

import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Locus on 10/30/17.
 */
object RxSchedulers {
    fun <T> applyObservableAsync(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyObservableCompute(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyObservableMainThread(): ObservableTransformer<T, T> =
            ObservableTransformer { observable -> observable.observeOn(AndroidSchedulers.mainThread()) }

    fun <T> applyFlowableMainThread(): FlowableTransformer<T, T> =
            FlowableTransformer { flowable -> flowable.observeOn(AndroidSchedulers.mainThread()) }

    fun <T> applyFlowableAsysnc(): FlowableTransformer<T, T> =
            FlowableTransformer { flowable -> flowable.observeOn(AndroidSchedulers.mainThread()) }

    fun <T> applyFlowableCompute(): FlowableTransformer<T, T> =
            FlowableTransformer { flowable -> flowable.observeOn(AndroidSchedulers.mainThread()) }
}