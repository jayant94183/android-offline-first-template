package com.example.myandroidtemplate.di

import android.content.Context
import androidx.room.Room
import com.example.myandroidtemplate.data.local.db.AppDatabase
import com.example.myandroidtemplate.data.local.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun db(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "my_database").build()

    @Provides
    fun userDao(db: AppDatabase): UserDao = db.userDao()
}