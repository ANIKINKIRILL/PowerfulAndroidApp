package com.anikinkirill.powerfulandroidapp.repository

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.anikinkirill.powerfulandroidapp.ui.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean // is there a network connection ?
) {

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    @OptIn(InternalCoroutinesApi::class)
    protected fun initNewJob() : Job {
        Log.d(TAG, "initNewJob: called")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler {
            override fun invoke(cause: Throwable?) {
                if(job.isCancelled) {
                    Log.d(TAG, "NetworkBoundResource: Job has been canceled")
                    cause?.let {
                        // TODO("show error dialog")
                    }
                }else if(job.isCompleted) {
                    Log.d(TAG, "NetworkBoundResource: Job has been completed")
                }
            }
        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

}