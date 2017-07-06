package com.ybg.app.meishow.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import com.ybg.app.base.utils.AppUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.meishow.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {

    override fun setContentViewId(): Int {
        return 0
    }

    override fun setUpView() {
        setCustomTitle("关于")
    }

    override fun init() {
       val version = AppUtil.getAppVersion(mContext!!, "com.ybg.app.meishow")
        val binding = DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about)
        binding.versionName = String.format("版本：%s", version)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
