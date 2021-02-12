package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent
import kotlinx.android.synthetic.main.fragment_update_blog.*
import okhttp3.MultipartBody

class UpdateBlogFragment : BaseBlogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.getContentIfNotHandled()?.let { viewState ->
                    viewState.viewBlogFields.blogPost?.let { blogPost ->
                        // TODO("onBlogPostUpdateSuccess")
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.updateBlogFields.let { updateBlogFields ->
                setBlogProperties(
                    updateBlogFields.updatedTitle,
                    updateBlogFields.updatedBody,
                    updateBlogFields.updatedImageUri
                )
            }
        })
    }

    private fun setBlogProperties(
        updatedTitle: String?,
        updatedBody: String?,
        updatedImageUri: Uri?
    ) {
        requestManager.load(updatedImageUri).into(blog_image)
        blog_title.setText(updatedTitle)
        blog_body.setText(updatedBody)
    }

    private fun saveChanges() {
        val multipartBody: MultipartBody.Part? = null
        viewModel.setStateEvent(BlogStateEvent.UpdatedBlogPostEvent(
            blog_title.text.toString(),
            blog_body.text.toString(),
            multipartBody
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}