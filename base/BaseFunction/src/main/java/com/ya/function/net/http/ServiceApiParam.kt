package com.ya.function.net.http

import com.ya.function.net.gson.JsonUtil
import com.ya.function.net.http.converter.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 *author: Bai
 *Date:2019/11/5
 *Description
 */
class ServiceApiParam<T>(private val mBuilder: ParamBuilder<T>){


    fun getApi(client: OkHttpClient): T{
        return mBuilder.mClientBuilder
            .client(client)
            .baseUrl(mBuilder.mBaseUrl)
            .build()
            .create(mBuilder.clazz)
    }

    companion object{

        fun <T> newDefaultBuilder(mBaseUrl: String, clazz: Class<T>): ParamBuilder<T> {
            return newParamBuilder(mBaseUrl,clazz)
                .addConverterFactory(NullOnEmptyConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(JsonUtil.getGson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }

        fun <T> newParamBuilder( mBaseUrl: String, clazz: Class<T>): ParamBuilder<T> {
            return ParamBuilder(mBaseUrl, clazz)
        }
    }

    class ParamBuilder<T>(val mBaseUrl: String,val clazz: Class<T>){

        val mClientBuilder = Retrofit.Builder()

        fun addConverterFactory(factory: Converter.Factory): ParamBuilder<T> {
            mClientBuilder.addConverterFactory(factory)
            return this
        }

        fun addCallAdapterFactory(factory: CallAdapter.Factory): ParamBuilder<T> {
            mClientBuilder.addCallAdapterFactory(factory)
            return this
        }

        fun build(): ServiceApiParam<T> {
            return ServiceApiParam(this)
        }
    }
}