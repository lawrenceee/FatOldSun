package com.lj.fatoldsun.core.webview

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.viewbinding.ViewBinding
import com.lj.fatoldsun.core.base.BaseLibFragment

/**
 * @author LJ
 * @time 2025/03/29 21:33
 * @description:
 * 通用的 WebView 基类，提供基础的 WebView 配置和功能
 * 支持高效加载、链接处理、进度管理
 * 子类需提供布局和具体逻辑
 */
abstract class BaseWebViewFragment<VB: ViewBinding> : BaseLibFragment<VB>() {

    companion object {
        const val ARG_URL = "arg_url"
    }

    private lateinit var mWebView: WebView

    //设置 WebView

    @SuppressLint("SetJavaScriptEnabled")
    protected fun setupWebView(webview: WebView, url: String) {
        mWebView = webview
        mWebView.apply {

            // 启用 JavaScript（网页交互需要）
            settings.javaScriptEnabled = true

            // 启用 DOM 存储（支持 HTML5 特性）
            settings.domStorageEnabled = true

            //自适应屏幕
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            //启用优先使用缓存，提升加载速度
            settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.url?.toString()?.let { url ->
                        if (url.startsWith("tel:") || url.startsWith("mailto:")) {
                            return onSpecialUrl(url)
                        }
                    }
                    return false //其他URL由WebView加载
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onPageLoadFinished()
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    onLoadError(error?.description?.toString() ?: "Unknown Error")
                }

            }
            //设置WebChromeClient, 处理进度条和JS弹窗
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    onProgressChanged(newProgress)
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    // TODO: 实现文件选择功能
                    return false
                }
            }

            loadUrl(url)
        }
    }

    //页面加载完成
    open fun onPageLoadFinished() {}

    // 加载错误
    open fun onLoadError(errorMessage: String) {}

    //进度变化
    open fun onProgressChanged(newProgress: Int) {}

    // 处理特殊协议（子类可重写）
    open fun onSpecialUrl(url: String): Boolean {
        // 默认不处理，子类可实现
        // TODO: 启动拨号或邮件应用
        return true
    }

    // 刷新
    open fun refresh() {
        mWebView.reload()
    }

    // 前进
    open fun goForward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward()
        }
    }

    // 后退
    open fun goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        }
    }

    override fun onDestroyView() {
        if (::mWebView.isInitialized) { // 确保 webView 已初始化
            mWebView.apply {
                stopLoading()
                removeAllViews()
                destroy()
            }
        }
        super.onDestroyView()

    }
}