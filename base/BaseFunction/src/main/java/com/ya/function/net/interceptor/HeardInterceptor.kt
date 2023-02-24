package com.ya.function.net.interceptor

import okhttp3.Interceptor
import okhttp3.Request

/**
 * 为所有请求添加请求头
 */
object HeardInterceptor {
    fun register(map: Map<String, String>): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            for ((key, value) in map.entries) {
                builder.addHeader(key, value)
            }
            chain.proceed(builder.build())
        }
    }
}

