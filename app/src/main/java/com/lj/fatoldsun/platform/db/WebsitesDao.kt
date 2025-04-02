package com.lj.fatoldsun.platform.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lj.fatoldsun.platform.model.entity.Website

@Dao
interface WebsitesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebsites(websites: List<Website>)

    @Query("SELECT * FROM website")
    suspend fun getAllWebsites(): List<Website>

    @Query("DELETE FROM website" )
    suspend fun clearWebsites()
}
