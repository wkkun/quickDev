package com.ya.function

import android.content.Context
import androidx.startup.Initializer
import com.ya.function.net.NetManager
import okhttp3.logging.HttpLoggingInterceptor

class FunctionInitializer : Initializer<Unit> {
    override fun create(context: Context): Unit {
     val loggingInterceptor=  HttpLoggingInterceptor().also {
            it.setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        NetManager.Builder()
            .debug(BuildConfig.DEBUG)
            .logInterceptor(loggingInterceptor)
            .init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}