package com.ybg.app.base.bean

import java.io.Serializable
import java.util.Date

/**
 * Created by yangbagang on 2016/10/27.
 */

class YueShow : Serializable {
    var id: Long? = null
    var thumbnail = ""//缩略图
    var title = ""//说明
    var createTime: String? = null//发布时间
    var updateTime: String? = null//结束时间
    var pingNum: Int = 0//评论次数
    var zanNum: Int = 0//赞次数
    var viewNum: Int = 0//分享次数
    var type: Int = 1//1图片2视频3直播
    var fileNum: Int = 1//附带文件数量
    var flag: Int = 1//是否有效
    var user: UserBase? = null
    val price: Int = 0//需要付多少钱才可以看

    override fun toString(): String {
        return "YueShow{" +
                "thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", pingNum=" + pingNum +
                ", zanNum=" + zanNum +
                ", shareNum=" + viewNum +
                '}'
    }

    companion object {

        private val serialVersionUID = -1080554695148177189L
    }
}
