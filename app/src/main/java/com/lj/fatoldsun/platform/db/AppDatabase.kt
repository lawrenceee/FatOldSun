package com.lj.fatoldsun.platform.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lj.fatoldsun.platform.model.Website

/**
 * @author LJ
 * @time 2025/03/22 11:14
 * @description: 存储websites的数据库
 */
@Database(entities = [Website::class], version = 1, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun websitesDao(): WebsitesDao

}