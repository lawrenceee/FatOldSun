package com.lj.fatoldsun.core.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {

    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    fun showLong(context: Context, message: String) {
        show(context, message, Toast.LENGTH_LONG)
    }
}