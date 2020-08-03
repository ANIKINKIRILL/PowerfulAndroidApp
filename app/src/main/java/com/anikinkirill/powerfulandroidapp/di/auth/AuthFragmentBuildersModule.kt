package com.anikinkirill.powerfulandroidapp.di.auth

import com.anikinkirill.powerfulandroidapp.ui.auth.ForgotPasswordFragment
import com.anikinkirill.powerfulandroidapp.ui.auth.LauncherFragment
import com.anikinkirill.powerfulandroidapp.ui.auth.LoginFragment
import com.anikinkirill.powerfulandroidapp.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLauncherFragment() : LauncherFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment() : LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment() : RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment() : ForgotPasswordFragment

}