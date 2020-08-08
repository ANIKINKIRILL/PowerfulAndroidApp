package com.anikinkirill.powerfulandroidapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        authViewModel.viewState.observe(this, Observer { authViewState ->
            authViewState.registrationFields?.let { registrationFields ->
                registrationFields.registration_email?.let { input_email.setText(it) }
                registrationFields.registration_username?.let { input_username.setText(it) }
                registrationFields.registration_password?.let { input_password.setText(it) }
                registrationFields.registration_confirm_password?.let { input_password_confirm.setText(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        authViewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }

}