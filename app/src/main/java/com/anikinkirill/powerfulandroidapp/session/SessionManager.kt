package com.anikinkirill.powerfulandroidapp.session

import android.app.Application
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(val authTokenDao: AuthTokenDao, val application: Application) {

}