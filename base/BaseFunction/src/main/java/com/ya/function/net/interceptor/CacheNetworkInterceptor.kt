package com.ya.function.net.interceptor

import com.ya.function.net.config.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

internal abstract class CacheNetworkInterceptor(private val config: HttpConfig) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val time = when (config.cacheNetWorkType) {
//            NetWorkCacheType.NOCACHE -> {
//                0
//            }
//            NetWorkCacheType.CACHE_TIME -> {
//                config.cacheNetworkTimeOut
//            }
//            else -> {
//                0
//            }
//        }
//        val value = 0
//
//        val cacheControl = when(value) {
//            // 不缓存数据
//            1->"no-store"
//            //如果有网络,则将缓存的过期时间,设置为0,获取最新数据
//            2->"private, max-age=" + 0
//            // 不缓存
//            else->"no-cache"
//        }

        // 有网络时，缓存数据，缓存多久：time
//        return chain.proceed(chain.request()).newBuilder()
//            // 清除头信息 因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//            .removeHeader("Pragma")
//            .addHeader("Cache-Control", "max-age=$time")
////            .addHeader("Cache-Control", cacheControl)
//            .build()
//    }

}
