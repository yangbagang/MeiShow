package com.ybg.app.meishow.activity.user

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.BangItem
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.UserBase
import com.ybg.app.base.bean.YueShow
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.MainActivity
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.meishow.adapter.HomeShowAdapter
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.meishow.view.CircleImageView
import com.ybg.app.meishow.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.meishow.view.bgarefresh.BGARefreshLayout
import kotlinx.android.synthetic.main.activity_person_center_listview.*
import java.util.*

/**
 * 类描述：个人中心
 */
class PersonCenterActivity : BaseActivity(), View.OnClickListener {

    private var vDynamic_float: View? = null
    private var tvDynamic_float: TextView? = null
    private var tvDynamicNum_float: TextView? = null

    private var rl_user_wall: RelativeLayout? = null//照片背景
    private var iv_user_logo: CircleImageView? = null//用户头像
    private var tv_nickname: TextView? = null
    private var tv_sign_name: TextView? = null// 昵称 用户等级 用户等级名称 用户的个性签名
    private var tv_care: TextView? = null
    private var tv_fans: TextView? = null//美力值 关注 粉丝
    private var iv_miai_1: CircleImageView? = null
    private var iv_miai_2: CircleImageView? = null
    private var iv_miai_3: CircleImageView? = null
    private var rv_miai_1: RelativeLayout? = null
    private var rv_miai_2: RelativeLayout? = null
    private var rv_miai_3: RelativeLayout? = null
    private var tv_go_aimi: TextView? = null//跳转到密爱页面

    private var user: UserBase? = null

    private var hasMore = true
    private val pageSize = 5//每页取5条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var mAdapter: HomeShowAdapter
    private var userShowList: MutableList<YueShow> = ArrayList()

    override fun setContentViewId(): Int {
        return R.layout.activity_person_center_listview
    }

    override fun setUpView() {
        vDynamic_float = findViewById(R.id.v_dynamic)
        tvDynamic_float = findViewById(R.id.tv_dynamic) as TextView
        tvDynamicNum_float = findViewById(R.id.tv_dynamic_num) as TextView

        mContext = this@PersonCenterActivity
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val headerView = inflater.inflate(R.layout.item_user_list_head, null)
        val floatView = inflater.inflate(R.layout.item_list_user_floating_bar2, lv_user, false)
        lv_user.addHeaderView(headerView)
        lv_user.addHeaderView(floatView)
        initHeadFloatView(floatView)
        initHeadview(headerView)
        lv_user.setHeaderDividersEnabled(false)
        lv_user.setOnScrollListener(scrollListener)

        rl_fresh_layout.setRefreshViewHolder(BGANormalRefreshViewHolder(mContext!!, true))
        rl_fresh_layout.setDelegate(mDelegate)
    }

    override fun init() {
        setCustomTitle("个人中心")

        mAdapter = HomeShowAdapter(mContext!!)
        mAdapter.setDataList(userShowList)
        lv_user.adapter = mAdapter
    }

    /**
     * @param headview 初始化headerview
     */
    private fun initHeadview(headview: View) {
        rl_user_wall = headview.findViewById(R.id.rl_user_wall) as RelativeLayout
        iv_user_logo = headview.findViewById(R.id.iv_user_logo) as CircleImageView
        tv_nickname = headview.findViewById(R.id.tv_nickname) as TextView
        tv_sign_name = headview.findViewById(R.id.tv_sign_name) as TextView
        tv_care = headview.findViewById(R.id.tv_care) as TextView
        tv_fans = headview.findViewById(R.id.tv_fans) as TextView
        iv_miai_1 = headview.findViewById(R.id.iv_mi_ai_first) as CircleImageView
        iv_miai_2 = headview.findViewById(R.id.iv_mi_ai_second) as CircleImageView
        iv_miai_3 = headview.findViewById(R.id.iv_mi_ai_third) as CircleImageView
        rv_miai_1 = headview.findViewById(R.id.rl_mi_ai_first) as RelativeLayout
        rv_miai_2 = headview.findViewById(R.id.rl_mi_ai_second) as RelativeLayout
        rv_miai_3 = headview.findViewById(R.id.rl_mi_ai_third) as RelativeLayout
        tv_go_aimi = headview.findViewById(R.id.tv_go_mi_ai) as TextView
        tv_go_aimi!!.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        loadUserBase { userBase ->
            setUserInfo(userBase)
        }
    }

    private fun setUserInfo(userBase: UserBase) {
        user = userBase
        //头像
        val utils = ImageLoaderUtils.instance
        if (TextUtils.isEmpty(userBase.avatar)) {
            //需要修改默认头像时修改此处
            //utils.loadBitmap(userImage, R.mipmap.ic_default_girl);
        } else {
            utils.loadBitmap(iv_user_logo!!, HttpUrl.getImageUrl(userBase.avatar))
        }
        if (TextUtils.isEmpty(userBase.avatarBG)) {
            //navHeader.setBackgroundResource(R.drawable.side_nav_bar);
        } else {
            val bitmap = utils.loadBitmap(userBase.avatarBG)
            rl_user_wall?.background = BitmapDrawable(resources, bitmap)
        }
        //呢称
        tv_nickname?.text = userBase.nickName
        //签名
        tv_sign_name?.text = userBase.ymMemo
        //粉丝数
        loadFansNum(userBase)
        //关注数
        loadFollowNum(userBase)
        //蜜爱
        loadMiAi(userBase)
        //加载用户动态
        rl_fresh_layout.beginRefreshing()
        //加载动态数量
        getUserShowNum()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_center, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            //跳转到设置页面
            UserSettingActivity.start(mContext!!)
            return true
        } else if (id == R.id.action_exit) {
            mApplication.token = ""
            MainActivity.instance?.removeUserInfo()
            finish()
            return true
        } else if (id == R.id.action_me) {
            //跳转到用户资料
            MyInformationActivity.start(mContext!!)
            return true
        } else if (id == R.id.action_account) {
            UserAccountActivity.start(mContext!!)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private var vDynamic: View? = null
    private var tvDynamic: TextView? = null
    private var tvDynamicNum: TextView? = null
    /**
     * @param floatview
     */
    private fun initHeadFloatView(floatview: View) {
        /*动态*/
        vDynamic = floatview.findViewById(R.id.v_dynamic) as View
        tvDynamic = floatview.findViewById(R.id.tv_dynamic) as TextView
        tvDynamicNum = floatview.findViewById(R.id.tv_dynamic_num) as TextView
    }

    //@OnClick({R.id.rl_dynamic, R.id.rl_join_Data, R.id.rl_achievement})
    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_go_mi_ai -> {
                if (user != null) {
                    MiAiActivity.start(mContext!!, user!!.id, user!!.nickName)
                }
            }
        }
    }

    /**
     * listview 滑动监听
     */
    internal var scrollListener: AbsListView.OnScrollListener = object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(absListView: AbsListView, i: Int) {
        }

        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            if (firstVisibleItem >= 1) {
                ll_floating!!.visibility = View.VISIBLE
            } else {
                ll_floating!!.visibility = View.GONE
            }
        }
    }

    private fun loadFansNum(userBase: UserBase) {
        SendRequest.getFansNum(mContext!!, userBase.id, object : OkCallback<String>
        (OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    runOnUiThread {
                        tv_fans?.text = jsonBean.data
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                //不理会
            }
        })
    }

    private fun loadFollowNum(userBase: UserBase) {
        SendRequest.getFollowNum(mContext!!, userBase.id, object : OkCallback<String>
        (OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    runOnUiThread {
                        tv_care?.text = jsonBean.data
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                //不理会
            }
        })
    }

    private fun loadMiAi(userBase: UserBase) {
        SendRequest.getMiAiBang(mContext!!, "2016-01-01", "2999-12-31", 1, 3, userBase.id, object :
                OkCallback<String>(OkStringParser()){

            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val list = GsonUtil.createGson().fromJson<List<BangItem>>(jsonBean.data, object :
                            TypeToken<List<BangItem>>(){}.type)
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val first = list.first()
                            ImageLoaderUtils.instance.loadBitmap(iv_miai_1!!, HttpUrl.getImageUrl
                            (first.avatar))
                        } else {
                            rv_miai_1!!.visibility = View.GONE
                        }
                        if (list.size > 1) {
                            val second = list[1]
                            ImageLoaderUtils.instance.loadBitmap(iv_miai_2!!, HttpUrl.getImageUrl
                            (second.avatar))
                        } else {
                            rv_miai_2!!.visibility = View.GONE
                        }
                        if (list.size > 2) {
                            val third = list[2]
                            ImageLoaderUtils.instance.loadBitmap(iv_miai_3!!, HttpUrl.getImageUrl
                            (third.avatar))
                        } else {
                            rv_miai_3!!.visibility = View.GONE
                        }
                    }
                } else {
                    jsonBean?.let {
                        showToast(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                //ToastUtil.show("获取美力榜失败")
            }

        })
    }

    /**
     * 模拟请求网络数据
     */
    private val mShowHandler = object : Handler() {
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
                TYPE_REFRESH -> {
                    rl_fresh_layout.endRefreshing()
                    userShowList.clear()
                    userShowList.addAll(list)
                }
                TYPE_LOADMORE -> {
                    rl_fresh_layout.endLoadingMore()
                    userShowList.addAll(list)
                }
            }
            mAdapter.setDataList(userShowList)
            mAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 监听 刷新或者上拉
     */
    private val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            pageNum = 1
            getUserShowList()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                getUserShowList()
            } else {
                showToast("没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
    }

    private fun getUserShowList() {
        if (user == null) {
            return
        }
        SendRequest.getUserShowList(mContext!!, user!!.id.toInt(), pageNum, pageSize,object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                if (pageNum == 1) {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_REFRESH
                    message.obj = response
                    mShowHandler.sendMessage(message)
                } else {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_LOADMORE
                    message.obj = response
                    mShowHandler.sendMessage(message)
                }
            }

            override fun onFailure(e: Throwable) {
                rl_fresh_layout.endRefreshing()
            }
        })
    }

    private fun getUserShowNum() {
        if (user == null) {
            return
        }
        SendRequest.getUserShowNum(mContext!!, user!!.id, object : OkCallback<String> (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    runOnUiThread {
                        tvDynamicNum?.text = jsonBean.data
                        tvDynamicNum_float?.text = jsonBean.data
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                //do nothing
            }
        })
    }

    companion object {

        /**
         * @param context 跳转到本页面
         */
        fun start(context: Context) {
            val starter = Intent(context, PersonCenterActivity::class.java)
            context.startActivity(starter)
        }
    }
}
