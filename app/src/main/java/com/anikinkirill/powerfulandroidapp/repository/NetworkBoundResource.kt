package com.anikinkirill.powerfulandroidapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.Response
import com.anikinkirill.powerfulandroidapp.ui.ResponseType
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean // is there a network connection ?
) {

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

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

}