package com.example.example

import com.google.gson.annotations.SerializedName


data class CategoryModel(

    @SerializedName("title") var title: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("c_id") var cId: Int? = null,
    @SerializedName("color") var color: String? = null

)