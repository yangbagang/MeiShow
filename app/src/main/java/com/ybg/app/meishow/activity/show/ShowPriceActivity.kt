package com.ybg.app.meishow.activity.show

import android.os.Bundle
import android.view.View
import com.ybg.app.meishow.activity.base.BaseActivity
import com.ybg.app.meishow.R
import kotlinx.android.synthetic.main.activity_show_price.*

class ShowPriceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_price)
    }

    override fun setContentViewId(): Int {
        return R.layout.activity_show_price
    }

    override fun setUpView() {
        setCustomTitle("美秀价格设定")
    }

    override fun init() {

    }

    fun onClick(v: View) {
        when(v.id) {
            R.id.tv_show_10 -> {
                setPrice(10)
            }
            R.id.tv_show_20 -> {
                setPrice(20)
            }
            R.id.tv_show_30 -> {
                setPrice(30)
            }
            R.id.tv_show_40 -> {
                setPrice(40)
            }
            R.id.tv_show_50 -> {
                setPrice(50)
            }
            R.id.btn_show_price -> {
                try {
                    val price = et_show_other.text
                    setPrice(price.toString().toInt())
                } catch (e: Exception) {
                    setPrice(0)
                }
            }
        }
    }

    private fun setPrice(price: Int) {
        intent.putExtra(SHOW_PRICE, price)
        setResult(SHOW_PRICE_RESULT_CODE, intent)
        finish()
    }

    companion object {
        val SHOW_PRICE = "show_price"
        val SHOW_PRICE_REQUEST_CODE = 100
        val SHOW_PRICE_RESULT_CODE = 101
    }
}
