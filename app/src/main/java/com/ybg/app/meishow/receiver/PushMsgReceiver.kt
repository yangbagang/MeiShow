package com.ybg.app.meishow.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.igexin.sdk.PushConsts
import com.ybg.app.meishow.activity.gift.CardPaymentActivity
import org.json.JSONObject

/**
 * Created by yangbagang on 2017/1/26.
 */
class PushMsgReceiver : BroadcastReceiver() {

    private val PAY_CALL_BACK = "pay_call_back"

    private val gson = Gson()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        val bundle = intent.extras
        val cmd = bundle.getInt(PushConsts.CMD_ACTION)
        if (cmd == PushConsts.GET_MSG_DATA) {
            val payload = bundle.getByteArray("payload")
            if (payload != null) {
                parseMsg(String(payload))
            }
        }
    }

    private fun parseMsg(msg: String) {
        try {
            val json = JSONObject(msg)
            val type = json.getString("type")
            val data = json.getString("data")
            when(type) {
                PAY_CALL_BACK -> {
                    CardPaymentActivity.instance?.payFinish()
                }
                else -> {
                    println("type=$type, data=$data")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}