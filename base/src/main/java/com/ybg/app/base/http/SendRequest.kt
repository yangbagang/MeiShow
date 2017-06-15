package com.ybg.app.base.http

import android.content.Context
import android.util.Pair
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.listener.UploadListener
import java.io.File

object SendRequest {

    /**
     * 1.1获取验证码
     *
     * @param mobile 手机号
     */
    fun getCaptcha(tag: Context, mobile: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile)
        OkHttpProxy.post(HttpUrl.getCaptchaUrl, tag, params, callback)
    }

    /**
     * 1.2验证码校验
     *
     * @param mobile 手机号
     * @param captcha  验证码的值
     */

    fun checkCaptcha(tag: Context, mobile: String, captcha: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "captcha" to captcha)
        OkHttpProxy.post(HttpUrl.checkCaptchaUrl, tag, params, callback)
    }

    /**
     * 1.3用户注册接口
     *
     * @param mobile 手机号
     * @param password  密码
     */
    fun userRegister(tag: Context, mobile: String, password: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "password" to password)
        OkHttpProxy.post(HttpUrl.userRegisterUrl, tag, params, callback)
    }


    /**
     * 1.4 用户登录接口
     *
     * @param mobile 手机号
     * @param password 密码
     */
    fun userLogin(tag: Context, mobile: String, password: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "password" to password)
        OkHttpProxy.post(HttpUrl.userLoginUrl, tag, params, callback)
    }

    /**
     * 1.5 获取用户基本信息
     *
     * @param token
     */
    fun getUserBase(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.userBaseUrl, tag, params, callback)
    }

    fun updateUserBase(tag: Context, token: String, nickName: String, avatar: String, ymMemo: String,
                       callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "nickName" to nickName, "avatar" to avatar, "ymMemo" to ymMemo)
        OkHttpProxy.post(HttpUrl.updateUserBaseUrl, tag, params, callback)
    }

    /**
     * 1.6 获取用户个性化信息
     *
     * @param token
     */
    fun getUserInfo(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.userInfoUrl, tag, params, callback)
    }

    fun updateUserInfo(tag: Context, token: String, birthday: String, position: String,
                       bodyHigh: Int, bodyWeight: Int, cupSize: String, bust: Int, waist: Int,
                       hips: Int, province: String, city: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "birthday" to birthday,
                "position" to position, "bodyHigh" to "$bodyHigh", "bodyWeight" to "$bodyWeight", "cupSize"
                to cupSize, "bust" to "$bust", "waist" to "$waist", "hips" to "$hips", "province" to
                province, "city" to city)
        OkHttpProxy.post(HttpUrl.updateUserInfoUrl, tag, params, callback)
    }

    fun getUserId(tag: Context, ymCode: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("ymCode" to ymCode)
        OkHttpProxy.post(HttpUrl.userIdUrl, tag, params, callback)
    }

    /**
     * 1.7 补充用户个性化信息
     *
     * @param token
     */
    fun completeUserInfo(tag: Context, token: String, birthday: String, nickName: String,
                         sex: String, avatar: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "birthday" to birthday,
                "nickName" to nickName, "sex" to sex, "avatar" to avatar)
        OkHttpProxy.post(HttpUrl.userCompleteUrl, tag, params, callback)
    }

    /**
     * 获取关注数
     */
    fun getFollowNum(tag: Context, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, Long>("userId" to userId)
        OkHttpProxy.post(HttpUrl.followNumUrl, tag, params, callback)
    }

    /**
     * 获取粉丝数
     */
    fun getFansNum(tag: Context, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, Long>("userId" to userId)
        OkHttpProxy.post(HttpUrl.fansNumUrl, tag, params, callback)
    }


    fun getUserLabel(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.getUserLabelUrl, tag, params, callback)
    }

    fun updateUserLabel(tag: Context, token: String, labels: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "labels" to labels)
        OkHttpProxy.post(HttpUrl.updateUserLabelUrl, tag, params, callback)
    }

    fun updateAppToken(tag: Context, userToken: String, appToken: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("userToken" to userToken, "appToken" to appToken)
        OkHttpProxy.post(HttpUrl.updateClientIdUrl, tag, params, callback)
    }

    /**
     * 2.1 获取美秀列表
     *
     * @param pageNum  第几页
     * @param pageSize  每页显示多少条
     * @param type  1最新 2最热
     */
    fun getShowList(tag: Context, pageNum: Int, pageSize: Int, type: Int, callback: OkCallback<*>) {
        val params = mapOf<String, Int>("pageNum" to pageNum, "pageSize" to pageSize, "type" to type)
        OkHttpProxy.post(HttpUrl.liveListUrl, tag, params, callback)
    }

    fun getUserShowList(tag: Context, userId: Int, pageNum: Int, pageSize: Int, callback: OkCallback<*>) {
        val params = mapOf<String, Int>("pageNum" to pageNum, "pageSize" to pageSize, "userId" to userId)
        OkHttpProxy.post(HttpUrl.userLiveListUrl, tag, params, callback)
    }

    fun getUserShowNum(tag: Context, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, Long>("userId" to userId)
        OkHttpProxy.post(HttpUrl.userLiveNumUrl, tag, params, callback)
    }

    /**
     * 获取发布者信息
     */
    fun getAuthorInfo(tag: Context, showId: Long, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token)
        OkHttpProxy.post(HttpUrl.authorInfoUrl, tag, params, callback)
    }

    /**
     * 获取附件信息
     */
    fun getShowFiles(tag: Context, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId")
        OkHttpProxy.post(HttpUrl.showFilesUrl, tag, params, callback)
    }

    /**
     * 获取评论信息信息
     */
    fun getShowPingList(tag: Context, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId")
        OkHttpProxy.post(HttpUrl.showPingUrl, tag, params, callback)
    }

    /**
     * 获取点赞列表信息
     */
    fun getShowZanList(tag: Context, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId")
        OkHttpProxy.post(HttpUrl.showZanUrl, tag, params, callback)
    }

    /**
     * 获取分享列表信息
     */
    fun getShowShareList(tag: Context, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId")
        OkHttpProxy.post(HttpUrl.showShareUrl, tag, params, callback)
    }

    /**
     * 2.2 新建美秀
     *
     * @param token
     * @param thumbnail
     * @param title
     * @param type 1 图片 2 视频
     */
    fun createShow(tag: Context, token: String, thumbnail: String,
                   title: String, type: String, price: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "thumbnail" to thumbnail,
                "title" to title, "type" to type, "price" to price)
        OkHttpProxy.post(HttpUrl.createLiveUrl, tag, params, callback)
    }

    fun addFiles(tag: Context, showId: String, files: String, type: String, callback:
    OkCallback<*>) {
        val params = mapOf<String, String>("showId" to showId, "fileIds" to files, "type" to type)
        OkHttpProxy.post(HttpUrl.appendFileUrl, tag, params, callback)
    }

    fun addEvents(tag: Context, showId: String, events: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to showId, "eventIds" to events)
        OkHttpProxy.post(HttpUrl.appendEventUrl, tag, params, callback)
    }

    /**
     * 美秀详情
     *
     * @param token
     * @param showId
     * @param content
     */
    fun viewLive(tag: Context, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId")
        OkHttpProxy.post(HttpUrl.showDetailUrl, tag, params, callback)
    }

    /**
     * 评论美秀
     *
     * @param token
     * @param showId
     * @param content
     */
    fun pingLive(tag: Context, token: String, showId: Long, content: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token, "content" to content)
        OkHttpProxy.post(HttpUrl.pingLiveUrl, tag, params, callback)
    }

    /**
     * 点赞
     *
     * @param token
     * @param showId
     */
    fun zanLive(tag: Context, token: String, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token)
        OkHttpProxy.post(HttpUrl.zanLiveUrl, tag, params, callback)
    }

    /**
     * 分享美秀
     *
     * @param token
     * @param showId
     */
    fun shareLive(tag: Context, token: String, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token)
        OkHttpProxy.post(HttpUrl.shareLiveUrl, tag, params, callback)
    }

    fun checkPayStatus(tag: Context, token: String, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token)
        OkHttpProxy.post(HttpUrl.checkShowPayStatusUrl, tag, params, callback)
    }

    fun payForShow(tag: Context, token: String, showId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("showId" to "$showId", "token" to token)
        OkHttpProxy.post(HttpUrl.payForShowUrl, tag, params, callback)
    }

    /**
     * 2.7 获得板块列表
     */
    fun getCategoryList(tag: Context, callback: OkCallback<*>) {
        val params = emptyMap<String, String>()
        OkHttpProxy.get(HttpUrl.categoryListUrl, tag, params, callback)
    }

    /**
     * 2.8 获得话题列表
     */
    fun getTopicList(tag: Context, callback: OkCallback<*>) {
        val params = emptyMap<String, String>()
        OkHttpProxy.post(HttpUrl.topicListUrl, tag, params, callback)
    }

    /**
     * 获取关注列表
     */
    fun getFollowList(tag: Context, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, Long>("userId" to userId)
        OkHttpProxy.post(HttpUrl.followListUrl, tag, params, callback)
    }

    /**
     * 关注
     */
    fun followUser(tag: Context, token: String, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("userId" to "$userId", "token" to token)
        OkHttpProxy.post(HttpUrl.followUserUrl, tag, params, callback)
    }

    fun checkFollowStatus(tag: Context, token: String, userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("userId" to "$userId", "token" to token)
        OkHttpProxy.post(HttpUrl.checkFollowStatusUrl, tag, params, callback)
    }

    /**
     * 上传图片文件
     */
    fun uploadPicFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_PIC_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 上传视频文件
     */
    fun uploadVideoFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_VIDEO_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 3.1 活跃榜
     */
    fun getHuoYueBang(tag: Context, beginTime: String, endTime: String, pageNum: Int, pageSize: Int,
                      callback: OkCallback<*>) {
        val params = mapOf<String, String>("beginTime" to beginTime, "endTime" to endTime,
                "pageNum" to "$pageNum", "pageSize" to "$pageSize")
        OkHttpProxy.post(HttpUrl.huoYueBang, tag, params, callback)
    }

    /**
     * 3.2 人气榜
     */
    fun getRenQiBang(tag: Context, beginTime: String, endTime: String, pageNum: Int, pageSize: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("beginTime" to beginTime, "endTime" to endTime,
                "pageNum" to "$pageNum", "pageSize" to "$pageSize")
        OkHttpProxy.post(HttpUrl.renQiBang, tag, params, callback)
    }

    /**
     * 3.3 收入榜
     */
    fun getShouRuBang(tag: Context, beginTime: String, endTime: String, pageNum: Int, pageSize: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("beginTime" to beginTime, "endTime" to endTime,
                "pageNum" to "$pageNum", "pageSize" to "$pageSize")
        OkHttpProxy.post(HttpUrl.shouRuBang, tag, params, callback)
    }

    /**
     * 3.4 豪气榜
     */
    fun getHaoQiBang(tag: Context, beginTime: String, endTime: String, pageNum: Int, pageSize: Int,
                      callback: OkCallback<*>) {
        val params = mapOf<String, String>("beginTime" to beginTime, "endTime" to endTime,
                "pageNum" to "$pageNum", "pageSize" to "$pageSize")
        OkHttpProxy.post(HttpUrl.haoQiBang, tag, params, callback)
    }

    fun getMiAiBang(tag: Context, beginTime: String, endTime: String, pageNum: Int, pageSize: Int,
                    userId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, String>("beginTime" to beginTime, "endTime" to endTime,
                "pageNum" to "$pageNum", "pageSize" to "$pageSize", "userId" to "$userId")
        OkHttpProxy.post(HttpUrl.miAiBang, tag, params, callback)
    }

    fun getAccountInfo(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.accountInfo, tag, params, callback)
    }

    /**
     * 5.1 获得充值卡
     */
    fun getCardList(tag: Context, callback: OkCallback<*>) {
        val params = emptyMap<String, String>()
        OkHttpProxy.post(HttpUrl.getCardListUrl, tag, params, callback)
    }

    /**
     * 5.2 创建订单
     */
    fun createOrder(tag: Context, token: String, cardId: Long, callback: OkCallback<*>) {
        val params = mapOf<String, Any>("token" to token, "cardId" to cardId)
        OkHttpProxy.post(HttpUrl.createOrderUrl, tag, params, callback)
    }

    /**
     * 5.3 查询订单状态
     */
    fun checkOrderStatus(tag: Context, orderNo: String, callback: OkCallback<*>) {
        val params = mapOf<String, Any>("orderNo" to orderNo)
        OkHttpProxy.post(HttpUrl.checkOrderStatusUrl, tag, params, callback)
    }

    /**
     * 5.4 创建交易对象
     */
    fun createCharge(tag: Context, orderNo: String, payType: String,callback: OkCallback<*>) {
        val params = mapOf("orderNo" to orderNo, "payType" to payType)
        OkHttpProxy.post(HttpUrl.createChargeUrl, tag, params, callback)
    }

}
