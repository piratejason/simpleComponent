package com.lxch.paycomponent_kotlin

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
interface PayResultCallback {
    fun result(success: Boolean, msg: String)
}