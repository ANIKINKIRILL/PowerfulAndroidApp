package com.anikinkirill.powerfulandroidapp.ui.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountStateEvent
import com.anikinkirill.powerfulandroidapp.util.SuccessHandling.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : BaseAccountFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        update_password_button.setOnClickListener {
            changePassword(
                input_current_password.text.toString(),
                input_new_password.text.toString(),
                input_confirm_new_password.text.toString()
            )
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            if (dataState != null) {
                dataState.data?.let { data ->
                    data.response?.let { event ->
                        if (event.peekContent().message.equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        })
    }

    private fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModel.setStateEvent(AccountStateEvent.ChangePasswordEvent(currentPassword, newPassword, confirmNewPassword))
    }

}