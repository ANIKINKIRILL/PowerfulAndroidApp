package com.anikinkirill.powerfulandroidapp.api.main

import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface OpenApiMainService {

    @GET("api/account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<ApiResponse<AccountProperties>>

}