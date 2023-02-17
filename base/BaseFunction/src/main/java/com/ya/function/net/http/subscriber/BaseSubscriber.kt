package com.ya.function.net.http.subscriber

import io.reactivex.subscribers.DisposableSubscriber

/**
 *author: Bai
 *Date:2019/11/5
 *Description
 */
abstract class BaseSubscriber<T> : DisposableSubscriber<T>() {

    final override fun onStart() {
        // 这个方法不要重写，里面处理了背压相关策略
        super.onStart()
    }

    override fun onNext(t: T) {

    }

    override fun onError(t: Throwable?) {

    }

    override fun onComplete() {

    }
}