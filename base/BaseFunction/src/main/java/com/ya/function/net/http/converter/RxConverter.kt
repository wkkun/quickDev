package com.ya.function.net.http.converter

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */

/**
 * Flowable 转换为 Observable
 */
fun <T>Flowable<T>.toObservable(): Observable<T>{
    return toObservable()
}

/**
 * Observable 转换为  Flowable
 */
fun <T>Observable<T>.toBufferFlowable(): Flowable<T>{
    return toFlowable(BackpressureStrategy.BUFFER)
}

fun <T> Flowable<T>.composeNetThread2UI(): Flowable<T>{
    return this.compose {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
