package com.anikinkirill.powerfulandroidapp.persitence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.models.BlogPost

@Database(entities = [AccountProperties::class, AuthToken::class, BlogPost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getBlogPostDao(): BlogPostDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}