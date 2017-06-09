package com.ybg.app.meishow.activity.home

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.base.BaseFragment
import com.ybg.app.base.bean.BangItem
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.bang.ShowBang
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.app.ShowApplication
import kotlinx.android.synthetic.main.fragment_charts.*

/**
 * 悦美榜
 */
class ChartsFragment : BaseFragment() {

    private lateinit var zhongHuaBang: RelativeLayout
    private lateinit var meiLiBang: RelativeLayout
    private lateinit var renQiBang: RelativeLayout
    private lateinit var huoLiBang: RelativeLayout
    private lateinit var haoQiBang: RelativeLayout

    private val showApplication = ShowApplication.instance!!

    override fun setContentViewId(): Int {
        return R.layout.fragment_charts
    }

    override fun setUpView() {
        zhongHuaBang = mRootView!!.findViewById(R.id.rl_zh_bang) as RelativeLayout
        meiLiBang = mRootView!!.findViewById(R.id.rl_ml_bang) as RelativeLayout
        renQiBang = mRootView!!.findViewById(R.id.rl_hl_bang) as RelativeLayout
        huoLiBang = mRootView!!.findViewById(R.id.rl_rq_bang) as RelativeLayout
        haoQiBang = mRootView!!.findViewById(R.id.rl_hq_bang) as RelativeLayout

        zhongHuaBang.setOnClickListener {
            ShowBang.start(mContext!!, 0)
        }
        meiLiBang.setOnClickListener {
            ShowBang.start(mContext!!, 1)
        }
        renQiBang.setOnClickListener {
            ShowBang.start(mContext!!, 2)
        }
        huoLiBang.setOnClickListener {
            ShowBang.start(mContext!!, 3)
        }
        haoQiBang.setOnClickListener {
            ShowBang.start(mContext!!, 4)
        }
    }

    override fun init() {
        loadRuiMeiBang()
        loadMeiLiBang()
        loadHuoLiBang()
        loadRenQiBang()
        loadHaoQiBang()
    }

    fun loadRuiMeiBang() {
        SendRequest.getRuiMeiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(zh_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_zh_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(zh_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_zh_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(zh_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_zh_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取美力榜失败")
            }

        })
    }

    fun loadMeiLiBang() {
        SendRequest.getRuiMeiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(ml_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_ml_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(ml_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_ml_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(ml_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_ml_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取美力榜失败")
            }

        })
    }

    fun loadHuoLiBang() {
        SendRequest.getHuoLiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(hl_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_hl_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(hl_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_hl_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(hl_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_hl_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取美力榜失败")
            }

        })
    }

    fun loadRenQiBang() {
        SendRequest.getRenQiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(rq_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_rq_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(rq_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_rq_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(rq_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_rq_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取美力榜失败")
            }

        })
    }

    fun loadHaoQiBang() {
        SendRequest.getRuiMeiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(hq_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_hq_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(hq_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_hq_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(hq_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_hq_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取美力榜失败")
            }

        })
    }

    companion object {

        fun newInstance(): ChartsFragment {

            val args = Bundle()

            val fragment = ChartsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
