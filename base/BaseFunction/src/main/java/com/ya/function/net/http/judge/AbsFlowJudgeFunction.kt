package com.ya.function.net.http.judge

import io.reactivex.*
import io.reactivex.functions.Function

/**
 *author: Bai
 *Date:2019/11/6
 *Description
 */
abstract class AbsFlowJudgeFunction<T,R>: Function<T,Flowable<R>> {

    final override fun apply(t: T): Flowable<R> {
        return Flowable.create({ emitter ->

            if(checkSuccess(t)){

                emitter.onNext(convert(t))

                emitter.onComplete()

            }else{
                emitter.onError(generateDefaultException(t))
            }
        }, BackpressureStrategy.BUFFER)
    }

    abstract fun checkSuccess(data: T): Boolean

    abstract fun generateDefaultException(data: T): Throwable

    abstract fun convert(data: T): R
}