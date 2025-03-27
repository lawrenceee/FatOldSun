package com.lj.fatoldsun.platform.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lj.fatoldsun.core.base.BaseLibFragment
import com.lj.fatoldsun.core.config.status.State
import com.lj.fatoldsun.core.utils.ToastUtil
import com.lj.fatoldsun.platform.adapter.WebsitesAdapter
import com.lj.fatoldsun.platform.databinding.FragmentWebsitesBinding
import com.lj.fatoldsun.platform.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author LJ
 * @time 2025/03/19 20:41
 * @description:
 */
@AndroidEntryPoint
class WebsitesFragment : BaseLibFragment<FragmentWebsitesBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val websitesAdapter: WebsitesAdapter = WebsitesAdapter()

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
        //点击按钮获取数据
        mBinding.btnChangeMessage.setOnClickListener {
            mainViewModel.fetchDataFromNet()
        }


        /**
         * 观察在状态变化
         */
        observe(mainViewModel.state) { state ->
            when(state) {
                is State.Loading -> toggleLoading(state.isLoading)
                is State.Error -> ToastUtil.show(mContext, state.errorMessage)
                is State.Success -> websitesAdapter.updateWebsites(state.data)
                }

            }
        }
    }


