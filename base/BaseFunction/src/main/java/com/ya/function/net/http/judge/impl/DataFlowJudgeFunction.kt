package com.ya.function.net.http.judge.impl

import com.ya.function.net.http.exception.ApiException
import com.ya.function.net.http.judge.AbsFlowJudgeFunction
import com.ya.function.net.http.model.ApiDataModel

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
open class DataFlowJudgeFunction<T> : AbsFlowJudgeFunction<ApiDataModel<T>, T?>() {

    override fun checkSuccess(data: ApiDataModel<T>): Boolean {
        return data.code == 0
    }

    override fun generateDefaultException(data: ApiDataModel<T>): Throwable {
        return ApiException(data.code,data.message)
    }

    override fun convert(data: ApiDataModel<T>): T? {
        return data.data
    }

}