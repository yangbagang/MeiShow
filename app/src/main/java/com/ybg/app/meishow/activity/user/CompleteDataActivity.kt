package com.ybg.app.meishow.activity.user

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.base.utils.LogUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.MainActivity
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.listener.UploadListener
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.meishow.utils.OnoptionsUtils
import com.ybg.app.meishow.view.gallery.MultiImageSelectorActivity
import com.ybg.app.meishow.view.pickerview.OptionsPopupWindow
import com.ybg.app.meishow.view.pickerview.TimePopupWindow
import kotlinx.android.synthetic.main.activity_complete_data.*
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*

/**
 * 完善资料页面
 */
class CompleteDataActivity : BaseActivity() {

    private var mNickName = ""//昵称
    private var mAvatar = "c2hvdy9iYXNlL2F2YXRhci9kZWZhdWx0LnBuZw=="//头像
    private var mSex = -1//性别
    private var mBirthday: String? = null//生日

    private var path: String? = null//头像图片文件

    override fun setContentViewId(): Int {
        return R.layout.activity_complete_data
    }

    override fun setUpView() {
        setCustomTitle("填写资料")
    }

    override fun init() {

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_user_avatar -> MultiImageSelectorActivity.start(mContext!!, true, 1,
                    MultiImageSelectorActivity.MODE_SINGLE)
            R.id.tv_user_birthday -> OnoptionsUtils.showDateSelect(mContext!!, tv_user_birthday,
                    object : TimePopupWindow.OnTimeSelectListener {
                        override fun onTimeSelect(date: Date) {
                            tv_user_birthday.text = DateUtil.format(date)
                            mBirthday = DateUtil.format(date)
                        }
                    })
            R.id.tv_user_gender -> OnoptionsUtils.showGardenSelect(mContext!!, tv_user_gender,
                    object : OptionsPopupWindow.OnOptionsSelectListener {
                        override fun onOptionsSelect(options1: Int, option2: Int, options3: Int, options4: Int) {
                            tv_user_gender.text = if (options1 == 1) "男" else "女"
                            mSex = options1
                        }
                    })
            R.id.btn_complete_register -> {
                mNickName = getTextViewString(et_user_nickName)
                if (TextUtils.isEmpty(mNickName)) {
                    showToast("请输入昵称!")
                    return
                }
                if (TextUtils.isEmpty(mBirthday)) {
                    showToast("请选择生日!")
                    return
                }
                if (mSex < 0) {
                    showToast("请选择性别!")
                    return
                }
                saveUserInfo()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentExtra.RequestCode.REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            path = data.getStringExtra(MultiImageSelectorActivity.EXTRA_RESULT)
            ImageLoaderUtils.instance.loadFileBitmap(iv_user_avatar!!, path!!)
        }
    }

    private fun saveUserInfo() = if (path != null && path != "") {
        uploadAvatar()
    } else {
        completeUserInfo()
    }

    private fun completeUserInfo() {
        hideKeyboard()
        btn_complete_register.isEnabled = false
        val token = mApplication.token
        SendRequest.completeUserInfo(mContext!!, token, mBirthday!!, mNickName, "${mSex}", mAvatar, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                LogUtil.d(TAG + ": " + response)
                btn_complete_register.isEnabled = true
                val resultBean = mGson!!.fromJson(response, JSonResultBean::class.java)
                if (resultBean.isSuccess) {
                    btn_complete_register.progress = btn_complete_register.maxProgress
                    MainActivity.instance?.loadUserInfo()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    showToast(resultBean.message)
                }
            }

            override fun onFailure(e: Throwable) {
                btn_complete_register.isEnabled = true
                LogUtil.e(TAG + ": " + e.message)
            }
        })
    }

    private fun uploadAvatar() {
        //上传头像
        btn_complete_register.isEnabled = false
        btn_complete_register.loadingText = "准备上传头像..."
        SendRequest.uploadFile(mContext!!, "avatar", File(path), object: UploadListener(){
            override fun onResponse(call: Call?, response: Response?) {
                response?.let { onSuccess(response) }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                e?.let { onFailure(it) }
            }

            override fun onSuccess(response: Response) {
                workInLoopThread {
                    btn_complete_register.loadingText = "头像上传成功，正在保存数据"
                }
                val json = JSONObject(response.body().string())
                mAvatar = json.getString("fid")
                path = ""
                completeUserInfo()
            }

            override fun onFailure(e: Exception) {
                workInLoopThread {
                    btn_complete_register.isEnabled = true
                }
            }

            override fun onUIProgress(progress: Progress) {
                workInLoopThread {
                    val progressInt: Int = (progress.currentBytes * 100 / progress.totalBytes).toInt()
                    btn_complete_register.loadingText = "正在上传头像${progressInt}%"
                }
            }
        })
    }

    companion object {

        fun start(context: Activity) {
            val starter = Intent(context, CompleteDataActivity::class.java)
            context.startActivityForResult(starter, IntentExtra.RequestCode.REQUEST_CODE_REGISTER)
        }
    }
}
