package com.lj.fatoldsun.platform.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _error.value = null //重置错误信息
    }

    fun getUrl(): String = _url

    //加载进度，通过 LiveData 通知 UI。
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    //错误信息，加载失败时显示。
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    //加载进度
    fun onProgressChanged(newProgress: Int) {
        _progress.value = newProgress
    }

    fun onPageFinshed() {
        _error.value = null //加载成功 清空错误
    }

    fun onError(errorMessage: String) {
        _error.value = errorMessage
    }

    fun retry() {
        _error.value = null
        _progress.value = 0
    }

}