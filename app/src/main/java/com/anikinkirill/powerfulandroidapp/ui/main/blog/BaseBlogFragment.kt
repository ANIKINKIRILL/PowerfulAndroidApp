package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.anikinkirill.powerfulandroidapp.ui.DataStateChangeListener
import dagger.android.support.DaggerFragment

@SuppressLint("LongLogTag")
abstract class BaseBlogFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseBlogFragment"
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