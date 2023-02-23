package com.ya.function.net.http

import com.ya.function.net.IClientFactory

/**
 *author: Bai
 *Date:2019/11/5
 *Description
 */
class HttpFactory:
    IClientFactory<HttpClient, HttpParam> {

    override fun get(param: HttpParam): HttpClient {
        return HttpClient(param)
    }
}