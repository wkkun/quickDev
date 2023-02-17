package com.ya.function.net.http.judge.impl

import com.ya.function.net.http.exception.ApiException
import com.ya.function.net.http.judge.AbsFlowJudgeFunction
import com.ya.function.net.http.model.ApiDataMetaModel

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
open class DataHasMetaFlowJudgeFunction<T> : AbsFlowJudgeFunction<ApiDataMetaModel<T>, T?>() {

    override fun checkSuccess(dataMeta: ApiDataMetaModel<T>): Boolean {
        return dataMeta.meta.code == 0
    }

    override fun generateDefaultException(dataMeta: ApiDataMetaModel<T>): Throwable {
        return ApiException(dataMeta.meta.code,dataMeta.meta.message)
    }

    override fun convert(dataMeta: ApiDataMetaModel<T>): T? {
        return dataMeta.data
    }

}