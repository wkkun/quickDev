package com.ya.function.net.http

import com.fenzotech.hitup.net.IClientFactory

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