package com.lj.fatoldsun.platform.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lj.fatoldsun.core.base.BaseLibFragment
import com.lj.fatoldsun.core.config.status.State
import com.lj.fatoldsun.core.utils.ToastUtil
import com.lj.fatoldsun.platform.adapter.WebsitesAdapter
import com.lj.fatoldsun.platform.databinding.FragmentWebsitesBinding
import com.lj.fatoldsun.platform.vm.WebsitesViewModel
import com.lj.fatoldsun.platform.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author LJ
 * @time 2025/03/19 20:41
 * @description:
 */
@AndroidEntryPoint
class WebsitesFragment : BaseLibFragment<FragmentWebsitesBinding>() {
    private val mainViewModel: WebsitesViewModel by viewModels()
    private val websitesAdapter: WebsitesAdapter by lazy {  WebsitesAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebsitesBinding {
        return FragmentWebsitesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //初始化RecyclerView
        mBinding.rvWebsites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = websitesAdapter
        }
        //获取数据
        mainViewModel.fetchDataFromNet()

        websitesAdapter.setOnItemClickListener { adapter, view, position ->
            val website = adapter.items[position]
            mActivity.navigateTo(WebViewActivity::class.java, "url" to website.link)
        }

        /**
         * 观察状态变化（使用Flow）
         */
        lifecycleScope.launch {
            mainViewModel.stateFlow.collect { state ->
                state?.let {
                    when(it) {
                        is State.Loading -> toggleLoading(it.isLoading)
                        is State.Error -> ToastUtil.show(mActivity, it.errorMessage)
                        is State.Success -> websitesAdapter.updateWebsites(it.data)
                    }
                }
            }
        }
        
        }
    }


