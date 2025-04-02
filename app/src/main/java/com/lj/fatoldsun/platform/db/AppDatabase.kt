package com.lj.fatoldsun.platform.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lj.fatoldsun.platform.model.entity.ArticleItem
import com.lj.fatoldsun.platform.model.entity.Website

/**
 * @author LJ
 * @time 2025/03/22 11:14
 * @description: 存储websites的数据库
 */
@Database(entities = [Website::class, ArticleItem::class], version = 2, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun websitesDao(): WebsitesDao
    abstract fun articlesDao(): ArticlesDao

}

/**
 * 数据库迁移，从版本 1 到 2
 * 作用：新增 articles 表
 */
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL(
//                """CREATE TABLE IF NOT EXISTS `articles` (
//            `id` INTEGER NOT NULL PRIMARY KEY,
//            `title` TEXT NOT NULL,
//            `link` TEXT NOT NULL,
//            `author` TEXT,
//            `shareUser` TEXT,
//            `niceDate` TEXT,
//            `chapterName` TEXT,
//            `superChapterName` TEXT)""".trimIndent()
//        )
//    }
//
//}