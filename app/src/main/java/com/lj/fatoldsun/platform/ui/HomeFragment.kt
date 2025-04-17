package com.lj.fatoldsun.platform.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lj.fatoldsun.core.base.BaseLibFragment
import com.lj.fatoldsun.core.config.status.State
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.utils.ToastUtil
import com.lj.fatoldsun.core.utils.ext.SlideDirection
import com.lj.fatoldsun.core.utils.ext.dp
import com.lj.fatoldsun.core.utils.ext.slideIn
import com.lj.fatoldsun.core.utils.ext.slideOut
import com.lj.fatoldsun.core.widget.banner.BannerView
import com.lj.fatoldsun.platform.adapter.ArticleLoadStateFooterAdapter
import com.lj.fatoldsun.platform.adapter.ArticlePagingAdapter
import com.lj.fatoldsun.platform.adapter.HeaderAdapter
import com.lj.fatoldsun.platform.databinding.FragmentHomeBinding
import com.lj.fatoldsun.platform.model.entity.BannerItem
import com.lj.fatoldsun.platform.vm.HomeViewModel
import com.lj.fatoldsun.platform.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseLibFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bannerView: BannerView
    private lateinit var headerAdapter: HeaderAdapter
    private val articleAdapter by lazy {
        ArticlePagingAdapter { articles -> //列表的点击事件回调
            mActivity.navigateTo(
                WebViewActivity::class.java, "url" to articles.link
            )
        }
    }

    /**
     * 加载更多adapter
     */
    private val articleLoadStateFooterAdapter by lazy {
        ArticleLoadStateFooterAdapter { articleAdapter.retry() }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerView = BannerView(mActivity).apply {
            //宽撑满，高200dp
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200f.dp.toInt()
            )
            setOnItemClickListener {
                mActivity.navigateTo(
                    WebViewActivity::class.java,
                    "url" to it.actionUrl
                )
            }
        }

        headerAdapter = HeaderAdapter(bannerView) //头布局适配器假如bannerView

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter( //拼接adapter
                headerAdapter,
                articleAdapter.withLoadStateFooter(articleLoadStateFooterAdapter) //附加上加载更多脚布局adapter
            )
//             监听滚动事件，控制 FAB 的显示/隐藏
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val isFabShown = mBinding.fbScrollToTop.isShown
                    mBinding.fbScrollToTop.apply {
                        if (firstVisiblePosition > 10) {
                            if (!isFabShown) {
                                this.slideIn(SlideDirection.BOTTOM, duration = 300L)
                            }
                        } else {
                            if (isFabShown) {
                                this.slideOut(SlideDirection.BOTTOM)
                            }
                        }
                    }
                }
            })

        }

        // 点击 FAB 回到顶部
        mBinding.fbScrollToTop.setOnClickListener {
            mBinding.recyclerView.smoothScrollToPosition(0)
        }


        /**
         * 观察banner数据
         */
        observe(homeViewModel.state) { state ->
            when (state) {
                is State.Loading -> {}//加载状态交给让 articleAdapter.loadStateFlow 统一管理
                is State.Error -> ToastUtil.show(mActivity, state.errorMessage)
                is State.Success -> {
                    val bannerItems = state.data.map {
                        BannerItem(
                            imageUrl = it.imagePath,
                            title = it.title,
                            actionUrl = it.url
                        )
                    }
                    bannerView.setItems(bannerItems)
                }

            }

        }




        // 监听文章数据的加载状态，统一管理 swipeRefresh.isRefreshing 如果 Flow 发射数据的速度很快，而下游（收集端）处理速度跟不上，collectLatest 会取消之前的处理，只处理最新发射的数据。减少不必要的 UI 更新。
        lifecycleScope.launch {
            articleAdapter.loadStateFlow.collectLatest { loadStates ->
                when (val refreshState = loadStates.refresh) {
                    is LoadState.Loading -> mBinding.swipeRefresh.isRefreshing = true
                    is LoadState.NotLoading -> mBinding.swipeRefresh.isRefreshing = false
                    is LoadState.Error -> {
                        ToastUtil.show(mActivity, refreshState.error.localizedMessage ?: "加载失败")
                        Logger.e(message = refreshState.error.localizedMessage ?: "加载失败")
                        mBinding.swipeRefresh.isRefreshing = false
                    }

                }
            }
        }


        // 在生命周期作用域内启动协程
        // 安全处理异步数据
        lifecycleScope.launch {
            // 收集最新的文章分页数据  将 Paging 数据提交给适配器
            homeViewModel.getArticlesFromNetwork().collectLatest { pagingData ->
                articleAdapter.submitData(pagingData)

            }
        }
        mBinding.swipeRefresh.setOnRefreshListener {
            homeViewModel.refresh()
            // 触发适配器刷新
            // 作用：重新请求 Paging 数据
            articleAdapter.refresh()
        }

        homeViewModel.fetchBannersFromNet() //获取banner数据


    }


}