package com.anikinkirill.powerfulandroidapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.anikinkirill.powerfulandroidapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseAuthFrag"
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var authViewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = activity?.let {
            ViewModelProvider(it, viewModelProviderFactory).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        cancelActiveJobs()

    }

    private fun cancelActiveJobs() {
        authViewModel.cancelActiveJobs()
    }

}