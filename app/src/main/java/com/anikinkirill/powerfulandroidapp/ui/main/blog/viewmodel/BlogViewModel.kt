package com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.persitence.BlogQueryUtils
import com.anikinkirill.powerfulandroidapp.repository.main.BlogRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.BaseViewModel
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.Loading
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.*
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import com.anikinkirill.powerfulandroidapp.util.PreferenceKeys.Companion.BLOG_FILTER
import com.anikinkirill.powerfulandroidapp.util.PreferenceKeys.Companion.BLOG_ORDER
import javax.inject.Inject

class BlogViewModel
@Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<BlogStateEvent, BlogViewState>() {

    init {
        setBlogFilter(
            sharedPreferences.getString(
                BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )

        sharedPreferences.getString(
            BLOG_ORDER,
            BlogQueryUtils.BLOG_ORDER_ASC
        )?.let { blogOrder ->
            setBlogOrder(
                blogOrder
            )
        }
    }

    override fun initNewViewState(): BlogViewState {
        return BlogViewState()
    }

    override fun handleStateEvent(event: BlogStateEvent): LiveData<DataState<BlogViewState>> {
        return when (event) {
            is BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { token ->
                    blogPostRepository.searchBlogPosts(
                        token,
                        getSearchQuery(),
                        getBlogOrder() + getBlogFilter(),
                        getPage()
                    )
                } ?: AbsentLiveData.create()
            }

            is CheckAuthorOfBlogPost -> {
               sessionManager.cachedToken.value?.let { token ->
                   blogPostRepository.isAuthorOfBlogPost(
                       token,
                       getSlug()
                   )
               } ?: AbsentLiveData.create()
            }

            is DeleteBlogPostEvent -> {
                sessionManager.cachedToken.value?.let { token ->
                    blogPostRepository.deleteBlogPost(
                        token,
                        getBlogPost()
                    )
                } ?: AbsentLiveData.create()
            }

            is None -> {
                return object : LiveData<DataState<BlogViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState(
                            null,
                            Loading(false),
                            null
                        )
                    }
                }
            }
        }
    }

    fun saveFilterOptions(filter: String, order: String) {
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
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