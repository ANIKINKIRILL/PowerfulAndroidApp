package com.anikinkirill.powerfulandroidapp.api.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error_message")
    @Expose
    var error_message: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("token")
    @Expose
    var token: String

)