package com.lj.fatoldsun.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lj.fatoldsun.core.config.status.State
import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.utils.NetworkErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author LJ
 * @time 2025/03/20 17:15
 * @description:
 */
open class BaseViewModel<T> : ViewModel() {
    //储存状态的LiveData
    private val _state = MutableLiveData<State<T>>()
    val state: LiveData<State<T>> get() = _state


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
            _state.value = State.Loading()
            runCatching {
                block()
            }.onSuccess { response ->
                if(response.isSuccess()) {
                    _state.value = State.Success(response.data)
                    onSuccess(response.data)
                } else {
                    val error = response.errorMsg
                    Logger.e(message = error)
                    _state.value = State.Error(error)
                }
                _state.value = State.Loading(false)
            }.onFailure { throwable ->
                val error = NetworkErrorHandler.handleError(throwable)
                Logger.e(message = error)
                _state.value = State.Error(NetworkErrorHandler.handleError(throwable))
                _state.value = State.Loading(false)
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
            _state.value = State.Loading()
            runCatching {
                block()
            }.onSuccess { data ->
                _state.value = State.Success(data)
                onSuccess(data)
                _state.value = State.Loading(false)
            }.onFailure { throwable ->
                val error = NetworkErrorHandler.handleError(throwable)
                Logger.e(message = error)
                _state.value = State.Error(error)
                _state.value = State.Loading(false)

            }
        }
    }

    /**
     * 更新数据
     */
    fun updateData(newData: T) {
        _state.value = State.Success(newData)
    }
}