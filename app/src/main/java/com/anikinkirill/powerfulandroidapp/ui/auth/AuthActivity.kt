package com.anikinkirill.powerfulandroidapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.BaseActivity
import com.anikinkirill.powerfulandroidapp.ui.ResponseType.*
import com.anikinkirill.powerfulandroidapp.ui.main.MainActivity
import com.anikinkirill.powerfulandroidapp.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authViewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()
    }

    private fun subscribeObservers() {

        authViewModel.dataState.observe(this, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { dataEvent ->
                    dataEvent.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.authToken?.let { authToken ->
                            authViewModel.setAuthToken(authToken)
                        }
                    }
                }

                data.response?.let { dataEvent ->
                    dataEvent.getContentIfNotHandled()?.let { response ->
                        when(response.responseType) {
                            is Dialog -> {
                                // inflate dialog
                            }
                            is Toast -> {
                                // inflate toast
                            }
                            is None -> {

                            }
                        }
                    }
                }
            }
        })

        authViewModel.viewState.observe(this, Observer { authViewState ->
            authViewState.authToken?.let { authToken ->
                sessionManager.login(authToken)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { authToken ->
            if(authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        authViewModel.cancelActiveJobs()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

}