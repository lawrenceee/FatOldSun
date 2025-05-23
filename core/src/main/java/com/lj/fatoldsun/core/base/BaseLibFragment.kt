package com.lj.fatoldsun.core.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.lj.fatoldsun.core.ui.LoadingDialog
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.utils.StatusNavBarUtil
import me.jessyan.autosize.AutoSizeCompat
import timber.log.Timber

/**
 * @author LJ
 * @time 2025/03/19 14:17
 * @description:
 */
abstract class BaseLibFragment<VB : ViewBinding> : Fragment() {
    protected val mContext by lazy {
        requireActivity()
    }

    //用lateinit不合适，因为Fragment销毁时需要置空_binding
    private var _binding: VB? = null

    //受保护的 binding 属性，只有在 _binding 不为 null 时才能访问
    protected val mBinding get() = _binding!! // 使用 !! 确保在访问时 _binding 已初始化

    private val mLoadingDialog: LoadingDialog by lazy { LoadingDialog(mContext) }

    //子类实现，创建VB实例
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container) //初始化
        //屏幕适配
        AutoSizeCompat.autoConvertDensityOfGlobal(resources)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("Fragment ${javaClass.simpleName} onViewCreated")
        Logger.e(null,"Fragment ${javaClass.simpleName} onViewCreated")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleLoading(false) //隐藏加载
        //视图销毁时fragment实例可能依然存在从而导致内存泄漏，所以需要置空
        _binding = null
    }

    protected fun toggleLoading(show: Boolean) {
        if (show && !mLoadingDialog.isShowing) {
            mLoadingDialog.show()
        } else if (!show && mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }



    //通用的observe方法,简化LiveData观察
    protected fun <T> observe(liveData: LiveData<T>, onChange: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner) { data ->
            onChange(data)
        }

    }
}