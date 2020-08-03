package com.anikinkirill.powerfulandroidapp.ui.auth

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.BaseActivity
import com.anikinkirill.powerfulandroidapp.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authViewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
    }
}