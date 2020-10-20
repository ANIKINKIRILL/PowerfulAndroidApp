package com.anikinkirill.powerfulandroidapp.repository.main

import android.annotation.SuppressLint
import android.util.Log
import com.anikinkirill.powerfulandroidapp.api.main.OpenApiMainService
import com.anikinkirill.powerfulandroidapp.persitence.AccountPropertiesDao
import com.anikinkirill.powerfulandroidapp.session.SessionManager
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

    fun cancelActiveJobs() {
        Log.d(TAG, "cancelActiveJobs: called")
    }

}