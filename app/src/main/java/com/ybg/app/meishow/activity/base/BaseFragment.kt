package com.ybg.app.meishow.activity.base

import android.app.Activity
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.app.ShowApplication

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseFragment : Fragment() {

    protected var TAG = javaClass.simpleName
    protected var mContext: Activity? = null
    protected var mGson: Gson? = null
    protected var mRootView: View? = null
    protected var mPageNum = 0//下拉刷新和上拉加载时翻页的记录
    protected var mEventBus: EventBus? = null

    protected val mApplication = ShowApplication.instance!!

    /**
     * 设置布局文件
     */
    @LayoutRes
    protected abstract fun setContentViewId(): Int

    /**
     * 初始化View的方法
     */
    protected abstract fun setUpView()

    /**
     * 初始化方法
     */
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mGson = GsonBuilder().serializeNulls().create()
        mEventBus = EventBus.getDefault()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater!!.inflate(setContentViewId(), container, false)

        setUpView()
        init()
        return mRootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mContext?.let { OkHttpProxy.cancel(it) }
    }

    protected fun getTextViewText(view: TextView?): CharSequence {
        if (view == null) return ""
        return view.text
    }

    protected fun getTextViewString(view: TextView): String {
        return getTextViewText(view).toString().trim { it <= ' ' }
    }

    @Subscribe
    open fun onEvent(event: MessageEvent) {

    }

    protected fun showToast(msg: String) {
        ToastUtil.show(mApplication, msg)
    }

}
