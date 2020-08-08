package com.anikinkirill.powerfulandroidapp.di

import com.anikinkirill.powerfulandroidapp.di.auth.AuthFragmentBuildersModule
import com.anikinkirill.powerfulandroidapp.di.auth.AuthModule
import com.anikinkirill.powerfulandroidapp.di.auth.AuthScope
import com.anikinkirill.powerfulandroidapp.di.auth.AuthViewModelModule
import com.anikinkirill.powerfulandroidapp.di.main.MainScope
import com.anikinkirill.powerfulandroidapp.ui.auth.AuthActivity
import com.anikinkirill.powerfulandroidapp.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthModule::class,
            AuthViewModelModule::class,
            AuthFragmentBuildersModule::class
        ]
    )
    abstract fun contributeAuthActivity() : AuthActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity

}