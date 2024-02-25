package com.example.example

import com.google.gson.annotations.SerializedName
import matemart.material.interior.model.SearchWord


data class ResponseSearch(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: SearchWord= SearchWord()

)