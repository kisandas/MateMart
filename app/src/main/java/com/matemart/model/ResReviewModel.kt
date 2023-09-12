package com.matemart.model

import com.google.gson.annotations.SerializedName


data class ResReviewModel(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)

data class Data(

    @SerializedName("review_total") var reviewTotal: Int? = null,
    @SerializedName("avg_rating") var avgRating: Double? = null,
    @SerializedName("review") var review: ArrayList<Review> = arrayListOf(),
    @SerializedName("product") var product: Products? = Products()

)


data class Products(

    @SerializedName("p_name") var pName: String? = null,
    @SerializedName("image") var image: String? = null

)