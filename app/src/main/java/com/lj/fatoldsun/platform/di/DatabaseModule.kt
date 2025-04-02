package com.lj.fatoldsun.platform.di

import android.content.Context
import com.lj.fatoldsun.platform.db.AppDatabase
import com.lj.fatoldsun.platform.db.ArticlesDao
import com.lj.fatoldsun.platform.db.DatabaseProvider
import com.lj.fatoldsun.platform.db.WebsitesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author LJ
 * @time 2025/03/24 22:40
 * @description:
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return DatabaseProvider.getDatabase(context)
    }
    @Provides
    @Singleton
    fun provideWebsitesDao(appDatabase: AppDatabase): WebsitesDao {
        return appDatabase.websitesDao()
    }

    @Provides
    @Singleton
    fun provideArticlesDao(appDatabase: AppDatabase): ArticlesDao {
        return appDatabase.articlesDao()
    }

}