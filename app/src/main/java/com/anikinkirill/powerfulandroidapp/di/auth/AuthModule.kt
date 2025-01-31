package com.anikinkirill.powerfulandroidapp.di.auth

import android.content.SharedPreferences
import com.anikinkirill.powerfulandroidapp.api.auth.OpenApiAuthService
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.anikinkirill.powerfulandroidapp.repository.auth.AuthRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofit: Retrofit) : OpenApiAuthService {
        return retrofit.create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        openApiAuthService: OpenApiAuthService,
        accountPropertiesDao: AccountPropertiesDao,
        authTokenDao: AuthTokenDao,
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ) : AuthRepository {
        return AuthRepository(authTokenDao, accountPropertiesDao, openApiAuthService, sessionManager, sharedPreferences, editor)
    }

}
