package com.example.example

import com.google.gson.annotations.SerializedName
import com.matemart.model.SearchWord
import com.matemart.model.ViewListModel


data class ResponseSearch(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: SearchWord= SearchWord()

)