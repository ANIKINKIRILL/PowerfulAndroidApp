package com.anikinkirill.powerfulandroidapp.ui.main.create_blog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.DataStateChangeListener
import dagger.android.support.DaggerFragment

@SuppressLint("LongLogTag")
abstract class BaseCreateBlogFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseCreateBlogFragment"
    }

    lateinit var onDataStateChangeListener: DataStateChangeListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.createBlogFragment, activity as AppCompatActivity)
    }

    private fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as DataStateChangeListener
        }catch (e: Exception) {
            Log.d(TAG, "onAttach: ${e.message}")
        }
    }

}