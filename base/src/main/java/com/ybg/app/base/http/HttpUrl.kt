package com.ybg.app.base.http

import com.ybg.app.base.constants.AppConstant

/**
 * 网络请求相关设置,配置请求地址及参数
 */
object HttpUrl {

    //开发服务器地址
    val API_HOST_DEBUG = "http://192.168.12.99:8080/mShow"
    //生产服务器地址
    val API_HOST_PRODUCT = "http://139.224.186.241:8080/mShow"
    //上传
    val FILE_SERVER_UPLOAD = "http://139.224.186.241:8080/fileserver/file/upload3"
    //预览
    val FILE_SERVER_PREVIEW = "http://139.224.186.241:8080/fileserver/file/preview"
    //下载
    val FILE_SERVER_DOWNLOAD = "http://139.224.186.241:8080/fileserver/file/download"

    val ROOT_URL = if (AppConstant.isDebug) API_HOST_DEBUG else API_HOST_PRODUCT
    //获取验证码
    private val GET_CAPTCHA = "/system/getCaptcha"
    //验证验证码
    private val CHECK_CAPTCHA = "/system/checkCaptcha"
    //用户注册
    private val USER_REGISTER = "/userBase/register"
    //用户登录
    private val USER_LOGIN = "/userBase/login"
    //用户基本信息
    private val GET_USER_BASE = "/userBase/getUserBase"
    private val UPDATE_USER_BASE = "/userBase/updateUserBase"
    //用户个性化信息
    private val GET_USER_INFO = "/userInfo/getUserInfo"
    private val UPDATE_USER_INFO = "/userInfo/updateUserInfo"
    //用户个性化信息
    private val COMPLETE_USER_INFO = "/userBase/completeData"
    //用户爱好
    private val GET_USER_LABEL = "/userLabel/list"
    private val UPDATE_USER_LABEL = "/userLabel/update"
    private val UPDATE_CLIENT_ID = "/userBase/updateAppToken"
    /*美秀列表*/
    private val LIVE_LIST = "/ruiShow/list"
    private val USER_LIVE_LIST = "/ruiShow/listByUserId"
    private val USER_LIVE_NUM = "/ruiShow/getShowNumByUserId"
    private val AUTHOR_INFO = "/userBase/getAuthorInfoByShowId"
    private val SHOW_FIELS = "/showFile/list"
    private val SHOW_PING_LIST = "/showPing/list"
    private val SHOW_ZAN_LIST = "/showZan/list"
    private val SHOW_SHARE_LIST = "/showShare/list"
    /*新建美秀*/
    private val CREATE_LIVE = "/ruiShow/create"
    private val APPEND_FILES = "/showFile/addFiles"
    private val APPEND_EVENTS = "/ruiEvent/appendEvent"
    private val LIVE_DETAIL = "/ruiShow/detail"
    /*评论美秀*/
    private val PING_LIVE = "/ruiShow/ping"
    /*点赞*/
    private val ZAN_LIVE = "/ruiShow/zan"
    /*分享*/
    private val SHARE_LIVE = "/ruiShow/share"
    /*美秀板块*/
    private val CATEGORY_LIST = "/ruiBar/list"
    /*活动主题*/
    private val TOPIC_LIST = "/ruiEvent/list"
    private val PING_LIST = "/showPing/list"

    private val FOLLOW_LIST = "/follow/list"
    private val FOLLOW_FOLLOW = "/follow/follow"

    //悦美榜
    val ruiMeiBang: String
        get() = ROOT_URL + "/meiLiHistory/meiliList"
    val renQiBang: String
        get() = ROOT_URL + "/meiLiHistory/renqiList"
    val huoLiBang: String
        get() = ROOT_URL + "/meiLiHistory/huoliList"
    val miAiBang: String
        get() = ROOT_URL + "/meiLiHistory/miAiList"

    //用户模块
    val userLoginUrl: String
        get() = ROOT_URL + USER_LOGIN

    val getCaptchaUrl: String
        get() = ROOT_URL + GET_CAPTCHA

    val checkCaptchaUrl: String
        get() = ROOT_URL + CHECK_CAPTCHA

    val userRegisterUrl: String
        get() = ROOT_URL + USER_REGISTER

    val userBaseUrl: String
        get() = ROOT_URL + GET_USER_BASE

    val userIdUrl: String
        get() = ROOT_URL + "/userBase/getUserIdByYmCode"

    val updateUserBaseUrl: String
        get() = ROOT_URL + UPDATE_USER_BASE

    val userInfoUrl: String
        get() = ROOT_URL + GET_USER_INFO

    val updateUserInfoUrl: String
        get() = ROOT_URL + UPDATE_USER_INFO

    val userCompleteUrl: String
        get() = ROOT_URL + COMPLETE_USER_INFO

    val getUserLabelUrl: String
        get() = ROOT_URL + GET_USER_LABEL

    val updateUserLabelUrl: String
        get() = ROOT_URL + UPDATE_USER_LABEL

    val updateClientIdUrl: String
        get() = ROOT_URL + UPDATE_CLIENT_ID

    val liveListUrl: String
        get() = ROOT_URL + LIVE_LIST

    val userLiveListUrl: String
        get() = ROOT_URL + USER_LIVE_LIST

    val userLiveNumUrl: String
        get() = ROOT_URL + USER_LIVE_NUM

    val showDetailUrl: String
        get() = ROOT_URL + LIVE_DETAIL

    val authorInfoUrl: String
        get() = ROOT_URL + AUTHOR_INFO

    val showFilesUrl: String
        get() = ROOT_URL + SHOW_FIELS

    val showPingUrl: String
        get() = ROOT_URL + SHOW_PING_LIST

    val showZanUrl: String
        get() = ROOT_URL + SHOW_ZAN_LIST

    val showShareUrl: String
        get() = ROOT_URL + SHOW_SHARE_LIST

    val followUserUrl: String
        get() = ROOT_URL + FOLLOW_FOLLOW
    val followListUrl: String
        get() = ROOT_URL + FOLLOW_LIST

    val followNumUrl: String
        get() = ROOT_URL + "/follow/getFollowNum"

    val fansNumUrl: String
        get() = ROOT_URL + "/follow/getFansNum"

    val checkShowPayStatusUrl: String
        get() = ROOT_URL + "/ruiShow/checkPayStatus"

    val payForShowUrl: String
        get() = ROOT_URL + "/ruiShow/payForShow"

    /**
     * @return 点赞URL
     */
    val zanLiveUrl: String
        get() = ROOT_URL + ZAN_LIVE

    val checkFollowStatusUrl: String
        get() = ROOT_URL + "/follow/checkFollowStatus"

    val createLiveUrl: String
        get() = ROOT_URL + CREATE_LIVE

    val appendFileUrl: String
        get() = ROOT_URL + APPEND_FILES

    val appendEventUrl: String
        get() = ROOT_URL + APPEND_EVENTS

    val topicListUrl: String
        get() = ROOT_URL + TOPIC_LIST

    val categoryListUrl: String
        get() = ROOT_URL + CATEGORY_LIST

    val shareLiveUrl: String
        get() = ROOT_URL + SHARE_LIVE

    val pingLiveUrl: String
        get() = ROOT_URL + PING_LIVE

    fun getImageUrl(fid: String): String {
        if (fid.startsWith("http:", true) || fid.startsWith("https:", true)) {
            return fid
        }
        //val path = Base64Util.getDecodeString(fid)
        //println("path=$path")
        return FILE_SERVER_PREVIEW + "/" + fid
    }

    fun getVideoUrl(fid: String): String {
        if (fid.startsWith("http:", true) || fid.startsWith("https:", true)) {
            return fid
        }
        //val path = Base64Util.getDecodeString(fid)
        //println("path=$path")
        return FILE_SERVER_DOWNLOAD + "/" + fid
    }

    val getCardListUrl: String
        get() = ROOT_URL + "/ruiCard/list"
    val createOrderUrl: String
        get() = ROOT_URL + "/orderInfo/createOrder"
    val checkOrderStatusUrl: String
        get() = ROOT_URL + "/orderInfo/queryOrderIsPay"
    val createChargeUrl: String
        get() = ROOT_URL + "/orderInfo/createPingPlusCharge"

}
