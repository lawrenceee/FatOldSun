package com.lj.fatoldsun.core.config.status

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author LJ
 * @time 2025/04/20
 * @description:
 * 基于Flow的状态管理类
 * 提供响应式的状态管理功能
 * 支持与Kotlin协程和Flow API的无缝集成
 */
sealed class State<T> {
    data class Loading<T>(val isLoading: Boolean = true) : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val errorMessage: String): State<T>()
    
    companion object {
        /**
         * 创建一个StateFlow状态管理器
         * @return StateManager<T> 状态管理器实例
         */
        fun <T> createStateManager(): StateManager<T> = StateManager()
    }
}

/**
 * 状态管理器
 * 封装了StateFlow的状态管理功能
 */
class StateManager<T> {
    private val _state = MutableStateFlow<State<T>?>(null)
    val state: StateFlow<State<T>?> = _state
    
    /**
     * 设置加载状态
     * @param isLoading 是否正在加载
     */
    fun setLoading(isLoading: Boolean = true) {
        _state.value = State.Loading(isLoading)
    }
    
    /**
     * 设置成功状态
     * @param data 成功数据
     */
    fun setSuccess(data: T) {
        _state.value = State.Success(data)
    }
    
    /**
     * 设置错误状态
     * @param errorMessage 错误信息
     */
    fun setError(errorMessage: String) {
        _state.value = State.Error(errorMessage)
    }
    
    /**
     * 重置状态
     */
    fun reset() {
        _state.value = null
    }
}