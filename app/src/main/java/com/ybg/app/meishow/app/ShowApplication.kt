package com.ybg.app.meishow.app

import android.content.Context
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.ybg.app.base.app.YbgAPP

/**
 * Created by yangbagang on 2017/5/14.
 */
class ShowApplication : YbgAPP() {

    var ymCode = ""

    override fun onCreate() {
        super.onCreate()

        instance = this
        initImageLoader(applicationContext)
    }

    private fun initImageLoader(context: Context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        val config = ImageLoaderConfiguration.Builder(context)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024) // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        config.writeDebugLogs() // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build())
    }

    fun setAutoPlay(boolean: Boolean) {
        preference.setBoolean("autoPlay", boolean)
    }

    fun isAutoPlay(): Boolean {
        return preference.getBoolean("autoPlay", false)
    }

    fun setReceiverMsg(boolean: Boolean) {
        preference.setBoolean("receiverMsg", boolean)
    }

    fun isReceiverMsg(): Boolean {
        return preference.getBoolean("receiverMsg", false)
    }

    companion object {
        var instance: ShowApplication? = null
            private set
    }
}