package com.lxch.paycomponent_kotlin

import android.app.Activity
import android.os.Handler
import android.os.Message
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
class PayUtil private constructor() {
    var SDK_PAY_FLAG = 11
    var activity: Activity? = null
        set(value) {
            field = value
        }
    var payResultCallback: PayResultCallback? = null
        set(value) {
            field = value
        }
    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == SDK_PAY_FLAG) {
                var obj: Map<String, String> = msg.obj as Map<String, String>
                var payResult = AliPayResult(obj)
                when (payResult.resultStatus) {
                    "9000" -> payResultCallback?.result(true, "支付成功")
                    else -> payResultCallback?.result(false, "支付失败")
                }
            }
        }
    }

    companion object {
        var instance = PayUtil()

    }


    fun update(success: Boolean) {
        payResultCallback?.result(success, if (success) "支付成功" else "支付失败")
    }

    fun pay(payType: PayType, payModel: PayModel) {
        when (payType) {
            PayType.ALI_PAY -> ali(payModel.payString!!)
            PayType.WECHAT_PAY -> wechat(payModel)
        }
    }

    private fun ali(payString: String) {

        Thread(Runnable {
            var payTask = PayTask(activity)
            val payV2 = payTask.payV2(payString, true)

        }).start()
    }

    private fun wechat(payModel: PayModel) {
        var msgApi = WXAPIFactory.createWXAPI(activity, "") as IWXAPI
        msgApi.registerApp(PayConstant.wechat_app_key)
        var req = PayReq()
        req.appId = payModel.appId
        req.partnerId = payModel.partnerId
        req.prepayId = payModel.prepayId
        req.packageValue = payModel.packageValue
        req.nonceStr = payModel.nonceStr
        req.timeStamp = payModel.timeStamp
        req.sign = payModel.sign
        msgApi.sendReq(req)
    }
}