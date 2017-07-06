package com.ybg.app.meishow.activity.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.UserBase
import com.ybg.app.base.bean.UserInfo
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.app.ShowApplication
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = javaClass.simpleName
    protected var mContext: Activity? = null
    protected var mGson: Gson? = null
    private val mRootView: View? = null

    protected val mApplication = ShowApplication.instance!!

    protected var mPageNum: Int = 0//下拉刷新和上拉加载时翻页的记录
    protected var mEventBus: EventBus? = null
    protected var mHandler: Handler? = null
    protected var mProgressDialog: ProgressDialog? = null

    protected abstract fun setContentViewId(): Int

    /**
     * 初始化View的方法
     */
    protected abstract fun setUpView()

    /**
     * 初始化方法
     */
    protected abstract fun init()

    fun <T : View> `$`(@IdRes idRes: Int): T {
        return findViewById(idRes) as T
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = setContentViewId()
        if (layoutId > 0) {
            setContentView(layoutId)
        }
        mContext = this
        mEventBus = EventBus.getDefault()
        mEventBus!!.register(this)
        mHandler = Handler()
        mGson = GsonBuilder().serializeNulls().create()
        setContent()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mEventBus != null) mEventBus!!.unregister(this)
        OkHttpProxy.cancel(mContext!!)
    }

    private fun setContent() {
        setUpView()
        init()
    }

    /*隐藏虚拟键盘*/
    protected fun hideKeyboard() {
        val imm = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mContext!!.window.peekDecorView().windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    /*显示虚拟键盘*/
    protected fun showKeyboard() {
        val imm = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mContext!!.window.peekDecorView().windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    protected fun getTextViewText(view: TextView?): CharSequence {
        if (view == null) return ""
        return view.text
    }

    protected fun getTextViewString(view: TextView): String {
        return getTextViewText(view).toString().trim()
    }

    //防止字体随系统设置发生改变
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    @Subscribe
    open fun onEvent(event: MessageEvent) {//处理EventBus 发送的消息的方法,具体操作在子类实现

    }

    private val mLocalCallBack = Handler.Callback { msg ->
        onHandler(msg)
        false
    }

    protected open fun onHandler(msg: Message) {//处理handler发送的消息,具体操作在子类中实现

    }

    protected fun showProgressDialog(message: String) {
        var msg = message
        if (mProgressDialog != null && mProgressDialog!!.isShowing) return
        if (mProgressDialog == null) mProgressDialog = ProgressDialog(mContext)
        if (TextUtils.isEmpty(msg)) msg = "加载中..."
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.show()
    }

    protected fun dismissProgressDialog() {
        if (mProgressDialog == null) return
        if (mProgressDialog!!.isShowing) mProgressDialog!!.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun setCustomTitle(title: String) {
        try {
            val actionBar = supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar.title = title
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    protected fun checkUserValid(msg: String) {
        if (mApplication.checkNeedLogin(msg)) {
            mApplication.token = ""
            EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_NEED_LOGIN))
        } else {
            showToast(msg)
        }
    }

    protected fun loadUserBase(call: (UserBase) -> Unit) {
        SendRequest.getUserBase(mContext!!, mApplication.token, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                mGson?.let {
                    val userBase = mGson!!.fromJson(data, UserBase::class.java)
                    call(userBase)
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                checkUserValid(jsonBean.message)
            }

            override fun onException(e: Throwable) {
                super.onException(e)
                showToast("获取用户信息失败。")
            }
        })
    }

    protected fun loadUserInfo(call: (UserInfo) -> Unit) {
        SendRequest.getUserInfo(mContext!!, mApplication.token, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                mGson?.let {
                    val userInfo = mGson!!.fromJson(data, UserInfo::class.java)
                    call(userInfo)
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                super.onJsonFail(jsonBean)
                checkUserValid(jsonBean.message)
            }

            override fun onException(e: Throwable) {
                super.onException(e)
                showToast("获取用户信息失败。")
            }
        })
    }

    protected fun workInLoopThread(work: () -> Unit) {
        Thread {
            Looper.prepare()
            kotlin.run(work)
            Looper.loop()
        }.start()
    }

    protected fun showToast(msg: String) {
        ToastUtil.show(mApplication, msg)
    }

}
