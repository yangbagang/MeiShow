package com.ybg.app.meishow.activity.gift

import android.app.Activity
import android.content.Intent
import com.pingplusplus.android.Pingpp
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.meishow.R
import com.ybg.app.base.base.BaseActivity
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.app.ShowApplication
import kotlinx.android.synthetic.main.activity_card_payment.*

class CardPaymentActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    override fun setContentViewId(): Int {
        return R.layout.activity_card_payment
    }

    override fun setUpView() {
        setCustomTitle("选择支付方式")//1:支付宝 2:微信支付
    }

    override fun init() {
        instance = this@CardPaymentActivity
        if (intent != null) {
            val money = intent.extras.getFloat("money")
            val orderNo = intent.extras.getString("orderNo")
            tv_money.text = "共需支付${money}元"
            tv_wx_pay.setOnClickListener {
                createCharge(orderNo, "2")
            }
            tv_zfb_pay.setOnClickListener {
                createCharge(orderNo, "1")
            }
        }
    }

    private fun createCharge(orderNo: String, payWay: String) {
        SendRequest.createCharge(mContext!!, orderNo, payWay, object : OkCallback<String>(OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    Pingpp.createPayment(this@CardPaymentActivity, jsonBean.data)
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                e.printStackTrace()
                ToastUtil.show(showApplication, "操作失败")
            }
        })
    }

    fun payFinish() {
        val response = Intent(mContext, CardListActivity::class.java)
        response.putExtra("payResult", true)
        setResult(Activity.RESULT_OK, response)
        finish()
    }

    companion object {

        var instance: CardPaymentActivity? = null

        fun start(context: Activity, orderNo: String, money: Float) {
            val starter = Intent(context, CardPaymentActivity::class.java)
            starter.putExtra("orderNo", orderNo)
            starter.putExtra("money", money)
            context.startActivityForResult(starter, IntentExtra.RequestCode.REQUEST_ZHI_FU)
        }
    }
}
