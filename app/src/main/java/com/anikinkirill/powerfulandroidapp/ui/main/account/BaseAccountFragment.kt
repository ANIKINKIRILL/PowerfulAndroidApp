package com.anikinkirill.powerfulandroidapp.ui.main.account

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.anikinkirill.powerfulandroidapp.ui.DataStateChangeListener
import dagger.android.support.DaggerFragment

@SuppressLint("LongLogTag")
abstract class BaseAccountFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseAccountFragment"
    }

    lateinit var onDataStateChangeListener: DataStateChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onDataStateChangeListener = context as DataStateChangeListener
        }catch (e: Exception) {
            Log.d(TAG, "onAttach: ${e.message}")
        }
    }

}