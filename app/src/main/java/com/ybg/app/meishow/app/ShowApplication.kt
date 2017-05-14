package com.ybg.app.meishow.app

import com.ybg.app.base.app.YbgAPP

/**
 * Created by yangbagang on 2017/5/14.
 */
class ShowApplication : YbgAPP() {

    override fun onCreate() {
        super.onCreate()

        instance = this
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