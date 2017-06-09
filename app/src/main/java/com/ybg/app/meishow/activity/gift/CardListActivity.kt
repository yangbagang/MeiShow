package com.ybg.app.meishow.activity.gift

import android.app.Activity
import android.content.Intent
import android.widget.ListView
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.RuiCard
import com.ybg.app.meishow.R
import com.ybg.app.base.base.BaseActivity
import com.ybg.app.meishow.adapter.CardItemAdapter
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.parser.OkStringParser
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.meishow.activity.show.PayNoticeActivity
import com.ybg.app.meishow.app.ShowApplication
import java.util.*

class CardListActivity : BaseActivity() {

    private val showApplication = ShowApplication.instance!!

    private lateinit var mListView: ListView
    private lateinit var mAdapter: CardItemAdapter
    private var cardList: MutableList<RuiCard> = ArrayList()

    override fun setContentViewId(): Int {
        return R.layout.activity_card_list
    }

    override fun setUpView() {
        setCustomTitle("充值中心")
    }

    override fun init() {
        mAdapter = CardItemAdapter(mContext!!)
        mAdapter.setData(cardList)
        mListView.adapter = mAdapter
        mListView.setOnItemClickListener { parent, view, position, id ->
            createOrderInfo(cardList[position])
        }

        getCardInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentExtra.RequestCode.REQUEST_ZHI_FU && resultCode == Activity
                .RESULT_OK) {
            val response = Intent(mContext, PayNoticeActivity::class.java)
            response.putExtra("czResult", true)
            setResult(Activity.RESULT_OK, response)
            finish()
        }
    }

    private fun getCardInfo() {
        SendRequest.getCardList(mContext!!, object : OkCallback<String>(OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val data = mGson!!.fromJson<List<RuiCard>>(jsonBean.data, object :
                            TypeToken<List<RuiCard>>(){}.type)
                    cardList.clear()
                    cardList.addAll(data)
                    mAdapter.setData(cardList)
                    mAdapter.notifyDataSetChanged()
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "获取数据失败")
            }
        })
    }

    private fun createOrderInfo(ruiCard: RuiCard) {
        SendRequest.createOrder(mContext!!, mApplication.token, ruiCard.id, object :
                OkCallback<String>(OkStringParser()){
            override fun onSuccess(code: Int, response: String) {
                val jsonBean = JSonResultBean.fromJSON(response)
                if (jsonBean != null && jsonBean.isSuccess) {
                    val orderNo = jsonBean.data
                    CardPaymentActivity.start(mContext!!, orderNo, ruiCard.realPrice)
                } else {
                    jsonBean?.let {
                        ToastUtil.show(showApplication, jsonBean.message)
                    }
                }
            }

            override fun onFailure(e: Throwable) {
                ToastUtil.show(showApplication, "操作失败")
            }
        })
    }

    companion object {

        fun start(context: Activity) {
            val starter = Intent(context, CardListActivity::class.java)
            context.startActivityForResult(starter, IntentExtra.RequestCode.REQUEST_CHONG_ZHI)
        }
    }

}
