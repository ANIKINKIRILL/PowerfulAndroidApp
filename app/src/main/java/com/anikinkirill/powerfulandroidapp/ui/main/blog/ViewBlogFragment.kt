package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.ui.AreYouSureCallback
import com.anikinkirill.powerfulandroidapp.ui.UIMessage
import com.anikinkirill.powerfulandroidapp.ui.UIMessageType
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.CheckAuthorOfBlogPost
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.DeleteBlogPostEvent
import com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel.*
import com.anikinkirill.powerfulandroidapp.util.DateUtils
import com.anikinkirill.powerfulandroidapp.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
import kotlinx.android.synthetic.main.fragment_view_blog.*

@SuppressLint("LongLogTag")
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
        checkIsAuthorOfBlogPost()
        onDataStateChangeListener.expandAppBar()

        delete_button.setOnClickListener {
            confirmDeleteRequest()
        }
    }

    private fun checkIsAuthorOfBlogPost() {
        viewModel.setIsAuthorOfBlogPost(false) // reset
        viewModel.setStateEvent(CheckAuthorOfBlogPost())
    }

    private fun deleteBlogPost() {
        viewModel.setStateEvent(DeleteBlogPostEvent())
    }

    private fun confirmDeleteRequest() {
        val callback = object : AreYouSureCallback {
            override fun proceed() {
                deleteBlogPost()
            }
            override fun cancel() {
                // ignore this case
            }
        }
        uiCommunicationListener.onUIMessageReceived(
            uiMessage = UIMessage(
                message = getString(R.string.are_you_sure_delete),
                uiMessageType = UIMessageType.AreYouSureDialog(
                    callback
                )
            )
        )
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.getContentIfNotHandled()?.let { viewState ->
                    viewModel.setIsAuthorOfBlogPost(
                        viewState.viewBlogFields.isAuthorOfBlogPost
                    )
                }
                data.response?.peekContent()?.let { response ->
                    if (response.message == SUCCESS_BLOG_DELETED) {
                        viewModel.removeDeletedBlogPost()
                        findNavController().popBackStack()
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewBlogFields.blogPost?.let { blogPost ->
                setBlogPostProperties(blogPost)
            }
            if (viewState.viewBlogFields.isAuthorOfBlogPost) {
                adaptViewToAuthorMode()
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

    private fun adaptViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if(viewModel.isAuthorOfBlogPost()) {
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(viewModel.isAuthorOfBlogPost()) {
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
        try {
            viewModel.setUpdatedBlogFields(
                title = viewModel.getBlogPost().title,
                body = viewModel.getBlogPost().body,
                image = viewModel.getBlogPost().image.toUri()
            )
            findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.message}")
        }
    }

}