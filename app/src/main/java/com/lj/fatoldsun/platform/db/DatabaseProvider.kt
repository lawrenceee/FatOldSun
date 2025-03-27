package com.lj.fatoldsun.platform.db

import android.content.Context
import androidx.room.Room

/**
 * @author LJ
 * @time 2025/03/22 17:25
 * @description:
 * 不使用hilt注入获取的Database
 * @Deprecated
 *
 */
object DatabaseProvider {
    private const val DB_NAME = "my_db"

    @Volatile //确保 instance 的可见性。即当一个线程修改 instance 时，其他线程能立即看到最新的值。
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).build().also { instance = it }
        }
    }

}