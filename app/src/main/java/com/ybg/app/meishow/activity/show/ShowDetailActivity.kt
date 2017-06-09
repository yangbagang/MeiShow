package com.ybg.app.meishow.activity.show

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.base.bean.*
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.picasso.Picasso
import com.ybg.app.base.utils.*
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.user.UserCenterActivity
import com.ybg.app.meishow.adapter.PingItemAdapter
import com.ybg.app.meishow.app.ShowApplication
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.meishow.view.BannerFrame
import com.ybg.app.meishow.view.CircleImageView
import kotlinx.android.synthetic.main.activity_home_show_detail.*

/**
 * Created by yangbagang on 2016/12/5.
 */
class ShowDetailActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    private lateinit var show: YueShow
    //事件定义
    private val commentOnClickListener = BtnCommentOnClickListener()
    private val zanOnClickListener = BtnZanOnClickListener()
    private val photoOnClickListener = BtnPhotoOnClickListener()
    //UI组件
    private lateinit var authorAvatar: CircleImageView
    private lateinit var authorNickName: TextView
    private lateinit var authorLevel: ImageView
    private lateinit var authorMeiLi: TextView
    private lateinit var postTime: TextView
    private lateinit var authorBtn: Button
    private lateinit var showContent: TextView
    private lateinit var zanLayout: LinearLayout
    private lateinit var zanNum: TextView

    private lateinit var pingAdapter: PingItemAdapter
    private lateinit var pingRecyclerView: RecyclerView
    private lateinit var pingHeaderView: RecyclerViewHeader

    private var w = 0
    private var videoUrl: String? = null

    private var user: UserBase? = null

    override fun setContentViewId(): Int {
        return R.layout.activity_home_show_detail
    }

    override fun setUpView() {
        authorAvatar = findViewById(R.id.iv_user_logo) as CircleImageView
        authorNickName = findViewById(R.id.tv_user_name) as TextView
        authorLevel = findViewById(R.id.iv_level_img) as ImageView
        authorMeiLi = findViewById(R.id.tv_meilizhi) as TextView
        postTime = findViewById(R.id.tv_time) as TextView
        authorBtn = findViewById(R.id.btn_chat) as Button
        authorBtn.text = "约会"
        showContent = findViewById(R.id.tv_fu_content) as TextView
        zanLayout = findViewById(R.id.ll_user_like_list) as LinearLayout
        zanNum = findViewById(R.id.tv_like_num) as TextView
        pingRecyclerView = findViewById(R.id.recycleview) as RecyclerView
        pingHeaderView = findViewById(R.id.header) as RecyclerViewHeader

        w = ScreenUtils.getScreenWidth(mContext!!)

        setCustomTitle("查看悦秀")
    }

    override fun init() {
        if (intent != null) {
            val showItem = intent.getSerializableExtra("show")
            if (showItem is YueShow) {
                show = showItem
                if (show.price > 0) {
                    checkPayStatus()
                } else {
                    //开始展示美秀
                    displayRuiShow()
                }
                //click event
                iv_comment.setOnClickListener(commentOnClickListener)
                iv_like.setOnClickListener(zanOnClickListener)
                //初始化适配器
                pingAdapter = PingItemAdapter()
                pingRecyclerView.adapter = pingAdapter
                val layoutManager = LinearLayoutManager.VERTICAL
                pingRecyclerView.layoutManager = LinearLayoutManager(mContext!!, layoutManager, false)
                pingRecyclerView.itemAnimator = DefaultItemAnimator()
                pingRecyclerView.addItemDecoration(SpaceItemDecoration(2))
                pingHeaderView.attachTo(pingRecyclerView)

                et_comment_content.setOnFocusChangeListener { view, b ->
                    if (b) {
                        iv_comment.visibility = View.VISIBLE
                        iv_like.visibility = View.GONE
                    } else {
                        iv_comment.visibility = View.GONE
                        iv_like.visibility = View.VISIBLE
                    }
                }
            } else {
                //
            }
        } else {
            //
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentExtra.RequestCode.REQUEST_PAY_NOTICE && resultCode == Activity.RESULT_OK) {
            displayRuiShow()
        }
    }

    private fun checkPayStatus() {
        SendRequest.checkPayStatus(mContext!!, mApplication.token, show.id!!, object : OkCallback<String>(OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    if (jsonBean.data == "true") {
                        displayRuiShow()
                    } else if (jsonBean.data == "false") {
                        PayNoticeActivity.start(mContext!!, show)
                    }
                }
            }

            override fun onFailure(e: Throwable) {

            }
        })
    }

    private fun displayRuiShow() {
        getDetailInfo()
        /** 1图片2视频3直播  */
        if (show.type == 3) {
            initLiveView()
        } else if (show.type == 2) {
            initVideoView()
        } else if (show.type == 1) {
            initPicView()
        }

        //填充用户信息
        authorAvatar.setOnClickListener(photoOnClickListener)

        getAuthorInfo()
        //填充美秀信息
        if (!TextUtils.isEmpty(show.createTime)) {
            postTime.text = DateUtil.getTimeInterval(show.createTime!!)
        }
        /**用户发布内容 */
        showContent.text = show.title
        /**点赞用户 */
        zanNum.text = "${show.zanNum}"
        loadShowFiles()
        loadZanUserList()
        loadPingList()
    }

    private fun getAuthorInfo() {
        SendRequest.getAuthorInfo(mContext!!, show.id!!, mApplication.token, object :
                OkCallback<String>(OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val mGson = GsonBuilder().serializeNulls().create()
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    //成功
                    val userBase = mGson.fromJson(jsonBean.data, UserBase::class.java)
                    user = userBase
                    loadInfo(userBase)
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取用户信息失败。")
            }
        })
    }

    private fun loadInfo(userBase: UserBase) {
        /**用户信息 */
        authorMeiLi.text = String.format("美力值 + %d", userBase.ml)
        if (TextUtils.isEmpty(userBase.nickName)) {
            authorNickName.text = userBase.ymCode
        } else {
            authorNickName.text = userBase.nickName
        }
        if (TextUtils.isEmpty(userBase.avatar)) {
            Picasso.with(mContext).load(R.mipmap.default_avatar).resize(100, 100).centerCrop()
                    .into(authorAvatar)
        } else {
            Picasso.with(mContext).load(HttpUrl.getImageUrl(userBase.avatar)).resize(100, 100).centerCrop()
                    .into(authorAvatar)
        }
    }

    private fun initLiveView() {
        rl_video.visibility = View.VISIBLE
        ll_photo_video.visibility = View.GONE
    }

    private fun initPicView() {
        rl_video.visibility = View.GONE
        ll_photo_video.visibility = View.VISIBLE
    }

    private fun initVideoView() {
        rl_video.visibility = View.VISIBLE
        ll_photo_video.visibility = View.GONE
        ImageLoaderUtils.instance.loadBitmap(iv_video_thumbnail, HttpUrl.getImageUrl(show.thumbnail))

        iv_video_cover.setOnClickListener {
            if (videoUrl != null) {
                VideoPlayerActivity.start(mContext!!, videoUrl!!)
            }
        }
    }

    private fun loadShowFiles() {
        SendRequest.getShowFiles(mContext!!, show.id!!, object : OkCallback<String>
        (OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val files = mGson!!.fromJson<List<ShowFile>>(jsonBean.data, object :
                            TypeToken<List<ShowFile>>() {}.type)
                    val params = LinearLayout.LayoutParams(w, (w * 0.75).toInt())
                    if (show.type == 1) {
                        runOnUiThread {
                            ll_photo_video.removeAllViews()
                            val picFrame = BannerFrame(mContext!!)
                            picFrame.layoutParams = params
                            ll_photo_video.addView(picFrame)
                            val pics = files.map { it.file }
                            picFrame.setImageResources(pics)
                            picFrame.startPlay()
                        }
                    } else if (show.type == 2) {
                        if (files.isNotEmpty()) {
                            val first = files.first()
                            videoUrl = HttpUrl.getVideoUrl(first.file)
                            if (videoUrl != null && showApplication.isAutoPlay() &&
                                    NetworkUtil.getNetworkState(mContext!!) == NetworkType.WIFI) {
                                //wifi下自动播放
                                VideoPlayerActivity.start(mContext!!, videoUrl!!)
                            }
                        }
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {

            }
        })
    }

    private fun loadZanUserList() {
        SendRequest.getShowZanList(mContext!!, show.id!!, object : OkCallback<String>(OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val zanUserList = mGson!!.fromJson<List<ShowZan>>(jsonBean.data,
                            object : TypeToken<List<ShowZan>>() {}.type)
                    zanNum.text = "${zanUserList.size}"
                    val limitNum = Math.min(6, zanUserList.size)
                    zanLayout.removeAllViews()
                    if (limitNum == 0) {
                        // 还没有点赞用户，跳过。
                        return
                    }
                    for (i in 0..limitNum - 1) {
                        val imageView = CircleImageView(mContext!!)
                        zanLayout.addView(imageView)
                        Picasso.with(mContext).load(HttpUrl.getImageUrl(zanUserList[i].avatar))
                                .resize(96, 96).centerCrop().into(imageView)
                    }
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取点赞用户失败")
                e.printStackTrace()
            }
        })
    }

    private fun loadPingList() {
        SendRequest.getShowPingList(mContext!!, show.id!!, object : OkCallback<String>(OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val pingList = mGson!!.fromJson<List<ShowPing>>(jsonBean.data,
                            object : TypeToken<List<ShowPing>>() {}.type)
                    pingAdapter.setData(pingList)
                    pingAdapter.notifyDataSetChanged()
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取评论失败")
                e.printStackTrace()
            }
        })
    }

    private fun getDetailInfo() {
        SendRequest.viewLive(mContext!!, show.id!!, object : OkCallback<String>(OkStringParser()) {
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    //TODO
                } else {
                    jsonBean?.let {
                        println(jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                e.printStackTrace()
            }

        })
    }

    /**
     * 评论点击事件
     */
    private inner class BtnCommentOnClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            if (!mApplication.hasLogin()) {
                //未登录不能评论
                ToastUtil.show(showApplication, "你还没有登录，请登录后再发表评论。")
                return
            }
            iv_comment.isClickable = false
            val pingContent = et_comment_content.text.toString()
            hideKeyboard()//关闭键盘
            SendRequest.pingLive(mContext!!, mApplication.token, show.id!!, pingContent,
                    object : OkCallback<String>(OkStringParser()) {
                        override fun onSuccess(code: Int, response: String) {
                            val jsonBean = JSonResultBean.fromJSON(response)
                            if (jsonBean != null && jsonBean.isSuccess) {
                                loadPingList()
                                et_comment_content.text.clear()
                                ToastUtil.show(showApplication, "发表评论完成")
                                iv_comment.visibility = View.GONE
                                iv_like.visibility = View.VISIBLE
                            } else {
                                jsonBean?.let {
                                    ToastUtil.show(showApplication, jsonBean.message)
                                }
                            }
                            iv_comment.isClickable = true
                        }

                        override fun onFailure(e: Throwable) {
                            e.printStackTrace()
                            ToastUtil.show(showApplication, "评论失败")
                            iv_comment.isClickable = true
                        }
                    })
        }
    }

    private inner class BtnZanOnClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            if (!mApplication.hasLogin()) {
                ToastUtil.show(showApplication, "请登录后再点赞")
            } else {
                iv_like.isClickable = false
                SendRequest.zanLive(mContext!!, mApplication.token, show.id!!,
                        object : OkCallback<String>(OkStringParser()) {
                            override fun onSuccess(code: Int, response: String) {
                                val resultBean = JSonResultBean.fromJSON(response)
                                if (resultBean != null && resultBean.isSuccess) {
                                    zanNum.text = resultBean.data
                                    iv_like.isClickable = false
                                    loadZanUserList()
                                }
                            }

                            override fun onFailure(e: Throwable) {
                                ToastUtil.show(showApplication, "点赞失败")
                                iv_like.isClickable = true
                            }

                        })
            }
        }
    }

    /**
     * 头像点击事件
     */
    private inner class BtnPhotoOnClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            if (user != null) {
                UserCenterActivity.start(mContext!!, user!!)
            }
        }
    }

    companion object {

        fun start(context: Context, show: YueShow) {
            val starter = Intent(context, ShowDetailActivity::class.java)
            starter.putExtra("show", show)
            context.startActivity(starter)
        }
    }
}