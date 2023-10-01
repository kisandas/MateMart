package com.matemart.model

import com.google.gson.annotations.SerializedName
import com.matemart.model.ViewListModel


data class ResponseProductList(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<ViewListModel> = arrayListOf()

)