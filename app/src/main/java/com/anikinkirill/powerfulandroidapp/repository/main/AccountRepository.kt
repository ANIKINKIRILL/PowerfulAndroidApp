package com.anikinkirill.powerfulandroidapp.repository.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.api.main.OpenApiMainService
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.repository.NetworkBoundResource
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountViewState
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import kotlinx.coroutines.Job
import javax.inject.Inject

@SuppressLint("LongLogTag")
class AccountRepository
@Inject constructor(
    private val openApiMainService: OpenApiMainService,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val sessionManager: SessionManager
) {

    companion object {
        private const val TAG = "AppDebug_AccountRepository"
    }

    private var repositoryJob: Job? = null

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object : NetworkBoundResource<AccountProperties, AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true
        ) {
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<AccountProperties>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<ApiResponse<AccountProperties>> {
                return openApiMainService.getAccountProperties(
                    "Token ${authToken.token}"
                )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun cancelActiveJobs() {
        Log.d(TAG, "cancelActiveJobs: called")
    }

}