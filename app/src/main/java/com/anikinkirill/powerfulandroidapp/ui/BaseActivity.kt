package com.anikinkirill.powerfulandroidapp.ui

import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    companion object {
        const val TAG = "BaseActivity"
    }

}