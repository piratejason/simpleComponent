package com.lxch.paycomponent_kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
open class PayActivity : Activity(), IWXAPIEventHandler {
    var TAG = "PayActivity"
    var api: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, PayConstant.wechat_app_key)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    override fun onResp(p0: BaseResp?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onPayFinish, errCode = " + p0?.errCode)
        }
        when (p0?.type) {
            ConstantsAPI.COMMAND_PAY_BY_WX -> {
                when (p0?.errCode) {
                    0 -> PayUtil.instance.update(true)
                    else -> PayUtil.instance.update(false)
                }
            }

        }
        finish()
    }

    override fun onReq(p0: BaseReq?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }
}