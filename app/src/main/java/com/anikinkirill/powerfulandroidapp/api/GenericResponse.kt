package com.anikinkirill.powerfulandroidapp.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericResponse(
    @SerializedName("response")
    @Expose
    val response: String
)