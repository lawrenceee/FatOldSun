package com.lj.fatoldsun.platform.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * @author LJ
 * @time 2025/03/29 16:27
 * @description:
 * WebView 页面的 ViewModel，管理 URL、加载进度和错误状态
 */
@HiltViewModel
class WebViewViewModel @Inject constructor(): ViewModel() {
    //当前Url
    private var _url: String = ""

    fun setUrl(newUrl: String) {
        _url = newUrl
        _errorFlow.value = null //重置错误信息
    }

    fun getUrl(): String = _url

    //加载进度，通过 Flow 通知 UI。
    private val _progressFlow = MutableStateFlow<Int>(0)
    val progressFlow: StateFlow<Int> get() = _progressFlow

    //错误信息，加载失败时显示。
    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow: StateFlow<String?> get() = _errorFlow

    //加载进度
    fun onProgressChanged(newProgress: Int) {
        _progressFlow.value = newProgress
    }

    fun onPageFinshed() {
        _errorFlow.value = null //加载成功 清空错误
    }

    fun onError(errorMessage: String) {
        _errorFlow.value = errorMessage
    }

    fun retry() {
        _errorFlow.value = null
        _progressFlow.value = 0
    }

}