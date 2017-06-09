package com.ybg.app.meishow.activity

import android.content.Context
import android.content.Intent
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.base.utils.AppUtil
import com.ybg.app.meishow.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun setContentViewId(): Int {
        return R.layout.activity_about
    }

    override fun setUpView() {
        setCustomTitle("关于")
    }

    override fun init() {
       val version = AppUtil.getAppVersion(mContext!!, "com.ybg.app.meishow")
        tv_about_version.text = String.format("版本：%s", version)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
