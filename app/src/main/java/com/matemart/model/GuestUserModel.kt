package com.matemart.model

import com.google.gson.annotations.SerializedName
import com.matemart.model.CartItem


data class GuestUserModel(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: GuestUserData? = GuestUserData()

)

data class GuestUserData(

    @SerializedName("api_token") var apiToken: String? = null

)