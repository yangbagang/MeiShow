package com.ybg.app.meishow.activity.show

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.ybg.app.meishow.R
import kotlinx.android.synthetic.main.activity_photo_player.*

class PhotoPlayerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_photo_player)

        initPhotoList()
    }

    private fun initPhotoList() {
        if (intent != null) {
            val pics = intent.extras.getStringArrayList("pics")
            bf_photo_frame.enableFullScreen(false)
            bf_photo_frame.setImageResources(pics)
            bf_photo_frame.startPlay()
        }
    }

    companion object {

        fun start(context: Context, pics: ArrayList<String>) {
            val starter = Intent(context, PhotoPlayerActivity::class.java)
            starter.putStringArrayListExtra("pics", pics)
            context.startActivity(starter)
        }
    }
}
