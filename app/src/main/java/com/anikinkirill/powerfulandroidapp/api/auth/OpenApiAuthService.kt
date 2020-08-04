package com.anikinkirill.powerfulandroidapp.api.auth

import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OpenApiAuthService {

    @POST("api/account/login")
    @FormUrlEncoded
    fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ) : LiveData<ApiResponse<LoginResponse>>

    @POST("api/account/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ) : LiveData<ApiResponse<RegistrationResponse>>

}