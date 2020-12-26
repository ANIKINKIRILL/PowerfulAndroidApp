package com.anikinkirill.powerfulandroidapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anikinkirill.powerfulandroidapp.api.main.responses.BlogSearchResponse
import com.anikinkirill.powerfulandroidapp.util.DateUtils

@Entity(tableName = "blog_post")
data class BlogPost(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "slug")
    var slug: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "date_updated")
    var date_updated: Long,

    @ColumnInfo(name = "username")
    var username: String
) {

    constructor(response: BlogSearchResponse): this (
        response.pk,
        response.title,
        response.slug,
        response.body,
        response.image,
        DateUtils.convertServerStringDateToLong(response.date_updated),
        response.username
    )

    override fun toString(): String {
        return "BlogPost(pk=$pk, " +
                "title='$title', " +
                "slug='$slug', " +
                "image='$image', " +
                "date_updated=$date_updated, " +
                "username='$username')"
    }
}