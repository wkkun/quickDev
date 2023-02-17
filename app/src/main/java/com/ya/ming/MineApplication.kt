package com.ya.ming

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication

class MineApplication: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}