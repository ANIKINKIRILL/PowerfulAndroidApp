package com.anikinkirill.powerfulandroidapp.ui

import com.anikinkirill.powerfulandroidapp.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    companion object {
        const val TAG = "BaseActivity"
    }

}