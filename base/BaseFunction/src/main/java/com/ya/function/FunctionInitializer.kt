package com.ya.function

import android.content.Context
import android.util.Log
import androidx.startup.Initializer

class FunctionInitializer : Initializer<FunctionInitializer> {
    override fun create(context: Context): FunctionInitializer {
        Log.d("=====", "create: FunctionInitializer")
        return this
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}