package com.lj.fatoldsun.core.config.status

/**
 * @author LJ
 * @time 2025/03/24 13:50
 * @description: 用于状态类，表示数据的加载状态
 */
sealed class State<T> {
    data class Loading<T>(val isLoading: Boolean = true) : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val errorMessage: String): State<T>()
}