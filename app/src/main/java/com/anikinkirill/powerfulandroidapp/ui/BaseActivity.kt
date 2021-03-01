package com.anikinkirill.powerfulandroidapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.UIMessageType.*
import com.anikinkirill.powerfulandroidapp.util.Constants
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangeListener, UICommunicationListener {

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

                // RESPONSE
                mDataState.data?.let { data ->
                    data.response?.let { responseEvent ->
                        handleResponseState(responseEvent)
                    }
                }
            }
        }
    }

    override fun onUIMessageReceived(uiMessage: UIMessage) {
        when (uiMessage.uiMessageType) {
            is AreYouSureDialog -> {
                displayAreYouSureDialog(
                    uiMessage.message,
                    uiMessage.uiMessageType.callback
                )
            }
            is Toast -> {
                displayToast(uiMessage.message)
            }
            is Dialog -> {
                displayInfoDialog(uiMessage.message)
            }
            is None -> {
                Log.d(TAG, "onUIMessageReceived: ${uiMessage.message}")
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

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun isStoragePermissionGranted(): Boolean {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.PERMISSION_REQUEST_READ_STORAGE
            )
            return false
        } else {
            return true
        }
    }
}