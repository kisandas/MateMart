package com.matemart.model

import com.example.example.RecentSearchData
import com.google.gson.annotations.SerializedName

data class ResponseRecentSearch(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: RecentSearchData? = RecentSearchData()

)