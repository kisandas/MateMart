package com.example.example

import com.google.gson.annotations.SerializedName
import com.matemart.model.CartItem


data class AddCartResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: CartItem? = null

)