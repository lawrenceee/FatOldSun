package com.lj.fatoldsun.core.utils

import com.lj.fatoldsun.core.BuildConfig
import timber.log.Timber

object Logger {
    init {
        if (BuildConfig.DEBUG)  {Timber.plant(Timber.DebugTree())}
    }
    fun d(message: String) {
        Timber.d(message)
    }
    fun e(throwable: Throwable? = null, message: String) {
        throwable?.let { Timber.e(it, message) } ?: Timber.e(message)
    }
}