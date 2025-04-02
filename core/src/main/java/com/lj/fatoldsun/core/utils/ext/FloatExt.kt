package com.lj.fatoldsun.core.utils.ext

import android.content.res.Resources
import android.util.TypedValue
import java.math.BigDecimal

/**
 * @author LJ
 * @time 2025/04/02 14:00
 * @description:
 */
/**
 * 保留count位小数
 * @param count Int
 */
fun Float.savaBit(count:Int)=  BigDecimal(this.toDouble()).setScale(count, BigDecimal.ROUND_HALF_UP).toFloat()

/**
 * dp转px
 */
val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)