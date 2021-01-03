package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.DataStateChangeListener
import com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel.BlogViewModel
import com.anikinkirill.powerfulandroidapp.viewmodels.ViewModelProviderFactory
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@SuppressLint("LongLogTag")
abstract class BaseBlogFragment : DaggerFragment() {

    companion object {
        const val TAG = "AppDebug_BaseBlogFragment"
    }

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: BlogViewModel

    lateinit var onDataStateChangeListener: DataStateChangeListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.blogFragment, activity as AppCompatActivity)
        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(BlogViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        cancelActiveJobs()
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
        try{
            onDataStateChangeListener = context as DataStateChangeListener
        }catch (e: Exception) {
            Log.d(TAG, "onAttach: ${e.message}")
        }
    }

    fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }

}