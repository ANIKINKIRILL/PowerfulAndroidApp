package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.repository.main.BlogRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.BaseViewModel
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.*
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class BlogViewModel
@Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val requestManager: RequestManager
) : BaseViewModel<BlogStateEvent, BlogViewState>() {

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    override fun handleStateEvent(event: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when (event) {
            is BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { token ->
                    blogPostRepository.searchBlogPosts(
                        token,
                        viewState.value!!.blogFields.searchQuery
                    )
                } ?: AbsentLiveData.create()
            }

            is CheckAuthorOfBlogPost -> {
               return AbsentLiveData.create()
            }

            is None -> AbsentLiveData.create()
        }
    }

    fun setQuery(query: String) {
        val update = getCurrentViewStateOrNew()
        if (query == update.blogFields.searchQuery) {
            return
        }
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun setBlogPostList(blogList: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogFields.blogList = blogList
        _viewState.value = update
    }

    fun setBlogPost(blogPost: BlogPost) {
        val update = getCurrentViewStateOrNew()
        update.viewBlogFields.blogPost = blogPost
        _viewState.value = update
    }

    fun setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
        _viewState.value = update
    }

    fun cancelActiveJobs() {
        blogPostRepository.cancelActiveJobs()
        handlePendingData()
    }

    private fun handlePendingData() {
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}