package com.anikinkirill.powerfulandroidapp.di

import android.app.Application
import androidx.room.Room
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AppDatabase
import com.anikinkirill.powerfulandroidapp.persitence.AppDatabase.Companion.DATABASE_NAME
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.anikinkirill.powerfulandroidapp.util.LiveDataCallAdapterFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() // ignore fields w/o @Expose
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(gson: Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://open-api.xyz/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

}