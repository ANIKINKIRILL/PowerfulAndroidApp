package com.anikinkirill.powerfulandroidapp.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.api.auth.LoginResponse
import com.anikinkirill.powerfulandroidapp.api.auth.OpenApiAuthService
import com.anikinkirill.powerfulandroidapp.api.auth.RegistrationResponse
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import com.anikinkirill.powerfulandroidapp.repository.NetworkBoundResource
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.Response
import com.anikinkirill.powerfulandroidapp.ui.ResponseType
import com.anikinkirill.powerfulandroidapp.ui.auth.state.AuthViewState
import com.anikinkirill.powerfulandroidapp.ui.auth.state.LoginFields
import com.anikinkirill.powerfulandroidapp.ui.auth.state.RegistrationFields
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import com.anikinkirill.powerfulandroidapp.util.ApiResponse.ApiSuccessResponse
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val authService: OpenApiAuthService,
    val sessionManager: SessionManager
) {

    companion object {
        private const val TAG = "AppDebug_AuthRepository"
    }

    private var repositoryJob: Job? = null

    fun attemptLogin(email: String, password: String) : LiveData<DataState<AuthViewState>> {
        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(loginFieldErrors != LoginFields.LoginError.none()) {
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<LoginResponse, AuthViewState>(sessionManager.isConnectedToTheInternet()) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")
                // Incorrect credentials counts as a 200 response from the server, so need to handle that
                if(response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(response.body.error_message, shouldUseDialog = true, shouldUseToast = false)
                }

                onCompleteJob(DataState.data(AuthViewState(authToken = AuthToken(response.body.pk, response.body.token))))
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                return authService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveData()
    }

    fun attemptRegister(email: String, username: String, password: String, confirm_password: String) : LiveData<DataState<AuthViewState>> {
        val registerFieldErrors = RegistrationFields(email, username, password, confirm_password).isValidForRegistration()
        if(registerFieldErrors != RegistrationFields.RegistrationError.none()) {
            return returnErrorResponse(registerFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegistrationResponse, AuthViewState>(sessionManager.isConnectedToTheInternet()) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")
                // Incorrect credentials counts as a 200 response from the server, so need to handle that
                if(response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(response.body.errorMessage, shouldUseDialog = true, shouldUseToast = false)
                }

                onCompleteJob(DataState.data(AuthViewState(authToken = AuthToken(response.body.pk, response.body.token))))
            }

            override fun createCall(): LiveData<ApiResponse<RegistrationResponse>> {
                return authService.register(email, username, password, confirm_password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType) : LiveData<DataState<AuthViewState>> {
        Log.d(TAG, "returnErrorResponse: $errorMessage")
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(Response(errorMessage, responseType))
            }
        }
    }

    fun cancelActiveJobs() {
        Log.d(TAG, "AuthRepository: canceling on-going jobs")
        repositoryJob?.cancel()
    }
}