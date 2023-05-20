package com.example.example

import com.google.gson.annotations.SerializedName


data class Offer(

    @SerializedName("offer_id") var offerId: Int? = null,
    @SerializedName("p_id") var pId: Int? = null,
    @SerializedName("offertitle") var offertitle: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("s_date") var sDate: String? = null,
    @SerializedName("e_date") var eDate: String? = null,
    @SerializedName("status") var status: Int? = null

)