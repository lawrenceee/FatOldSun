package com.lj.fatoldsun.platform.model.response

/**
 * @author LJ
 * @time 2025/04/01 13:15
 * @description:
 *
 *     {
 *         "desc": "我们支持订阅啦~",
 *         "id": 30,
 *         "imagePath": "https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png",
 *         "isVisible": 1,
 *         "order": 2,
 *         "title": "我们支持订阅啦~",
 *         "type": 0,
 *         "url": "https://www.wanandroid.com/blog/show/3352"
 *     },
 */
data class BannerResponse(
    val id: Int, //banner id
    val title: String,//标题
    val imagePath: String,//图片url
    val url: String,//跳转的网页链接
)