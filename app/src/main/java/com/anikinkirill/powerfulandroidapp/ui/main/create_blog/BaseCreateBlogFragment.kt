package com.anikinkirill.powerfulandroidapp.ui.main.create_blog

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.anikinkirill.powerfulandroidapp.ui.DataStateChangeListener
import dagger.android.support.DaggerFragment
import java.lang.Exception

@SuppressLint("LongLogTag")
abstract class BaseCreateBlogFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseCreateBlogFragment"
    }

    lateinit var onDataStateChangeListener: DataStateChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as DataStateChangeListener
        }catch (e: Exception) {
            Log.d(TAG, "onAttach: ${e.message}")
        }
    }

}