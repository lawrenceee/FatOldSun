package com.lj.fatoldsun.platform.network

/**
 * @author LJ
 * @time 2025/04/01 13:31
 * @description:
 * 分页响应模型（业务特定）
 * 作用：映射 article/list 接口的分页结构
 * @param T 数据项类型
 */
data class PagingResponse<T> (
    val curPage: Int,    // 当前页码
    val datas: List<T>,  // 数据列表
    val offset: Int,     // 偏移量
    val over: Boolean,   // 是否最后一页
    val pageCount: Int,  // 总页数
    val size: Int,       // 每页大小
    val total: Int       // 总条数
)
