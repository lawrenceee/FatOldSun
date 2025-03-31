package com.lj.fatoldsun.core.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.lj.fatoldsun.core.utils.Logger
import me.jessyan.autosize.AutoSizeCompat
import java.io.Serializable

/**
 * @author LJ
 * @time 2025/03/19 14:14
 * @description:
 */
abstract class BaseLibActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //屏幕适配
        AutoSizeCompat.autoConvertDensityOfGlobal(resources)
    }

    /**
     * 通用的 Activity 跳转方法
     * @param targetActivity 目标 Activity 类
     * @param extras 可选的 Bundle 参数
     * @param source 跳转来源（用于追踪）
     */
    fun <T : Activity> navigateTo(
        targetActivity: Class<T>,
        extras: Bundle? = null,
        source: String = this::class.java.simpleName
    ) {
        Logger.d("Navigating to ${targetActivity.simpleName} from $source")
        val intent = Intent(this, targetActivity)
        extras?.let { intent.putExtras(it) }
        startActivity(intent)
    }

    // 重载方法，支持直接传参数
    fun <T: Activity> navigateTo (
        targetActivity: Class<T>,
        vararg params: Pair<String, Any>,
        source: String = this::class.java.simpleName
    ) {
        var bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Serializable -> putSerializable(key, value)
                    is Parcelable -> putParcelable(key, value)
                    //可扩展其他类型
                    else -> throw IllegalArgumentException("Unsupported param type: ${value::class.java}")
                }
            }
        }
        navigateTo(targetActivity, bundle, source)
    }

}