package com.anikinkirill.powerfulandroidapp.ui

import android.util.Log
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangeListener {

    @Inject
    lateinit var sessionManager: SessionManager

    companion object {
        const val TAG = "AppDebug_BaseActivity"
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let { mDataState ->
            GlobalScope.launch(Main) {

                // LOADING
                displayProgressBar(mDataState.loading.isLoading)

                // ERROR
                mDataState.error?.let { errorEvent ->
                    handleErrorState(errorEvent)
                }

                // DATA
                mDataState.data?.let { data ->
                    data.response?.let { responseEvent ->
                        handleResponseState(responseEvent)
                    }
                }
            }
        }
    }

    private fun handleErrorState(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when(it.response.responseType) {
                is ResponseType.Dialog -> {
                    it.response.message?.let { message ->
                        displayErrorDialog(message)
                    }
                }
                is ResponseType.Toast -> {
                    it.response.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.None -> {
                    Log.d(TAG, "handleErrorState: called")
                }
            }
        }
    }

    private fun handleResponseState(responseEvent: Event<Response>) {
        responseEvent.getContentIfNotHandled()?.let {
            when(it.responseType) {
                is ResponseType.Dialog -> {
                    it.message?.let { message ->
                        displaySuccessDialog(message)
                    }
                }
                is ResponseType.Toast -> {
                    it.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.None -> {
                    Log.d(TAG, "handleErrorState: called")
                }
            }
        }
    }

    abstract fun displayProgressBar(isLoading: Boolean)

}