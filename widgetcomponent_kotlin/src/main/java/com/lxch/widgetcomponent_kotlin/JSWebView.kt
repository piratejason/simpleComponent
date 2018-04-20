package com.lxch.widgetcomponent_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
@SuppressLint("JavascriptInterface")
class JSWebView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : WebView(context, attrs, defStyleAttr) {
    var js = "javascript:window.NativeBridge = function(name,message){eval(\"messageHandlers.\"+name+\"(message)\")}"

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)

    init {
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        addJavascriptInterface(this, "messageHandlers")
        webChromeClient = WebChromeClient()
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadUrl(js)
            }
        }
    }
}