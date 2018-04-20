package com.lxch.components_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lxch.paycomponent_kotlin.PayModel
import com.lxch.paycomponent_kotlin.PayResultCallback
import com.lxch.paycomponent_kotlin.PayType
import com.lxch.paycomponent_kotlin.PayUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PayResultCallback {
    override fun result(success: Boolean, msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var payModel = PayModel()
        PayUtil.instance.payResultCallback = this
        aliPay.setOnClickListener { PayUtil.instance.pay(PayType.ALI_PAY, payModel) }
        wechatPay.setOnClickListener { PayUtil.instance.pay(PayType.WECHAT_PAY, payModel) }
    }
}

