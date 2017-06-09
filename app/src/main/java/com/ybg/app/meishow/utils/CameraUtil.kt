package com.ybg.app.meishow.utils

import com.ybg.app.base.utils.ScreenUtils
import com.ybg.app.meishow.app.ShowApplication

/**
 * Created by yangbagang on 2017/6/8.
 */
object CameraUtil {
    //获取自定义相机的宽度
    val cameraPhotoWidth: Int
        get() = ScreenUtils.getScreenWidth(ShowApplication.instance!!) / 4 - ResourcesUtils.dip2px(2)

    // 相机照片列表高度计算
    val cameraPhotoAreaHeight: Int
        get() = cameraPhotoWidth + ResourcesUtils.dip2px(4)
}