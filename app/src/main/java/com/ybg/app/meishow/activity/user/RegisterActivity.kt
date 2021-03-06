package com.ybg.app.meishow.activity.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.utils.LogUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.MainActivity
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.meishow.app.ShowApplication
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.RegularUtil
import com.ybg.app.base.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_register.*

/**
 * 用户注册页面
 */
class RegisterActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    private var mMobile: String = ""//手机号or悦美号
    private var mPassword: String = ""//登陆密码
    private var mCaptcha: String = ""//验证码

    override fun setContentViewId(): Int {
        return R.layout.activity_register
    }

    override fun setUpView() {
        setCustomTitle("注册")
    }

    override fun init() {

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_get_captcha//获取验证码
            -> {
                mMobile = getTextViewString(et_register_mobile)
                if (TextUtils.isEmpty(mMobile) || !RegularUtil.isMobile(mMobile)) {
                    ToastUtil.show(showApplication, R.string.input_mobile_error)
                    return
                }
                getCaptcha()
                mTimer!!.start()
            }
            R.id.btn_register//注册
            -> {
                mMobile = getTextViewString(et_register_mobile)
                mPassword = getTextViewString(et_register_password)
                mCaptcha = getTextViewString(et_register_captcha)
                if (TextUtils.isEmpty(mMobile) || !RegularUtil.isMobile(mMobile)) {
                    ToastUtil.show(showApplication, R.string.input_mobile_error)
                    return
                }
                if (TextUtils.isEmpty(mPassword) || !RegularUtil.isPassword(mPassword)) {
                    ToastUtil.show(showApplication, R.string.input_password_error)
                    return
                }
                if (TextUtils.isEmpty(mCaptcha)) {
                    ToastUtil.show(showApplication, R.string.input_captcha_error)
                    return
                }
                checkCaptcha()
            }
        }
    }

    private var mTimer: CountDownTimer? = object : CountDownTimer((2 * 60 * 1000).toLong(), 1000) {
        override fun onTick(l: Long) {
            btn_get_captcha.isClickable = false
            btn_get_captcha.text = String.format(getString(R.string.get_captcha_later), l / 1000)
            btn_get_captcha.setBackgroundResource(R.drawable.shape_btn_b3)
        }

        override fun onFinish() {
            btn_get_captcha.isClickable = true
            btn_get_captcha.setText(R.string.get_captcha_again)
            btn_get_captcha.setBackgroundResource(R.drawable.shape_btn_login)
        }
    }

    private fun getCaptcha() {
        SendRequest.getCaptcha(mContext!!, mMobile, object : OkCallback<String>(OkStringParser
        ()) {
            override fun onSuccess(code: Int, response: String) {
                LogUtil.d(TAG + ":get message success " + response)
                ToastUtil.show(showApplication, "验证码已发送到您手机")
                runOnUiThread {
                    et_register_captcha.setText("1234")
                }
            }

            override fun onFailure(e: Throwable) {
                LogUtil.d(TAG + ":get message error code = " + e.message)
                ToastUtil.show(showApplication, R.string.request_error)
            }
        })
    }

    private fun checkCaptcha() {
        hideKeyboard()
        SendRequest.checkCaptcha(mContext!!, mMobile, mCaptcha, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val result = mGson!!.fromJson(response, JSonResultBean::class.java)
                if (result != null) {
                    if (result.isSuccess) {
                        //验证码通过，开始实际注册。
                        userRegister()
                    } else {
                        ToastUtil.show(showApplication, result.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, R.string.request_error)
                ToastUtil.show(showApplication, "验证码检查失败，请稍候再试。")
            }
        })
    }

    private fun userRegister() {
        SendRequest.userRegister(mContext!!, mMobile, mPassword, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                LogUtil.d(TAG + ": " + response)
                val resultBean = mGson!!.fromJson(response, JSonResultBean::class.java)
                if (resultBean.isSuccess) {
                    mApplication.token = resultBean.data
                    MainActivity.instance?.loadUserInfo()
                    MainActivity.instance?.updateClientId()
                    CompleteDataActivity.start(mContext!!)
                    finish()
                } else {
                    LogUtil.d("注册失败：" + resultBean.message)
                    ToastUtil.show(showApplication, resultBean.message)
                }
            }

            override fun onFailure(e: Throwable) {
                LogUtil.e(TAG + ": " + e.message)
                ToastUtil.show(showApplication, "注册失败，请稍候再试。")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        //完善资料成功后结束当前页面
        if (resultCode == Activity.RESULT_OK && requestCode == IntentExtra.RequestCode.REQUEST_CODE_REGISTER) {
            finish()
        }
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }
}
