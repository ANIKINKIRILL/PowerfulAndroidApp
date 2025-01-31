package com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel

import android.net.Uri
import com.anikinkirill.powerfulandroidapp.models.BlogPost

fun BlogViewModel.setQuery(query: String) {
    val update = getCurrentViewStateOrNew()
    if (query == update.blogFields.searchQuery) {
        return
    }
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun BlogViewModel.setBlogPostList(blogList: List<BlogPost>) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

fun BlogViewModel.setBlogPost(blogPost: BlogPost) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)
}

fun BlogViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
    setViewState(update)
}

fun BlogViewModel.setQueryExhausted(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun BlogViewModel.setQueryInProgress(isInProgress: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun BlogViewModel.setBlogFilter(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.blogFields.filter = filter
        setViewState(update)
    }
}

fun BlogViewModel.setBlogOrder(order: String) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.order = order
    setViewState(update)
}

fun BlogViewModel.removeDeletedBlogPost() {
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for (blog in list) {
        if (blog.slug == getBlogPost().slug) {
            list.remove(blog)
            break
        }
    }
    setBlogPostList(list)
}

fun BlogViewModel.setUpdatedBlogFields(
    title: String?,
    body: String?,
    image: Uri?
) {
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updateBlogFields
    title?.let { updatedBlogFields.updatedTitle = it }
    body?.let { updatedBlogFields.updatedBody = it }
    image?.let { updatedBlogFields.updatedImageUri = it }
    update.updateBlogFields = updatedBlogFields
    setViewState(update)
}

fun BlogViewModel.updateListItem(newBlogPost: BlogPost) {
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for (i in 0 until list.size) {
        if (list[i].pk == newBlogPost.pk) {
            list[i] = newBlogPost
            break
        }
    }
    update.blogFields.blogList = list
    setViewState(update)
}

fun BlogViewModel.onBlogPostUpdateSuccess(blogPost: BlogPost) {
    setUpdatedBlogFields(
        title = blogPost.title,
        body = blogPost.body,
        image = null
    ) // for updating UpdateBlogFragment
    setBlogPost(blogPost) // for updating ViewBlogFragment
    updateListItem(blogPost) // for updating BlogFragment
}