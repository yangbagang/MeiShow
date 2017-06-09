package com.ybg.app.meishow.activity.user

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.pgyersdk.update.PgyUpdateManager
import com.ybg.app.base.utils.AppUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.AgreementActivity
import com.ybg.app.meishow.activity.MainActivity
import com.ybg.app.base.base.BaseActivity
import com.ybg.app.base.constants.AppConstant
import com.ybg.app.base.utils.FileUtils
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.app.ShowApplication
import kotlinx.android.synthetic.main.activity_user_setting.*
import java.io.File

/**
 * 类描述：设置中心页面
 */
class UserSettingActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    private var tvCache: TextView? = null
    private var tvCopyright: TextView? = null
    private var switchWifiAuto: Switch? = null
    private var switchNewMsg: Switch? = null

    override fun setContentViewId(): Int {
        return R.layout.activity_user_setting
    }

    override fun setUpView() {
        tvCache = findViewById(R.id.tv_cache) as TextView
        tvCopyright = findViewById(R.id.tv_copyright) as TextView
        switchWifiAuto = findViewById(R.id.switch_wifi_auto) as Switch
        switchNewMsg = findViewById(R.id.switch_new_msg) as Switch

        mContext = this@UserSettingActivity
        setCustomTitle("设置中心")
    }

    override fun init() {
        if (showApplication.isAutoPlay()) {
            switchWifiAuto?.isChecked = true
        }
        if (showApplication.isReceiverMsg()) {
            switchNewMsg?.isChecked = true
        }
        switchWifiAuto!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showApplication.setAutoPlay(true)
            } else {
                showApplication.setAutoPlay(false)
            }
        }
        switchNewMsg!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showApplication.setReceiverMsg(true)
            } else {
                showApplication.setReceiverMsg(false)
            }
        }
        tvCopyright?.text = AppUtil.getAppVersion(mContext!!, "com.ybg.app.meishow")

        updateCacheSize()
        PgyUpdateManager.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PgyUpdateManager.unregister()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.rl_drafts -> {
                //ToastUtil.show("草稿箱")
            }
            R.id.tv_cache -> {
                clearCacheDir()
            }
            R.id.tv_copyright -> {
                //ToastUtil.show("版本更新")
            }
            R.id.rl_agreement -> {
                //ToastUtil.show("协议")
                startActivity(Intent(this, AgreementActivity::class.java))
            }
            R.id.tv_logout -> {
                mApplication.token = ""
                MainActivity.instance?.removeUserInfo()
            }
        }
    }

    private fun updateCacheSize() {
        Thread {
            try {
                val cacheDir = File(AppConstant.BasePath)
                val cacheSize = FileUtils.getDirSizeInByte(cacheDir)
                val  formatSize = FileUtils.formatFileSize(cacheSize)
                runOnUiThread {
                    tv_cache.text = formatSize
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun clearCacheDir() {
        object : AsyncTask<Unit, Unit, Boolean>() {

            val progressDiag = AppUtil.getProgressDialog(this@UserSettingActivity, "正在清理缓存文件...")

            override fun doInBackground(vararg params: Unit?): Boolean {
                var result = false
                try {
                    result = FileUtils.deleteFile(AppConstant.BasePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    result = false
                }
                return result
            }

            override fun onPreExecute() {
                super.onPreExecute()
                if (!progressDiag.isShowing) {
                    progressDiag.show()
                }
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                if (progressDiag.isShowing) {
                    progressDiag.dismiss()
                }
                if (result != null && result) {
                    tv_cache.text = "0B"
                } else {
                    ToastUtil.show(showApplication, "清理缓存失败。")
                }
            }
        }.execute()
    }

    companion object {

        /**
         * @param context 跳转到本页面
         */
        fun start(context: Context) {
            val starter = Intent(context, UserSettingActivity::class.java)
            context.startActivity(starter)
        }
    }
}