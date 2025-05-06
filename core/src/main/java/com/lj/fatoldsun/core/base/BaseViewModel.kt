package com.lj.fatoldsun.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lj.fatoldsun.core.config.status.State
import com.lj.fatoldsun.core.config.status.StateManager
import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.utils.NetworkErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author LJ
 * @time 2025/03/20 17:15
 * @description:
 * 基础ViewModel类，提供状态管理和网络请求封装
 * 支持LiveData和Flow两种状态管理方式
 */
open class BaseViewModel<T> : ViewModel() {
    // 基于Flow的状态管理
    private val stateManager = State.createStateManager<T>()
    val stateFlow: StateFlow<State<T>?> get() = stateManager.state


    /**
     *
     *  执行网络请求，自动管理状态
     *  @param block 网络请求的操作，返回 Response<T>
     *  @param onSuccess 请求成功的回调，接收实际数据 T
     *
     */
    protected fun launchRequest(
        block: suspend CoroutineScope.() -> Response<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        viewModelScope.launch {
            // 更新Flow状态
            stateManager.setLoading()
            
            runCatching {
                block()
            }.onSuccess { response ->
                if(response.isSuccess()) {
                    stateManager.setSuccess(response.data)
                    onSuccess(response.data)
                } else {
                    val error = response.errorMsg
                    Logger.e(message = error)
                    stateManager.setError(error)
                }
                stateManager.setLoading(false)
            }.onFailure { throwable ->
                val error = NetworkErrorHandler.handleError(throwable)
                Logger.e(message = error)
                stateManager.setError(error)
                stateManager.setLoading(false)
            }
        }
    }

    /**
     *      执行本地操作（例如数据库查询），自动管理状态
     *      @param block 本地操作，返回实际数据 T
     *     @param onSuccess 操作成功的回调
     */
    protected fun launchLocal(
        block: suspend  CoroutineScope.() -> T,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            // 更新Flow状态
            stateManager.setLoading()
            
            runCatching {
                block()
            }.onSuccess { data ->
                stateManager.setSuccess(data)
                onSuccess(data)
                stateManager.setLoading(false)
            }.onFailure { throwable ->
                val error = NetworkErrorHandler.handleError(throwable)
                Logger.e(message = error)
                stateManager.setError(error)
                stateManager.setLoading(false)
            }
        }
    }

    /**
     * 更新数据
     */
    fun updateData(newData: T) {
        stateManager.setSuccess(newData)
    }
    
    /**
     * 重置状态
     */
    fun resetState() {
        stateManager.reset()
    }
}