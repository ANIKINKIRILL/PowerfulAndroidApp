package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.util.DateUtils
import kotlinx.android.synthetic.main.fragment_view_blog.*

class ViewBlogFragment : BaseBlogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewBlogFields.blogPost?.let { blogPost ->
                setBlogPostProperties(blogPost)
            }
        })
    }

    private fun setBlogPostProperties(blogPost: BlogPost) {
        with(blogPost) {
            requestManager.load(image).into(blog_image)
            blog_title.text = title
            blog_author.text = username
            blog_update_date.text = DateUtils.convertLongToStringDate(date_updated)
            blog_body.text = body
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // TODO("Check if current user if the author of the blog")
        val isAuthorOfBlogPost = true
        if(isAuthorOfBlogPost) {
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO("Check if current user if the author of the blog")
        val isAuthorOfBlogPost = true
        if(isAuthorOfBlogPost) {
            when(item.itemId) {
                R.id.edit -> {
                    navigateToUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToUpdateBlogFragment() {
        findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
    }

}