package com.anikinkirill.powerfulandroidapp.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.api.auth.LoginResponse
import com.anikinkirill.powerfulandroidapp.api.auth.OpenApiAuthService
import com.anikinkirill.powerfulandroidapp.api.auth.RegistrationResponse
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
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
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import com.anikinkirill.powerfulandroidapp.util.ApiResponse.ApiSuccessResponse
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.anikinkirill.powerfulandroidapp.util.PreferenceKeys
import com.anikinkirill.powerfulandroidapp.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val authService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val editor: SharedPreferences.Editor
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

        return object : NetworkBoundResource<LoginResponse, Any, AuthViewState>(sessionManager.isConnectedToTheInternet(), true, false) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")
                // Incorrect credentials counts as a 200 response from the server, so need to handle that
                if(response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(response.body.error_message, shouldUseDialog = true, shouldUseToast = false)
                }

                // don't care about result. Just insert if it does't exist b/c foreign key relationship
                // with AuthToken table
                accountPropertiesDao.insertOrIgnore(AccountProperties(pk = response.body.pk, email = response.body.email, username = ""))

                // will return -1 if failure
                val result = authTokenDao.insert(AuthToken(response.body.pk, response.body.token))

                if(result < 0) {
                    return onCompleteJob(DataState.error(response = Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())))
                }

                saveEmailToSharedPreferences(email)

                onCompleteJob(DataState.data(AuthViewState(authToken = AuthToken(response.body.pk, response.body.token))))
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                return authService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            // not used in this case
            override fun loadFromCache(): LiveData<AuthViewState> {
                TODO("Not yet implemented")
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()
    }

    fun attemptRegister(email: String, username: String, password: String, confirm_password: String) : LiveData<DataState<AuthViewState>> {
        val registerFieldErrors = RegistrationFields(email, username, password, confirm_password).isValidForRegistration()
        if(registerFieldErrors != RegistrationFields.RegistrationError.none()) {
            return returnErrorResponse(registerFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegistrationResponse, Any, AuthViewState>(sessionManager.isConnectedToTheInternet(), true, false) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")
                // Incorrect credentials counts as a 200 response from the server, so need to handle that
                if(response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(response.body.errorMessage, shouldUseDialog = true, shouldUseToast = false)
                }

                // don't care about result. Just insert if it does't exist b/c foreign key relationship
                // with AuthToken table
                accountPropertiesDao.insertOrIgnore(AccountProperties(pk = response.body.pk, email = response.body.email, username = ""))

                // will return -1 if failure
                val result = authTokenDao.insert(AuthToken(response.body.pk, response.body.token))

                if(result < 0) {
                    return onCompleteJob(DataState.error(response = Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())))
                }

                saveEmailToSharedPreferences(email)

                onCompleteJob(DataState.data(AuthViewState(authToken = AuthToken(response.body.pk, response.body.token))))
            }

            override fun createCall(): LiveData<ApiResponse<RegistrationResponse>> {
                return authService.register(email, username, password, confirm_password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            // not used in this case
            override fun loadFromCache(): LiveData<AuthViewState> {
                TODO("Not yet implemented")
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()
    }

    fun checkPreviousAuthUser() : LiveData<DataState<AuthViewState>> {
        val previousAuthUserEmail : String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if(previousAuthUserEmail.isNullOrBlank()) {
            Log.d(TAG, "checkPreviousAuthUser: No previous user was found")
            return returnNoTokenFound()
        }

        return object : NetworkBoundResource<Void, Any, AuthViewState>(sessionManager.isConnectedToTheInternet(), false, false) {

            override suspend fun createCacheRequestAndReturn() {
                accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->
                    Log.d(TAG, "checkPreviousAuthUser: searching the Token $accountProperties")
                    accountProperties?.let { mAccountProperties ->
                        if(mAccountProperties.pk > -1) {
                            authTokenDao.searchByPk(mAccountProperties.pk).let { authToken ->
                                if(authToken != null) {
                                    onCompleteJob(DataState.data(AuthViewState(authToken = authToken)))
                                    return
                                }
                            }
                        }
                    }
                    Log.d(TAG, "checkPreviousAuthUser: AccountProperties is null. AuthToken not found")
                    onCompleteJob(DataState.data(null, Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None())))
                }
            }

            // not used in this case
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {

            }

            // not used in this case
            override fun createCall(): LiveData<ApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            // not used in this case
            override fun loadFromCache(): LiveData<AuthViewState> {
                TODO("Not yet implemented")
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()
    }

    private fun returnNoTokenFound() : LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.data(null, Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()))
            }
        }
    }

    private fun saveEmailToSharedPreferences(email: String) {
        editor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email).apply()
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