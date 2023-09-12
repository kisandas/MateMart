package com.matemart.model

import com.google.gson.annotations.SerializedName


data class CartResponseModel(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<CartDataModel> = arrayListOf()

)