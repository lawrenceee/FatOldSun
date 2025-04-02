package com.lj.fatoldsun.platform.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lj.fatoldsun.platform.model.entity.ArticleItem

/**
 * @author LJ
 * @time 2025/04/01 14:39
 * @description:
 */
@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleItem>)

    // 获取文章分页数据源
    // 作用：供 Paging 使用
    @Query("SELECT * FROM articles ORDER BY id")
    fun getArticles(): PagingSource<Int, ArticleItem>

    @Query("DELETE  FROM articles")
    suspend fun clearAll()
}