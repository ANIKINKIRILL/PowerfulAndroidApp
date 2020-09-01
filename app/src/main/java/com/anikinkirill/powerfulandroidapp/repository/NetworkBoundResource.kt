package com.anikinkirill.powerfulandroidapp.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.Response
import com.anikinkirill.powerfulandroidapp.ui.ResponseType
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import com.anikinkirill.powerfulandroidapp.util.ApiResponse.*
import com.anikinkirill.powerfulandroidapp.util.Constants.Companion.NETWORK_TIMEOUT
import com.anikinkirill.powerfulandroidapp.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

@SuppressLint("LongLogTag")
abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean // is there a network connection ?
) {

    private val TAG: String = "AppDebug_NetworkBoundResource"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(true, null))
        if(isNetworkAvailable) {
            coroutineScope.launch {
                // a delay for testing purposes
                delay(TESTING_NETWORK_DELAY)
                withContext(Main) {
                    // make network call
                    val apiResponse = createCall()
                    result.addSource(apiResponse) {
                        result.removeSource(apiResponse)
                        coroutineScope.launch {
                            handleNetworkCall(it)
                        }
                    }
                }
            }

            GlobalScope.launch {
                delay(NETWORK_TIMEOUT)
                if(!job.isCompleted){
                    Log.d(TAG, "NetworkBoundResource: job is timed out")
                    job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                }
            }

        }else {
            onErrorReturn(UNABLE_TODO_OPERATION_WO_INTERNET, shouldUseDialog = true, shouldUseToast = false)
        }
    }

    private suspend fun handleNetworkCall(response: ApiResponse<ResponseObject>) {
        when(response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                Log.d(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage, shouldUseDialog = true, shouldUseToast = false)
            }
            is ApiEmptyResponse -> {
                Log.d(TAG, "NetworkBoundResource: request returns nothing HTTP 204")
                onErrorReturn("HTTP 204 error", shouldUseDialog = true, shouldUseToast = false)
            }
        }
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {
        var msg = errorMessage
        var useDialog = shouldUseDialog
        var responseType: ResponseType = ResponseType.None()
        if(msg == null) {
            msg = ERROR_UNKNOWN
        }else if(ErrorHandling.isNetworkError(msg)){
            msg = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }
        if(shouldUseToast) {
            responseType = ResponseType.Toast()
        }
        if(useDialog) {
            responseType = ResponseType.Dialog()
        }
        onCompleteJob(DataState.error(Response(msg, responseType)))
    }

    @OptIn(InternalCoroutinesApi::class)
    protected fun initNewJob() : Job {
        Log.d(TAG, "initNewJob: called")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler {
            override fun invoke(cause: Throwable?) {
                if(job.isCancelled) {
                    Log.d(TAG, "NetworkBoundResource: Job has been canceled")
                    cause?.let {
                        onErrorReturn(it.message, shouldUseDialog = false, shouldUseToast = true)
                    } ?: onErrorReturn(ERROR_UNKNOWN, shouldUseDialog = false, shouldUseToast = true)
                }else if(job.isCompleted) {
                    Log.d(TAG, "NetworkBoundResource: Job has been completed")
                }
            }
        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall() : LiveData<ApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)

}