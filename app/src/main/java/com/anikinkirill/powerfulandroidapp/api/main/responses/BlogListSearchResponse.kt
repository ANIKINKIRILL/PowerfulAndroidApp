package com.anikinkirill.powerfulandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogListSearchResponse(
    @SerializedName("results")
    @Expose
    val results: List<BlogSearchResponse>,

    @SerializedName("detail")
    @Expose
    val detail: String
) {

    override fun toString(): String {
        return "BlogListSearchResponse(results=$results, detail='$detail')"
    }
}