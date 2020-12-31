package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.ui.main.blog.BlogListAdapter.Interaction
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import com.anikinkirill.powerfulandroidapp.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*

class BlogFragment : BaseBlogFragment(), Interaction {

    companion object {
        private const val TAG = "AppDebug_BlogFragment"
    }

    private lateinit var recyclerAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBlogListRecyclerView()
        subscribeObservers()
        executeSearch()
    }

    private fun initBlogListRecyclerView() {
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)
            recyclerAdapter = BlogListAdapter(
                interaction = this@BlogFragment,
                requestManager = requestManager
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "Loading next page ...")
                    }
                }
            })

            adapter = recyclerAdapter
        }
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
            viewState?.let {
                recyclerAdapter.submitList(
                    list = it.blogFields.blogList,
                    isQueryExhausted = true
                )
            }
        })
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks when view is destroyed
        blog_post_recyclerview.adapter = null
    }
}