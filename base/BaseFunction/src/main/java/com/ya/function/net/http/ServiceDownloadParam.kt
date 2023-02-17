package com.ya.function.net.http

/**
 *author: Bai
 *Date:2019/12/19
 *Description
 */
class ServiceDownloadParam(mBuilder: ParamBuilder) {

    val mDownloadUrl = mBuilder.mDownloadUrl

    companion object{

        fun newParamBuilder( mDownloadUrl: String): ParamBuilder {
            return ParamBuilder(mDownloadUrl)
        }

    }
    class ParamBuilder (val mDownloadUrl: String){

        fun build(): ServiceDownloadParam {
            return ServiceDownloadParam(this)
        }

    }

}