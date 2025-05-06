package com.lj.fatoldsun.platform.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.webview.BaseWebViewFragment
import com.lj.fatoldsun.core.webview.WebAppInterface
import com.lj.fatoldsun.platform.databinding.FragmentWebviewBinding
import com.lj.fatoldsun.platform.vm.WebViewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author LJ
 * @time 2025/03/29 15:55
 * @description:
 * 通用的 WebView 页面，用于展示网页内容
 * 支持高效加载、各种链接处理、网页交互cls
 * 提供进度条、错误处理、刷新等功能
 * @param url 初始加载的网页 URL，通过 arguments 传入
 */
@AndroidEntryPoint
class WebViewFragment : BaseWebViewFragment<FragmentWebviewBinding>() {
    private val viewModel: WebViewViewModel by viewModels()

    companion object {
        private const val ARG_URL = "arg_url"

        // 创建 Fragment 实例并传入 URL
        fun newInstance(url: String): WebViewFragment {
            return WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
        }
    }
    
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebviewBinding {
       return FragmentWebviewBinding.inflate(inflater, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //获取传入的 URL
        val url = arguments?.getString(ARG_URL) ?: return

        viewModel.setUrl(url)

        setupWebView(mBinding.webView, url)

        /**
         * evaluateJavascript 必须在主线程调用（WebView 的操作通常都在主线程）。
         * WebAppInterface 的方法（比如 requestDataFromApp）也是在主线程被调用的
         */
        mBinding.webView.addJavascriptInterface(
            WebAppInterface(mActivity) { type, data ->
                when(type) {
                    "data"-> Logger.d("Received data from web: $data")
                    "request" -> callJsFunction("onDataReceived", "Data for $data")
                }
            }, "Android"
        )

        //使用Flow观察ViewModel的状态
        lifecycleScope.launch {
            viewModel.progressFlow.collect { progress ->
                mBinding.progressBar.visibility = if (progress < 100) View.VISIBLE else View.GONE
                mBinding.progressBar.progress = progress
            }
        }
        
        lifecycleScope.launch {
            viewModel.errorFlow.collect { errorMessage ->
                mBinding.errorLayout.visibility = if (null != errorMessage) View.VISIBLE else View.GONE
                mBinding.webView.visibility = if (null != errorMessage) View.GONE else View.VISIBLE
            }
        }

        mBinding.btnRetry.setOnClickListener {
            viewModel.retry()
            mBinding.webView.loadUrl(viewModel.getUrl())
        }


    }

    private fun callJsFunction(functionName: String, params: String, callback: (String?) -> Unit = {}) {
        mBinding.webView.evaluateJavascript("$functionName('$params')") { result ->
            if (result != null && result != "null") {
                callback(result)
            } else {
                callback(null)
            }
        }
    }

    override fun onPageLoadFinished() {
        viewModel.onPageFinshed()
    }

    override fun onLoadError(errorMessage: String) {
        viewModel.onError(errorMessage)
    }

    override fun onProgressChanged(newProgress: Int) {
        viewModel.onProgressChanged(newProgress)
    }

    override fun refresh() {
        viewModel.retry()
        super.refresh()
    }



}