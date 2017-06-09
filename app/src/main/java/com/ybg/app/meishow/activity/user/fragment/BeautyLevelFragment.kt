package com.ybg.app.meishow.activity.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ybg.app.meishow.R
import com.ybg.app.meishow.activity.base.BaseFragment

class BeautyLevelFragment : BaseFragment() {

    override fun setContentViewId(): Int {
        return R.layout.fragment_meilizhi_level
    }

    override fun setUpView() {

    }

    override fun init() {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {

        var inst: BeautyLevelFragment? = null
        fun getInstance(): BeautyLevelFragment {
            if (inst == null) {
                inst = BeautyLevelFragment()
            }
            return inst!!
        }
    }

}
