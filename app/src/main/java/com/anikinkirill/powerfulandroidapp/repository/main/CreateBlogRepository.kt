package com.anikinkirill.powerfulandroidapp.repository.main

import com.anikinkirill.powerfulandroidapp.api.main.OpenApiMainService
import com.anikinkirill.powerfulandroidapp.persitence.BlogPostDao
import com.anikinkirill.powerfulandroidapp.repository.JobManager
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import javax.inject.Inject

class CreateBlogRepository
@Inject constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("CreateBlogRepository") {
    companion object {
        private const val TAG = "AppDebug_CreateBlogRepository"
    }
}