package com.anikinkirill.powerfulandroidapp.di.main

import com.anikinkirill.powerfulandroidapp.ui.main.account.AccountFragment
import com.anikinkirill.powerfulandroidapp.ui.main.account.ChangePasswordFragment
import com.anikinkirill.powerfulandroidapp.ui.main.account.UpdateAccountFragment
import com.anikinkirill.powerfulandroidapp.ui.main.blog.BlogFragment
import com.anikinkirill.powerfulandroidapp.ui.main.blog.UpdateBlogFragment
import com.anikinkirill.powerfulandroidapp.ui.main.blog.ViewBlogFragment
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.CreateBlogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment

}