package com.lxch.paycomponent;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * Created by luoxiaocheng on 2018/3/28.
 */

public class PayUtil {
    private final int SDK_PAY_FLAG = 11;
    private Activity activity;
    private PayResultCallback payResultCallback;
    private static PayUtil instance;
    /**
     * 支付宝回调
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {
                AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (payResultCallback != null) {
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        payResultCallback.result(true, "支付成功");

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        payResultCallback.result(true, "支付失败");
                    }
                }
            }
        }
    };

    private PayUtil() {
    }

    public static PayUtil get() {
        if (instance == null) {
            instance = new PayUtil();
        }
        return instance;
    }

    public void pay(PayType payType, PayModel payModel) {
        switch (payType) {
            case ALI_PAY:
                ali(payModel.getPayString());
                break;
            case WECHAT_PAY:
                wechat(payModel);
                break;
        }
    }

    private void ali(final String payString) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(payString, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void wechat(PayModel weChatModel) {

        //  商户APP工程中引入微信JAR包，调用API前，需要先向微信注册您的APPID，代码如下：
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);

        // 将该app注册到微信
        msgApi.registerApp(PayConstant.wechat_app_key);

        /** 应用ID	appid	String(32)	是	wx8888888888888888	微信开放平台审核通过的应用APPID
         商户号	partnerid	String(32)	是	1900000109	微信支付分配的商户号
         预支付交易会话ID	prepayid	String(32)	是	WX1217752501201407033233368018	微信返回的支付交易会话ID
         扩展字段	package	String(128)	是	Sign=WXPay	暂填写固定值Sign=WXPay
         随机字符串	noncestr	String(32)	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
         时间戳	timestamp	String(10)	是	1412000000	时间戳，请见接口规则-参数规定
         签名	sign	String(32)	是	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
         **/
        PayReq request = new PayReq();
        request.appId = weChatModel.getAppId();
        request.partnerId = weChatModel.getPartnerId();
        request.prepayId = weChatModel.getPrepayId();
        request.packageValue = weChatModel.getPackageValue();
        request.nonceStr = weChatModel.getNonceStr();
        request.timeStamp = weChatModel.getTimeStamp();
        request.sign = weChatModel.getSign();

        msgApi.sendReq(request);
    }

    public void setPayResultCallback(PayResultCallback payResultCallback) {
        this.payResultCallback = payResultCallback;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void update(boolean b) {
        if (payResultCallback != null) {
            payResultCallback.result(b, b ? "支付成功" : "支付失败");
        }
    }
}
