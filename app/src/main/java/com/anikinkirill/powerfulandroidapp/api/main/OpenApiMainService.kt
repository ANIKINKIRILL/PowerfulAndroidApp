package com.anikinkirill.powerfulandroidapp.api.main

import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.api.GenericResponse
import com.anikinkirill.powerfulandroidapp.api.main.responses.BlogListSearchResponse
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import retrofit2.http.*

interface OpenApiMainService {

    @GET("api/account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<ApiResponse<AccountProperties>>

    @PUT("api/account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): LiveData<ApiResponse<GenericResponse>>

    @PUT("api/account/change_password/")
    @FormUrlEncoded
    fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): LiveData<ApiResponse<GenericResponse>>

    @GET("api/blog/list")
    fun searchListBlogPosts(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): LiveData<ApiResponse<BlogListSearchResponse>>

    @GET("api/blog/{slug}/is_author")
    fun isAuthorOfBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): LiveData<ApiResponse<GenericResponse>>

    @DELETE("api/blog/{slug}/delete")
    fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): LiveData<ApiResponse<GenericResponse>>
}