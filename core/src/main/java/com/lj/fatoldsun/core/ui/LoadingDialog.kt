package com.lj.fatoldsun.core.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.lj.fatoldsun.core.R

/**
 * @author LJ
 * @time 2025/03/21 12:43
 * @description:
 */
class LoadingDialog(context: Context) : Dialog(context) {
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        setContentView(view)
        //不可取消
        setCancelable(false)
        //设置背景为透明
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}