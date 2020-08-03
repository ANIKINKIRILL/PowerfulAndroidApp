package com.anikinkirill.powerfulandroidapp.di

import android.app.Application
import com.anikinkirill.powerfulandroidapp.BaseApplication
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelProviderFactoryModule::class,
        ActivityBuildersModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    val sessionManager: SessionManager // must add here b/c injecting into abstract classes

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder
        fun build() : AppComponent
    }

}