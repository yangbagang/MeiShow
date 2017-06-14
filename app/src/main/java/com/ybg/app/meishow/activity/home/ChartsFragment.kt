package com.ybg.app.meishow.activity.home

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.BangItem
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.bang.ShowBang
import com.ybg.app.meishow.activity.base.BaseFragment
import com.ybg.app.meishow.utils.ImageLoaderUtils
import kotlinx.android.synthetic.main.fragment_charts.*

/**
 * 悦美榜
 */
class ChartsFragment : BaseFragment() {

    private lateinit var huoYueBang: RelativeLayout
    private lateinit var renQiBang: RelativeLayout
    private lateinit var shouRuBang: RelativeLayout
    private lateinit var haoQiBang: RelativeLayout

    override fun setContentViewId(): Int {
        return R.layout.fragment_charts
    }

    override fun setUpView() {
        huoYueBang = mRootView!!.findViewById(R.id.rl_hy_bang) as RelativeLayout
        renQiBang = mRootView!!.findViewById(R.id.rl_rq_bang) as RelativeLayout
        shouRuBang = mRootView!!.findViewById(R.id.rl_sr_bang) as RelativeLayout
        haoQiBang = mRootView!!.findViewById(R.id.rl_hq_bang) as RelativeLayout

        huoYueBang.setOnClickListener {
            ShowBang.start(mContext!!, 0)
        }
        renQiBang.setOnClickListener {
            ShowBang.start(mContext!!, 1)
        }
        shouRuBang.setOnClickListener {
            ShowBang.start(mContext!!, 2)
        }
        haoQiBang.setOnClickListener {
            ShowBang.start(mContext!!, 3)
        }
    }

    override fun init() {
        loadHuoYueBang()
        loadRenQiBang()
        loadShouRuBang()
        loadHaoQiBang()
    }

    fun loadHuoYueBang() {
        SendRequest.getHuoYueBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(hy_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_hy_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(hy_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_hy_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(hy_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_hy_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        showToast(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                showToast("获取美力榜失败")
            }

        })
    }

    fun loadShouRuBang() {
        SendRequest.getShouRuBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = mGson!!.fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(sr_1_pic, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            fl_sr_1.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list.get(1)
                            ImageLoaderUtils.instance.loadBitmap(sr_2_pic, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            fl_sr_2.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list.get(2)
                            ImageLoaderUtils.instance.loadBitmap(sr_3_pic, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            fl_sr_3.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        showToast(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                showToast("获取美力榜失败")
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
                        showToast(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                showToast("获取美力榜失败")
            }

        })
    }

    fun loadHaoQiBang() {
        SendRequest.getHaoQiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, object :
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
                        showToast(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                showToast("获取美力榜失败")
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
