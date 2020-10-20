package com.anikinkirill.powerfulandroidapp.di.main

import androidx.lifecycle.ViewModel
import com.anikinkirill.powerfulandroidapp.di.ViewModelKey
import com.anikinkirill.powerfulandroidapp.ui.main.account.AccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel) : ViewModel

}