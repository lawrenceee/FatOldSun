package com.lj.fatoldsun.core.webview

import android.content.Context
import android.webkit.JavascriptInterface
import com.lj.fatoldsun.core.utils.ToastUtil

/**
 * @author LJ
 * @time 2025/03/29 17:45
 * @description:
 * JavaScript 接口，供网页调用 Android 方法
 * @param context 用于显示 Toast 等操作
 * @param callback: 用于处理网页请求的回调
 */
class WebAppInterface(
    private val context: Context,
    private val callback: (String, String) -> Unit = { _, _ -> }
) {

    @JavascriptInterface
    fun showToast(message: String) {
        //限制消息长度，防止恶意输入
        if (message.length <= 100) ToastUtil.show(context, message)
    }

    @JavascriptInterface
    fun getAppVersion(): String {
        return "1.0.0"
    }

    @JavascriptInterface
    fun sendDataToApp(data: String) {
        // 网页传递数据
        callback("data", data)
    }

    @JavascriptInterface
    fun requestDataFromApp(requestId: String) {
        callback("request", requestId)
    }
}