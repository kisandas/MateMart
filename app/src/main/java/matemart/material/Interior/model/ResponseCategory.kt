package com.example.example

import com.google.gson.annotations.SerializedName


data class ResponseCategory(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<CategoryModel> = arrayListOf()

)