package com.anikinkirill.powerfulandroidapp.di.main

import com.anikinkirill.powerfulandroidapp.api.main.OpenApiMainService
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.repository.main.AccountRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofit: Retrofit) : OpenApiMainService {
        return retrofit.create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ) : AccountRepository {
        return AccountRepository(openApiMainService, accountPropertiesDao, sessionManager)
    }

}