package com.ya.function.net.http.subscriber.impl

import com.ya.function.net.http.exception.ApiException
import com.ya.function.net.http.subscriber.BaseSubscriber

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
open class ApiSubscriber<T>: BaseSubscriber<T>() {

    final override fun onError(t: Throwable?) {
        super.onError(t)
        t?.let {
            if(t is ApiException){
                onApiError(t)
            }else{
                onApiError(ApiException(-1,t.message))
            }
        }
    }

    final override fun onNext(t: T) {
        super.onNext(t)
        onApiSuccess(t)
    }

//    final override fun onComplete() {
//        super.onComplete()
//    }

    protected open fun onApiSuccess(t: T){

    }

    protected open fun onApiError(exception: ApiException){

    }
}