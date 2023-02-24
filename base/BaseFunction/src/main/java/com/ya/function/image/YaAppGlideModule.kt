package com.ya.function.image

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.ya.function.net.NetManager
import java.io.InputStream


@GlideModule(glideName = "GlideApp")
class YaAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        // 日志级别
        builder.setLogLevel(Log.DEBUG)

        // 未捕获异常策略
        // 在加载图片时假如发生了一个异常 (例如, OOM), Glide 将会使用一个 GlideExecutor.UncaughtThrowableStrategy 。
        // 默认策略是将异常打印到设备的 LogCat 中。可被定制
//        val newDiskCacheExecutor = newDiskCacheExecutor(GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
//        val newSourceExecutor = newSourceExecutor(GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
//        ReflectUtils.setFinalField(newDiskCacheExecutor.javaClass,"delegate",ThreadPools.IO_THREAD_POOL.get(),true,false)
//        ReflectUtils.setFinalField(newSourceExecutor.javaClass,"delegate",ThreadPools.IO_THREAD_POOL.get(),true,false)
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(GlideExecutor.UncaughtThrowableStrategy.DEFAULT))
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(GlideExecutor.UncaughtThrowableStrategy.DEFAULT))

        // 图片默认请求
        builder.setDefaultRequestOptions(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .format(DecodeFormat.PREFER_RGB_565) // 默认8888
                .dontAnimate() // 不能全局配置，GIF显示不出来,开启：set(GifOptions.DISABLE_ANIMATION, false)
                .disallowHardwareConfig()
        )
//        builder.setConnectivityMonitorFactory { context, listener -> MyConnectivityMonitor(context, listener) }

        // 内存缓存
        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))

        // BitmapPool
        val calculatorBitmap = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(3f)
            .build()
        builder.setBitmapPool(LruBitmapPool(calculatorBitmap.bitmapPoolSize.toLong()))

        // 磁盘高速缓存
        val diskCacheSizeBytes: Long = 1024 * 1024 * 200  // 200MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(NetManager.getOkhttpClient(NetManager.TAG_GLIDE))
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}