package com.lj.fatoldsun.core.utils.ext

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

/**
 * @author LJ
 * @time 2025/04/03 10:52
 * @description:
 * 将 View 从指定方向滑出显示。
 * @param direction 滑动方向（默认从底部滑出）
 * @param distance 滑动距离（默认使用 View 的总高度或宽度）
 * @param duration 动画时长（默认 200ms）
 *
 */

enum class SlideDirection {
    LEFT, RIGHT, TOP, BOTTOM
}

fun View.slideIn(
    direction: SlideDirection = SlideDirection.BOTTOM,
    distance: Float? = null,
    duration: Long = 200
) {
    this.clearAnimation()
    this.visibility = View.VISIBLE

    val totalDistance = distance ?: when (direction) {
        SlideDirection.LEFT, SlideDirection.RIGHT -> this.totalWidth
        SlideDirection.TOP, SlideDirection.BOTTOM -> this.totalHeight
    }

    val startTranslation = when (direction) {
        SlideDirection.LEFT -> -totalDistance
        SlideDirection.RIGHT -> totalDistance
        SlideDirection.TOP -> -totalDistance
        SlideDirection.BOTTOM -> totalDistance
    }

    this.translationX = when (direction) {
        SlideDirection.LEFT, SlideDirection.RIGHT -> startTranslation
        else -> 0f
    }
    this.translationY = when (direction) {
        SlideDirection.TOP, SlideDirection.BOTTOM -> startTranslation
        else -> 0f
    }

    this.animate()
        .translationX(0f)
        .translationY(0f)
        .setDuration(duration)
        .setInterpolator(DecelerateInterpolator())
        .start()
}

/**
 * 将 View 滑到指定方向并隐藏。
 * @param direction 滑动方向（默认滑到底部）
 * @param distance 滑动距离（默认使用 View 的总高度或宽度）
 * @param duration 动画时长（默认 200ms）
 */
fun View.slideOut(
    direction: SlideDirection = SlideDirection.BOTTOM,
    distance: Float? = null,
    duration: Long = 200
) {
    this.clearAnimation()

    val totalDistance = distance ?: when (direction) {
        SlideDirection.LEFT, SlideDirection.RIGHT -> this.totalWidth
        SlideDirection.TOP, SlideDirection.BOTTOM -> this.totalHeight
    }

    val endTranslation = when (direction) {
        SlideDirection.LEFT -> -totalDistance
        SlideDirection.RIGHT -> totalDistance
        SlideDirection.TOP -> -totalDistance
        SlideDirection.BOTTOM -> totalDistance
    }

    this.animate()
        .translationX(
            when (direction) {
                SlideDirection.LEFT, SlideDirection.RIGHT -> endTranslation
                else -> this.translationX
            }
        )
        .translationY(
            when (direction) {
                SlideDirection.TOP, SlideDirection.BOTTOM -> endTranslation
                else -> this.translationY
            }
        )
        .setDuration(duration)
        .setInterpolator(AccelerateInterpolator())
        .withEndAction { this.visibility = View.INVISIBLE }
        .start()
}

private val View.totalWidth: Float
    get() {
        val layoutParams = layoutParams as? ViewGroup.MarginLayoutParams
        return this.width.toFloat() + (layoutParams?.leftMargin ?: 0) + (layoutParams?.rightMargin ?: 0)
    }

private val View.totalHeight: Float
    get() {
        val layoutParams = layoutParams as? ViewGroup.MarginLayoutParams
        return this.height.toFloat() + (layoutParams?.topMargin ?: 0) + (layoutParams?.bottomMargin ?: 0)
    }
