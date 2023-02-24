package com.ya.function.net.interceptor

import android.content.Context
import com.ya.function.net.config.HttpConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit


internal abstract class CacheInterceptor(private val context: Context, private val httpConfig: HttpConfig) :
    Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val resp: Response
//        val req: Request = if (GmuNetUtils.isNetworkAvailable(context)) {
//            chain.request()
//                .newBuilder()
//                .build()
//        } else {
//            // 无网络,检查*天内的缓存,即使是过期的缓存
////            val time = when (httpConfig.cacheNoNewWorkType) {
////                NoNetWorkCacheType.NO_TIMEOUT -> {
////                    Integer.MAX_VALUE
////                }
////                NoNetWorkCacheType.HAS_TIMEOUT -> {
////                    httpConfig.cacheNoNetworkTimeOut
////                }
////                else -> {
////                    0
////                }
////            }
//            chain.request().newBuilder()
//                .cacheControl(
//                    CacheControl.Builder()
//                        .onlyIfCached()
//                        .maxStale(time, TimeUnit.SECONDS)
//                        .build()
//                )
//                .build()
//        }
//        resp = chain.proceed(req)
//        return resp.newBuilder().build()
//    }
}