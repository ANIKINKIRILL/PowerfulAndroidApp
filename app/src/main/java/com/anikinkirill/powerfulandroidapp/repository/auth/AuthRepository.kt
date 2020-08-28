package com.anikinkirill.powerfulandroidapp.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.anikinkirill.powerfulandroidapp.api.auth.OpenApiAuthService
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.*
import com.anikinkirill.powerfulandroidapp.ui.auth.state.AuthViewState
import com.anikinkirill.powerfulandroidapp.util.ApiResponse.*
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val authService: OpenApiAuthService,
    val sessionManager: SessionManager
) {

    fun attemptLogin(email: String, password: String) : LiveData<DataState<AuthViewState>> {
        return authService.login(email, password).switchMap { apiResponse ->
            object : LiveData<DataState<AuthViewState>>() {
                override fun onActive() {
                    super.onActive()
                    when(apiResponse) {
                        is ApiSuccessResponse -> {
                            value = DataState.data(data = AuthViewState(authToken = AuthToken(apiResponse.body.pk, apiResponse.body.token)), response = null)
                        }
                        is ApiErrorResponse -> {
                            value = DataState.error(response = Response(apiResponse.errorMessage, responseType = ResponseType.Dialog()))
                        }
                        is ApiEmptyResponse -> {
                            value = DataState.error(response =  Response(ERROR_UNKNOWN, responseType = ResponseType.Dialog()))
                        }
                    }
                }
            }
        }
    }

    fun attemptRegister(email: String, username: String, password: String, confirm_password: String) : LiveData<DataState<AuthViewState>> {
        return authService.register(email, username, password, confirm_password).switchMap { apiResponse ->
            object : LiveData<DataState<AuthViewState>>() {
                override fun onActive() {
                    super.onActive()
                    when(apiResponse) {
                        is ApiSuccessResponse -> {
                            value = DataState.data(data = AuthViewState(authToken = AuthToken(apiResponse.body.pk, apiResponse.body.token)), response = null)
                        }
                        is ApiErrorResponse -> {
                            value = DataState.error(response = Response(apiResponse.errorMessage, responseType = ResponseType.Dialog()))
                        }
                        is ApiEmptyResponse -> {
                            value = DataState.error(response =  Response(ERROR_UNKNOWN, responseType = ResponseType.Dialog()))
                        }
                    }
                }
            }
        }
    }


}