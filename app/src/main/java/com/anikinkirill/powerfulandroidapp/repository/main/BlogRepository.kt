package com.anikinkirill.powerfulandroidapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.anikinkirill.powerfulandroidapp.api.main.OpenApiMainService
import com.anikinkirill.powerfulandroidapp.api.main.responses.BlogListSearchResponse
import com.anikinkirill.powerfulandroidapp.models.AuthToken
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.persitence.BlogPostDao
import com.anikinkirill.powerfulandroidapp.persitence.returnOrderedBlogQuery
import com.anikinkirill.powerfulandroidapp.repository.JobManager
import com.anikinkirill.powerfulandroidapp.repository.NetworkBoundResource
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import com.anikinkirill.powerfulandroidapp.util.Constants.Companion.PAGINATION_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogRepository
@Inject constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
) : JobManager("BlogRepository") {

    companion object {
        private const val TAG = "AppDebug_BlogRepository"
    }

    fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
    ): LiveData<DataState<BlogViewState>> {
        return object : NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main) {
                    result.addSource(loadFromCache()) {
                        it.blogFields.isQueryInProgress = false
                        if (page * PAGINATION_PAGE_SIZE > it.blogFields.blogList.size) {
                            it.blogFields.isQueryExhausted = true
                        }
                        onCompleteJob(DataState.data(it, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<BlogListSearchResponse>) {
                val blogPostList = response.body.results.map {
                    BlogPost(it)
                }
                updateLocalDb(blogPostList)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<ApiResponse<BlogListSearchResponse>> {
                return openApiMainService.searchListBlogPosts(
                    "Token ${authToken.token}",
                    query,
                    filterAndOrder,
                    page
                )
            }

            override fun setJob(job: Job) {
                addJob("searchBlogPosts", job)
            }

            override fun loadFromCache(): LiveData<BlogViewState> {
                Log.d(TAG, "loadFromCache: filter and order: $filterAndOrder")
                return blogPostDao.returnOrderedBlogQuery(
                    query, filterAndOrder, page
                )
                    .switchMap {
                        object : LiveData<BlogViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = BlogViewState(
                                    blogFields = BlogViewState.BlogFields(
                                        blogList = it,
                                        isQueryInProgress = true
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                cacheObject?.forEach {
                    withContext(Dispatchers.IO) {
                        try {
                            // inserting blog parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting a blog: $it")
                                blogPostDao.insert(it)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "updateLocalDb: error updating cache with slug: ${it.slug}")
                        }
                    }
                }
            }
        }.asLiveData()
    }

}