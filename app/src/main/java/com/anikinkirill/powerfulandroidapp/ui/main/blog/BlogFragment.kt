package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import kotlinx.android.synthetic.main.fragment_blog.*

class BlogFragment : BaseBlogFragment() {

    companion object {
        private const val TAG = "AppDebug_BlogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goViewBlogFragment.setOnClickListener {
            findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        }

        subscribeObservers()
        executeSearch()
    }

    private fun executeSearch() {
        viewModel.setQuery(" ")
        viewModel.setStateEvent(BlogSearchEvent())
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                onDataStateChangeListener.onDataStateChange(it)
                it.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { viewState ->
                            viewModel.setBlogPostList(viewState.blogFields.blogList)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "BlogFragment, ViewState: $viewState")
        })
    }
}