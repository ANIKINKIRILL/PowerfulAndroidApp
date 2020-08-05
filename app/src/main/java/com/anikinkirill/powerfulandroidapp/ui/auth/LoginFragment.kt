package com.anikinkirill.powerfulandroidapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        authViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.loginFields?.let { loginFields ->
                    loginFields.login_email?.let { email -> input_email.setText(email) }
                    loginFields.login_password?.let { password -> input_password.setText(password) }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        authViewModel.setLoginFields(LoginFields(input_email.text.toString(), input_password.text.toString()))
    }

}