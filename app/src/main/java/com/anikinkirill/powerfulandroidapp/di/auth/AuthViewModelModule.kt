package com.anikinkirill.powerfulandroidapp.di.auth

import androidx.lifecycle.ViewModel
import com.anikinkirill.powerfulandroidapp.di.ViewModelKey
import com.anikinkirill.powerfulandroidapp.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel) : ViewModel

}