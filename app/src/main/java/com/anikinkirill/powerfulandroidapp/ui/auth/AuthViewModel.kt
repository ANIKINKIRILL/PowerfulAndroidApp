package com.anikinkirill.powerfulandroidapp.ui.auth

import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.repository.auth.AuthRepository
import com.anikinkirill.powerfulandroidapp.ui.BaseViewModel
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.auth.state.AuthStateEvent
import com.anikinkirill.powerfulandroidapp.ui.auth.state.AuthStateEvent.*
import com.anikinkirill.powerfulandroidapp.ui.auth.state.AuthViewState
import com.anikinkirill.powerfulandroidapp.ui.auth.state.LoginFields
import com.anikinkirill.powerfulandroidapp.ui.auth.state.RegistrationFields
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel @Inject constructor(val authRepository: AuthRepository) : BaseViewModel<AuthStateEvent, AuthViewState>() {

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(event: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        return when(event) {
            is LoginAttemptEvent -> {
                AbsentLiveData.create()
            }
            is RegisterAttemptEvent -> {
                AbsentLiveData.create()
            }
            is CheckPreviousAuthEvent -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        update.authToken = authToken
        _viewState.value = update
    }

}