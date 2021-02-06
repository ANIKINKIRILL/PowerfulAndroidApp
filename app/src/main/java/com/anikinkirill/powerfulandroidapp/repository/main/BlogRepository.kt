package com.anikinkirill.powerfulandroidapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.anikinkirill.powerfulandroidapp.api.GenericResponse
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
import com.anikinkirill.powerfulandroidapp.ui.Response
import com.anikinkirill.powerfulandroidapp.ui.ResponseType
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState.BlogFields
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState.ViewBlogFields
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import com.anikinkirill.powerfulandroidapp.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.anikinkirill.powerfulandroidapp.util.SuccessHandling.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.anikinkirill.powerfulandroidapp.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
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
                                    blogFields = BlogFields(
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

    fun isAuthorOfBlogPost(
        authToken: AuthToken,
        slug: String
    ): LiveData<DataState<BlogViewState>> {
        return object : NetworkBoundResource<GenericResponse, Any, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<GenericResponse>) {
                withContext(Dispatchers.Main) {
                    var isAuthor = false
                    if (response.body.response == RESPONSE_HAS_PERMISSION_TO_EDIT) {
                        isAuthor = true
                    }
                    onCompleteJob(
                        DataState.data(
                            data = BlogViewState(
                                viewBlogFields = ViewBlogFields(
                                    isAuthorOfBlogPost = isAuthor
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<ApiResponse<GenericResponse>> {
                return openApiMainService.isAuthorOfBlogPost(
                    authorization = "Token ${authToken.token}",
                    slug = slug
                )
            }

            override fun setJob(job: Job) {
                addJob("isAuthorOfBlogPost", job)
            }

            // not used in this case
            override fun loadFromCache(): LiveData<BlogViewState> {
                TODO("Not yet implemented")
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }
        }.asLiveData()
    }

    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): LiveData<DataState<BlogViewState>> {
        return object : NetworkBoundResource<GenericResponse, BlogPost, BlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<GenericResponse>) {
                if (response.body.response == SUCCESS_BLOG_DELETED) {
                    updateLocalDb(blogPost)
                } else {
                    onCompleteJob(
                        DataState.error(
                            response = Response(
                                ERROR_UNKNOWN,
                                ResponseType.Dialog()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<ApiResponse<GenericResponse>> {
               return openApiMainService.deleteBlogPost(
                    authorization = "Token ${authToken.token}",
                    slug = blogPost.slug
                )
            }

            override fun setJob(job: Job) {
                addJob("deleteBlogPost", job)
            }

            // not used in this case
            override fun loadFromCache(): LiveData<BlogViewState> {
                TODO("Not yet implemented")
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let { blogPost ->
                    blogPostDao.deleteBlogPost(blogPost)
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(
                                SUCCESS_BLOG_DELETED,
                                ResponseType.Toast()
                            )
                        )
                    )
                }
            }
        }.asLiveData()
    }
}