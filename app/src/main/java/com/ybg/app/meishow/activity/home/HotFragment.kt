package com.ybg.app.meishow.activity.home

import android.os.Handler
import android.os.Message
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.meishow.activity.base.BaseFragment
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.YueShow
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.adapter.HomeShowAdapter
import com.ybg.app.meishow.app.ShowApplication
import com.ybg.app.meishow.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.meishow.view.bgarefresh.BGARefreshLayout
import org.greenrobot.eventbus.Subscribe
import java.util.*

class HotFragment : BaseFragment() {

    private val showApplication = ShowApplication.instance!!

    private lateinit var mListView: ListView
    private lateinit var mRefreshLayout: BGARefreshLayout

    private var hasMore = true
    private val pageSize = 5//每页取5条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var mAdapter: HomeShowAdapter
    private var hotEntityList: MutableList<YueShow> = ArrayList()

    override fun setContentViewId(): Int {
        return R.layout.fragment_hall_show_hot
    }

    override fun setUpView() {
        mListView = mRootView!!.findViewById(R.id.rv_list_view) as ListView
        mRefreshLayout = mRootView!!.findViewById(R.id.rl_fresh_layout) as BGARefreshLayout

        mRefreshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(mContext!!, true))
        mRefreshLayout.setDelegate(mDelegate)
        mRefreshLayout.beginRefreshing()
    }

    override fun init() {
        mAdapter = HomeShowAdapter(mContext!!)
        mAdapter.setDataList(hotEntityList)
        mListView.adapter = mAdapter
    }

    @Subscribe
    override fun onEvent(event: MessageEvent) {
        super.onEvent(event)
        if (event.what == MessageEvent.MESSAGE_SHOW_POST && pageNum == 1 && !hasMore) {
            mRefreshLayout.beginRefreshing()
        }
    }

    /**
     * 模拟请求网络数据
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val gson = Gson()
            val jSonResultBean = JSonResultBean.fromJSON(msg.obj.toString())
            var list: List<YueShow> = ArrayList()
            if (jSonResultBean != null && jSonResultBean.isSuccess) {
                list = gson.fromJson<List<YueShow>>(jSonResultBean.data, object : TypeToken<List<YueShow>>() {

                }.type)
            }

            hasMore = list.size == pageSize

            when (msg.what) {
                0 -> {
                    mRefreshLayout.endRefreshing()
                    hotEntityList.clear()
                    hotEntityList.addAll(list)
                }
                1 -> {
                    mRefreshLayout.endLoadingMore()
                    hotEntityList.addAll(list)
                }
            }
            mAdapter.setDataList(hotEntityList)
            mAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 监听 刷新或者上拉
     */
    private val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            pageNum = 1
            getHotInfoList()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                getHotInfoList()
            } else {
                ToastUtil.show(showApplication, "没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
    }

    /**
     * @param context
     * *
     * @param type
     * *
     * @param start
     * *
     * @param count
     */
    private fun getHotInfoList() {
        SendRequest.getShowList(mContext!!, pageNum, pageSize, 2, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                if (pageNum == 1) {
                    val message = mHandler.obtainMessage()
                    message.what = TYPE_REFRESH
                    message.obj = response
                    mHandler.sendMessage(message)
                } else {
                    val message = mHandler.obtainMessage()
                    message.what = TYPE_LOADMORE
                    message.obj = response
                    mHandler.sendMessage(message)
                }
            }

            override fun onFailure(e: Throwable) {
                mRefreshLayout.endRefreshing()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        val instance: HotFragment = HotFragment()
    }
}
