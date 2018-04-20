package com.lxch.widgetcomponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
@SuppressLint("JavascriptInterface")
public class JSWebView extends WebView {
    public String js = "javascript:window.NativeBridge = function(name,message){eval(\"messageHandlers.\"+name+\"(message)\")}";

    public JSWebView(Context context) {
        this(context, null);
    }

    public JSWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings settings = getSettings();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //没有缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //网页自适应屏幕大小
        settings.setLoadWithOverviewMode(true);
        //设置图片适合WebView大小
        settings.setUseWideViewPort(true);
        //图片太大超出屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //注入JS代码
        addJavascriptInterface(this, "messageHandlers");
        //添加监听，如果不添加会出现一个空白页面
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return true;
//            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //注入JS代码
                view.loadUrl(js);
            }
        });
    }
}
