package com.anikinkirill.powerfulandroidapp.di

import android.app.Application
import androidx.room.Room
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AppDatabase
import com.anikinkirill.powerfulandroidapp.persitence.AppDatabase.Companion.DATABASE_NAME
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideAuthTokenDao(appDatabase: AppDatabase) : AuthTokenDao {
        return appDatabase.getAuthTokenDao()
    }

    @Singleton
    @Provides
    fun provideAccountPropertiesDao(appDatabase: AppDatabase) : AccountPropertiesDao {
        return appDatabase.getAccountPropertiesDao()
    }

    @Singleton
    @Provides
    fun provideRequestOptions() : RequestOptions {
        return RequestOptions().placeholder(R.drawable.default_image).error(R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions) : RequestManager {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }

}