package com.lj.fatoldsun.platform.model.response

/**
 * @author LJ
 * @time 2025/03/31 21:36
 * @description:
 *   文章响应模型
 *   作用：映射 article/list 接口字段
 *   ArticleResponse 是接口绑定的，ArticleItem 是 UI 绑定的，两者分离便于维护。
 *   面向对象设计中的“数据层与表现层分离”原则。
 */
data class ArticleResponse(
    val id: Int,              // 文章 ID
    val title: String,        // 标题
    val link: String,         // 链接
    val author: String?,      // 作者
    val shareUser: String?,   // 分享者
    val niceDate: String?,    // 日期
    val chapterName: String?, // 章节名
    val superChapterName: String?  // 父章节名
)