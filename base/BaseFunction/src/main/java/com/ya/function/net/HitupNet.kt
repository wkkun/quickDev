package com.ya.function.net

import com.ya.function.net.http.HttpFactory
import com.ya.function.net.http.HttpParam
import com.ya.function.net.http.ServiceApiParam
import com.ya.function.net.http.ServiceDownloadParam
import com.ya.function.net.http.service.DownloadService

/**
 *author: Bai
 *Date:2019/11/5
 *Description
 */
class HitupNet{

    companion object{

        fun<T> newHttpApi(httpParam: HttpParam, serviceApiParam: ServiceApiParam<T>): T {
            return HttpFactory().get(httpParam).createServiceApi(serviceApiParam)
        }

        fun newHttpDownload(httpParam: HttpParam, serviceDownloadParam: ServiceDownloadParam): DownloadService {
            return HttpFactory().get(httpParam).createServiceDownload(serviceDownloadParam)
        }
    }
}
