package com.anikinkirill.powerfulandroidapp.session

import android.app.Application
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao

class SessionManager constructor(val authTokenDao: AuthTokenDao, val application: Application) {

}