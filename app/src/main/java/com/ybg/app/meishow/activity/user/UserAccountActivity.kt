package com.ybg.app.meishow.activity.user

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.UserAccount
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.meishow.view.CircleImageView

class UserAccountActivity : BaseActivity() {

    private lateinit var iv_user_logo: CircleImageView
    private lateinit var tv_nickname: TextView

    private lateinit var totalMoney: TextView
    private lateinit var restMoney: TextView
    private lateinit var usedMoney: TextView

    override fun setContentViewId(): Int {
        return R.layout.activity_user_account
    }

    override fun setUpView() {
        iv_user_logo = findViewById(R.id.ci_user_avatar) as CircleImageView
        tv_nickname = findViewById(R.id.tv_user_name) as TextView

        totalMoney = findViewById(R.id.tv_user_total) as TextView
        restMoney = findViewById(R.id.tv_user_rest) as TextView
        usedMoney = findViewById(R.id.tv_user_used) as TextView
    }

    override fun init() {
        setCustomTitle("账户中心")
    }

    override fun onStart() {
        super.onStart()
        loadUserBase { userBase ->
            //头像
            val utils = ImageLoaderUtils.instance
            if (TextUtils.isEmpty(userBase.avatar)) {
                //需要修改默认头像时修改此处
                //utils.loadBitmap(userImage, R.mipmap.ic_default_girl);
            } else {
                utils.loadBitmap(iv_user_logo, HttpUrl.getImageUrl(userBase.avatar))
            }
            //呢称
            tv_nickname.text = userBase.nickName
            //账户
            getUserAccount()
        }
    }

    private fun getUserAccount() {
        SendRequest.getAccountInfo(mContext!!, mApplication.token, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val userAccount = mGson!!.fromJson<UserAccount>(data, object : TypeToken<UserAccount>(){}.type)
                runOnUiThread {
                    totalMoney.text = "${userAccount.totalMoney}"
                    restMoney.text = "${userAccount.restMoney}"
                    usedMoney.text = "${userAccount.usedMoney}"
                }
            }
        })
    }

    companion object {

        /**
         * @param context 跳转到本页面
         */
        fun start(context: Context) {
            val starter = Intent(context, UserAccountActivity::class.java)
            context.startActivity(starter)
        }
    }

}
