package com.lj.fatoldsun.platform.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author LJ
 * @time 2025/04/01 13:12
 * @description:
 *  文章数据模型，定义UI使用的文章结构
 *  ArticleResponse 是接口绑定的，ArticleItem 是 UI 绑定的，两者分离便于维护。
 *  面向对象设计中的“数据层与表现层分离”原则。
 */
@Entity(tableName = "articles")
data class ArticleItem (
    @PrimaryKey val id: Int,  // 文章 ID
    val title: String,        // 标题
    val link: String,         // 链接
    val author: String?,      // 作者
    val shareUser: String?,   // 分享者
    val niceDate: String?,    // 日期
    val chapterName: String?, // 章节名
    val superChapterName: String?  // 父章节名
)