package com.anikinkirill.powerfulandroidapp.repository.auth

import com.anikinkirill.powerfulandroidapp.api.auth.OpenApiAuthService
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.anikinkirill.powerfulandroidapp.session.SessionManager

class AuthRepository
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val authService: OpenApiAuthService,
    val sessionManager: SessionManager
) {



}