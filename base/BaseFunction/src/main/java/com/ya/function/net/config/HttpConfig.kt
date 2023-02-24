package com.ya.function.net.config

import com.ya.function.net.interceptor.OnCookieInterceptor
import okhttp3.ConnectionPool
import okhttp3.EventListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

data class HttpConfig(

    // 连接超时等
    var connectTime: Long = DefaultConfig.DEFAULT_TIME_OUT,

    // 写超时
    var readTime: Long = DefaultConfig.DEFAULT_TIME_OUT,

    // 读超时
    var writeTime: Long = DefaultConfig.DEFAULT_TIME_OUT,

    var callTime: Long = DefaultConfig.DEFAULT_CALL_TIME_OUT,

    var connectionPool: ConnectionPool? = null,

    // 是否重试
    var retryOnConnectionFailure: Boolean = DefaultConfig.DEFAULT_RETRY,

    // 日志的拦截器
    var loggingInterceptor: HttpLoggingInterceptor? = null,

    // cookie的拦截器
    var cookieSet: MutableSet<OnCookieInterceptor> = mutableSetOf(),

    // 自定义的拦截器
    var interceptor: MutableSet<Interceptor> = mutableSetOf(),

    // 配置通用的请求头
    var heardMap: HashMap<String, String> = HashMap(),

    // 添加自定的转换器
    var converterFactorySet: MutableSet<Converter.Factory> = mutableSetOf(),

    // 添加自定义的适配器
    var callAdapterFactorySet: MutableSet<CallAdapter.Factory> = mutableSetOf(),

    // 有网时候的缓存策略 默认无缓存策略
//    var cacheNetWorkType: NetWorkCacheType = NetWorkCacheType.NONE,

    // 无网时候的缓存策略 默认无缓存策略
//    var cacheNoNewWorkType: NoNetWorkCacheType = NoNetWorkCacheType.NONE,

    // 有网时:特定时间之后请求数据；（比如：特定时间为20s） 默认20
    var cacheNetworkTimeOut: Int = DefaultConfig.DEFAULT_CACHE_NETWORK_TIMEOUT,

    // 无网时:特定时间之前请求有网请求好的数据；（（比如：特定时间为30天） 默认30 天  单位（秒）
    var cacheNoNetworkTimeOut: Int = DefaultConfig.DEFAULT_NO_CACHE_NETWORK_TIMEOUT,

    // 缓存大小  10M
    var cacheSize: Int = DefaultConfig.DEFAULT_CACHE_SIZE,

    // 缓存位置
    var cachePath: String = "",

    var eventListenerFactory: EventListener.Factory? = null

) {
    // 特殊情况，需要对okHttpBuilder特殊处理的情况
    var callback: Callback? = null

    var flag: Boolean = false

    interface Callback {

        fun handleBuilder(okHttpBuilder: OkHttpClient.Builder): OkHttpClient

        fun handleUrl(url:String):String? {
            return null
        }

    }
}