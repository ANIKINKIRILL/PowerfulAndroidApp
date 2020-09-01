package com.anikinkirill.powerfulandroidapp.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.persitence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(val authTokenDao: AuthTokenDao, val application: Application) {

    companion object {
        private const val TAG = "AppDebug_SessionManager"
    }

    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken get() = _cachedToken

    fun login(newValue: AuthToken) {
        Log.d(TAG, "login: called")
        setValue(newValue)
    }

    private fun setValue(value: AuthToken?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (_cachedToken.value != value) {
                _cachedToken.value = value
            }
        }
    }

    fun logout() {
        Log.d(TAG, "logout: called")
        GlobalScope.launch(Dispatchers.IO) {
            var errorMessage: String? = null
            try {
                _cachedToken.value!!.account_pk?.let {
                    authTokenDao.nullifyToken(it)
                }
            }catch (e: CancellationException) {
                Log.d(TAG, "logout: ${e.message}")
                errorMessage = e.message
            }catch (e: Exception) {
                Log.d(TAG, "logout: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            }finally {
                errorMessage?.let {
                    Log.d(TAG, "logout: $it")
                }
                Log.d(TAG, "logout: finally")
                setValue(null)
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try{
            return cm.activeNetworkInfo.isConnected
        }catch (e: Exception){
            Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }

}