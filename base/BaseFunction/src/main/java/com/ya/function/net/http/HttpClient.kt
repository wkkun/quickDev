package com.ya.function.net.http

import com.fenzotech.hitup.net.IClient
import com.ya.function.net.http.service.DownloadService
import okhttp3.Request

/**
 *author: Bai
 *Date:2019/11/5
 * http 实现类
 */
class HttpClient(private val param: HttpParam): IClient {

    fun<T> createServiceApi(serviceApiParam: ServiceApiParam<T>): T{
        return serviceApiParam.getApi(param.getOkHttp())
    }


    fun createServiceDownload(serviceDownloadParam: ServiceDownloadParam): DownloadService {

        val request = Request.Builder()
            .url(serviceDownloadParam.mDownloadUrl).build()

        return DownloadService(param.getOkHttp(),request)

    }
}