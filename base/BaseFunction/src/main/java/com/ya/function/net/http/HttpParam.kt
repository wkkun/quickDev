package com.ya.function.net.http

import android.util.ArrayMap
import com.ya.function.net.http.log.Level
import com.ya.function.net.http.log.LoggingInterceptor
import com.ya.function.net.http.ssl.SSLFactory
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 *author: Bai
 *Date:2019/11/5
 *Description
 */
class HttpParam(private val mBuilder: ParamBuilder) {

    private val mOkHttpClient: OkHttpClient = mBuilder.mOkHttpBuilder.build()

    fun getOkHttp(): OkHttpClient {
        return mOkHttpClient
    }

    fun bindTag(mTag: Any): HttpParam {
        mCacheParamOkHttp[mTag] = this
        return this
    }

    fun bindDefaultHitup(): HttpParam {
        bindTag(mHitupHttpTag)
        return this
    }

    companion object{

        /**
         * hitup 默认的 http ；
         * 可以绑定一些基础功能
         * 例如 原子参数/或者其他
         */
        private const val mHitupHttpTag = "mHitupHttpTag"


        /**
         * 缓存的 HttpParam
         */
        private val mCacheParamOkHttp = ArrayMap<Any, HttpParam>()


        private val mOkHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(15_000, TimeUnit.MILLISECONDS)
            .writeTimeout(15_000, TimeUnit.MILLISECONDS)
            .readTimeout(15_000, TimeUnit.MILLISECONDS)
//            .addInterceptor(TimeHeaderInterceptor())
            .connectionPool(ConnectionPool(10, 5, TimeUnit.MINUTES))


        /***
         * 获取全局唯一的okhttp
         * 库里创建的okhttp 实例都是以 此为基准 ,可以放一些公共的设置
         */
        fun getSingleOkHttpBuilder(): OkHttpClient.Builder {
            return mOkHttpBuilder
        }

        /**
         * 创建自定义的
         */
        fun createCustomer(): ParamBuilder {
            return ParamBuilder(mOkHttpBuilder.build())
        }

        /**
         * 创建自定义的
         */
        fun createCustomer(param: HttpParam): ParamBuilder {
            return ParamBuilder(param.getOkHttp())
        }

        /**
         * 创建默认的
         */
        fun createDefault(): ParamBuilder {
            return ParamBuilder(mOkHttpBuilder.build())
        }

        /**
         * 自动创建
         */
        fun autoCreate(isDebug: Boolean): ParamBuilder {
            return if(isDebug){
                createCustomer()
                    .printLog()
                    .capture()
            }else{
                createDefault()
            }
        }

        /**
         * 通过 tag 获取对应的 ParamBuilder 对象
         */
        fun getByTag(mTag: Any): HttpParam? {
            return mCacheParamOkHttp[mTag]
        }

        /**
         * 获取 hitup 默认的 httpParam 对象
         */
        fun getHitupDefault(): HttpParam {

            val index = mCacheParamOkHttp.indexOfKey(mHitupHttpTag)
            if(index < 0){
                throw Exception("没有找到默认的HitupHttpParam ，请调用 bindDefaultHitup 绑定")
            }
            return mCacheParamOkHttp.valueAt(index)
        }
    }

    class ParamBuilder(private val mOkHttp: OkHttpClient) {

        val mOkHttpBuilder: OkHttpClient.Builder = mOkHttp.newBuilder()

        /**
         * 打印日志
         */
        fun printLog(): ParamBuilder {
            mOkHttpBuilder.addInterceptor(
                LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("HTTP-Request")
                .response("HTTP-Response")
                .build())
            return this
        }

        /**
         * 添加拦截器
         */
        fun addInterceptor(interceptor: Interceptor): ParamBuilder {
            mOkHttpBuilder.addInterceptor(interceptor)
            return this
        }

        /**
         * 抓包
         */
        fun capture(): ParamBuilder {
            mOkHttpBuilder.sslSocketFactory(SSLFactory.initSSLSocketFactory(), SSLFactory.initTrustManager())
            return this
        }


        fun create(): HttpParam {
            return HttpParam(this)
        }
    }

}