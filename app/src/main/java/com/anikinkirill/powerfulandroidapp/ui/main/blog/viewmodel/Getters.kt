package com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel

import com.anikinkirill.powerfulandroidapp.models.BlogPost

fun BlogViewModel.getIsQueryExhausted(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryExhausted
    }
}

fun BlogViewModel.getIsQueryInProgress(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.blogFields.isQueryInProgress
    }
}

fun BlogViewModel.getPage(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.page
    }
}

fun BlogViewModel.getSearchQuery(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.searchQuery
    }
}

fun BlogViewModel.getBlogFilter(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.filter
    }
}

fun BlogViewModel.getBlogOrder(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.order
    }
}

fun BlogViewModel.getSlug(): String {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.slug ?: ""
    }
}

fun BlogViewModel.isAuthorOfBlogPost(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.isAuthorOfBlogPost
    }
}

fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost ?: getDummyBlogPost()
    }
}

private fun getDummyBlogPost(): BlogPost {
    return BlogPost(
        -1,
        "",
        "",
        "",
        "",
        1,
        ""
    )
}