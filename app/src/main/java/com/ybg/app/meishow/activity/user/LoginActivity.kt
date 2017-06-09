package com.ybg.app.meishow.activity.user

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.utils.LogUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.MainActivity
import com.ybg.app.base.base.BaseActivity
import com.ybg.app.meishow.app.ShowApplication
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.RegularUtil
import com.ybg.app.base.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 用户登陆界面
 */
class LoginActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    private var mMobile: String = ""//手机号or悦美号
    private var mPassword: String = ""//登录密码

    override fun setContentViewId(): Int {
        return R.layout.activity_login
    }

    override fun setUpView() {
        setCustomTitle("登录")
    }

    override fun init() {

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login//手机号or悦美号登录
            -> {
                LogUtil.d(TAG + ": 手机号or悦美号登录")
                mMobile = getTextViewString(et_login_mobile)
                mPassword = getTextViewString(et_login_password)
                if (TextUtils.isEmpty(mMobile) || !RegularUtil.isMobile(mMobile)) {
                    ToastUtil.show(showApplication, R.string.input_mobile_error)
                    return
                }
                if (TextUtils.isEmpty(mPassword) || !RegularUtil.isPassword(mPassword)) {
                    ToastUtil.show(showApplication, R.string.input_password_error)
                    return
                }
                userLogin()
            }
            R.id.tv_register_now//立即注册
            -> {
                RegisterActivity.start(mContext!!)
                finish()
            }
        }

    }

    /**
     * login
     */
    private fun userLogin() {
        hideKeyboard()
        SendRequest.userLogin(mContext!!, mMobile, mPassword, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val resultBean = mGson!!.fromJson(response, JSonResultBean::class.java)
                if (resultBean.isSuccess) {
                    //登录成功
                    mApplication.token = resultBean.data
                    MainActivity.instance?.loadUserInfo()
                    MainActivity.instance?.updateClientId()
                    finish()
                } else {
                    //登录失败
                    LogUtil.d("登录失败：" + resultBean.message)
                    ToastUtil.show(showApplication, resultBean.message)
                }
            }

            override fun onFailure(e: Throwable) {
                LogUtil.d("登陆失败：" + e.message)
                ToastUtil.show(showApplication, "登陆失败，请稍候再试。")
            }
        })
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

}
