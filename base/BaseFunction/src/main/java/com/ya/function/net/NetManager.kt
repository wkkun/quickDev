package com.ya.function.net

import android.content.Context
import com.google.gson.Gson
import com.ya.function.net.config.HttpConfig
import com.ya.function.net.converter.NullOnEmptyConverterFactory
import com.ya.function.net.interceptor.HeardInterceptor
import com.ya.function.net.interceptor.OnCookieInterceptor
import com.ya.function.net.interceptor.ReceivedCookieInterceptor
import com.ya.function.net.ssl.SSLFactory
import okhttp3.ConnectionPool
import okhttp3.EventListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object NetManager {
    const val DEFAULT = "default"
    const val TAG_GLIDE = "glide"
    private val okHttpClientContainer = ConcurrentHashMap<String, OkHttpClient>()
    private val retrofitContainer = ConcurrentHashMap<String, Any>()
    private var config: HttpConfig? = null
    var debug = false

    fun initHttp(config: HttpConfig, builder: OkHttpClient.Builder? = null) {
        this.config = config
        createOkhttpClick(config, builder).also {
            okHttpClientContainer[DEFAULT] = it
        }
    }

     fun getOkhttpClient(tag: String, config: HttpConfig? = null): OkHttpClient {
        return okHttpClientContainer[tag] ?: createOkhttpClick(config ?: HttpConfig()).also {
            okHttpClientContainer[tag] = it
        }
    }

    fun getDefaultOkhttpClient(): OkHttpClient {
        return getOkhttpClient(DEFAULT)
    }

    private fun createOkhttpClick(
        config: HttpConfig,
        builder: OkHttpClient.Builder? = null
    ): OkHttpClient {
        val okHttpBuilder = builder ?: OkHttpClient.Builder()
        // 默认配置
        okHttpBuilder.connectTimeout(config.connectTime, TimeUnit.SECONDS)
            .readTimeout(config.readTime, TimeUnit.SECONDS)
            .writeTimeout(config.writeTime, TimeUnit.SECONDS)
            .connectionPool(
                config.connectionPool ?: ConnectionPool(
                    10,
                    5,
                    TimeUnit.MINUTES
                )
            )
            .retryOnConnectionFailure(config.retryOnConnectionFailure)

        // 添加日志拦截器
        config.loggingInterceptor?.also {
            okHttpBuilder.addInterceptor(it)
        }
        if (debug) {
            //抓包
            okHttpBuilder.sslSocketFactory(
                SSLFactory.initSSLSocketFactory(),
                SSLFactory.initTrustManager()
            )
        }

        config.eventListenerFactory?.also {
            okHttpBuilder.eventListenerFactory(it)
        }

        val map = config.heardMap

        // 添加请求头
        if (map.isNotEmpty()) {
            okHttpBuilder.addInterceptor(HeardInterceptor.register(map))
        }

        // cookie 拦截器
        config.cookieSet.forEach {
            okHttpBuilder.addInterceptor(ReceivedCookieInterceptor.register(it))
        }

        // 添加自定义的拦截器
        okHttpBuilder.interceptors()
        config.interceptor.forEach {
            okHttpBuilder.addInterceptor(it)
        }

        return okHttpBuilder.build()
    }


    // 绑定 retrofit
    @Suppress("UNCHECKED_CAST")
    fun <S> bindRetrofit(serviceClass: Class<S>, baseUrl: String, gson: Gson?): S {
        val cacheKey = serviceClass.name + baseUrl
        return retrofitContainer[cacheKey] as S ?: createRetrofitService(
            serviceClass,
            baseUrl,
            gson
        ).also {
            retrofitContainer[cacheKey] = it as Any
        }
    }

    private fun <S> createRetrofitService(serviceClass: Class<S>, baseUrl: String, gson: Gson?): S {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(NullOnEmptyConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson ?: Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getDefaultOkhttpClient())
            .build()
            .create(serviceClass)
    }


    class Builder {

        private val httpConfig = HttpConfig()

        /***
         * 配置通用的 超时时间 默认单位是 秒
         */
        fun writeTimeout(time: Long) = apply {
            httpConfig.writeTime = time
        }

        /***
         * 配置通用的 超时时间 默认单位是 秒
         */
        fun readTimeout(time: Long) = apply {
            httpConfig.readTime = time
        }

        /***
         * 配置通用的 超时时间 默认单位是 秒
         */
        fun connectTimeout(time: Long) = apply {
            httpConfig.connectTime = time
        }

        /**
         * 线程池
         */
        fun connectPool(pool: ConnectionPool) = apply {
            httpConfig.connectionPool = pool
        }

        /**
         * 是否重试 默认 true
         */
        fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean) = apply {
            httpConfig.retryOnConnectionFailure = retryOnConnectionFailure
        }

        /**
         * 添加日志拦截
         */
        fun logInterceptor(loggingInterceptor: HttpLoggingInterceptor) = apply {
            httpConfig.loggingInterceptor = loggingInterceptor
        }

        /**
         * 添加自定义拦截器
         */
        fun addInterceptor(interceptor: Interceptor, need: Boolean = true) = apply {
            if (need) {
                httpConfig.interceptor.add(interceptor)
            }
        }

        /***
         * 添加自定义的[Converter.Factory]
         */
        fun converterFactory(factory: Converter.Factory) = apply {
            httpConfig.converterFactorySet.add(factory)
        }

        /***
         * 添加自定义的[CallAdapter.Factory]
         */
        fun callAdapterFactory(factory: CallAdapter.Factory) = apply {
            httpConfig.callAdapterFactorySet.add(factory)
        }


        /***
         * 为所有请求都添加heard
         */
        fun head(key: String, value: String) = apply {
            httpConfig.heardMap[key] = value
        }

        /**
         * 添加 cookie 拦截
         */
        fun cookieInterceptor(
            onCookieInterceptor: OnCookieInterceptor
        ) = apply {
            httpConfig.cookieSet.add(onCookieInterceptor)
        }


        /***
         *
         * 时候生效
         *
         * 实际上只是在请求的时候带上请求头 [max-age=time] max-age:最大缓存时间
         */
        fun cacheNetWorkTimeOut(time: Int) = apply {
            httpConfig.cacheNetworkTimeOut = time
        }

        /***
         * 无网时 特定时间之前  会将 有网时候请求到的数据 返回
         *
         * 默认是30天。
         * [time] 单位 秒
         */
        fun cacheNoNetWorkTimeOut(time: Int) = apply {
            httpConfig.cacheNoNetworkTimeOut = time
        }

        /***
         * 缓存的大小，默认 10M
         * [size] 单位 byte
         */
        fun cacheSize(size: Int) = apply {
            httpConfig.cacheSize = size
        }


        fun eventListenerFactory(eventListenerFactory: EventListener.Factory) = apply {
            httpConfig.eventListenerFactory = eventListenerFactory
        }

        // 使用默认的 client，然后添加自定义的属性
        fun flag() = apply {
            httpConfig.flag = true
        }

        /***
         * 显示日志
         * [debug] true  会显示日志 在 logcat 上
         */
        fun debug(debug: Boolean) = apply {
            NetManager.debug = debug
        }

        fun get(): HttpConfig {
            return httpConfig
        }

        fun init(context: Context) {
            initHttp(this.httpConfig)
        }
    }
}