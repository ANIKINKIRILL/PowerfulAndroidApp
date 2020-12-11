package com.anikinkirill.powerfulandroidapp.ui.main.blog.state

import com.anikinkirill.powerfulandroidapp.models.BlogPost

data class BlogViewState(
    // BlogFragment vars
    var blogFields: BlogFields = BlogFields()

    // ViewBlogFragment vars

    // UpdateBlogFragment vars
) {

    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = ""
    )

}