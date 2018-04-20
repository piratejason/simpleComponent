package com.lxch.paycomponent_kotlin

import android.text.TextUtils

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
class AliPayResult(rawResult: Map<String, String>) {
    var resultStatus: String? = null
        get() = field
    var result: String? = null
        get() = field
    var memo: String? = null
        get() = field

    init {
        if (rawResult != null) {
            val keys = rawResult.keys
            keys.forEach { key ->
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.getValue(key)
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.getValue(key)
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.getValue(key)
                }
            }
        }
    }
}