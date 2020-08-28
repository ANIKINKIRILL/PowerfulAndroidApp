package com.anikinkirill.powerfulandroidapp.util

class ErrorHandling {

    companion object {
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."

        const val ERROR_UNKNOWN = "Unknown error"

        fun isNetworkError(msg: String): Boolean{
            return when {
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
                else -> false
            }
        }
    }

}