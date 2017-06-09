package com.ybg.app.meishow.adapter

import android.content.Context
import com.ybg.app.meishow.adapter.RecyclerBaseAdapter
import com.ybg.app.base.bean.UserBase
import com.ybg.app.meishow.R
import com.ybg.app.base.http.HttpUrl
import com.ybg.app.meishow.utils.ImageLoaderUtils
import com.ybg.app.meishow.view.CircleImageView

/**
 * Created by yangbagang on 2017/1/26.
 */
class UserAvatarAdapter : RecyclerBaseAdapter<UserBase> {

    private var userAvatar: CircleImageView? = null

    constructor(context: Context) : super(context) {
    }

    override val rootResource: Int
        get() = R.layout.list_item_user_avatar

    override fun getView(viewHolder: RecyclerBaseAdapter<UserBase>.BaseViewHolder, item: UserBase?, position: Int) {
        userAvatar = viewHolder.getView(R.id.userAvatar)
        if (item != null && userAvatar != null) {
            val avatar = HttpUrl.getImageUrl(item.avatar)
            ImageLoaderUtils.instance.loadBitmap(userAvatar!!, avatar)

        }
        viewHolder.setIsRecyclable(false)
    }

}