package com.matemart.model

import com.example.example.Offer
import com.google.gson.annotations.SerializedName


data class CartItem(

    @SerializedName("qty") var qty: Int? = null,
    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("sample") var sample: Int? = null,
    @SerializedName("sku_id") var skuId: String? = null,
    @SerializedName("gstper") var gstper: Int? = null,
    @SerializedName("product_image") var productImage: String? = null,
    @SerializedName("saleprice") var saleprice: Int? = null,
    @SerializedName("p_name") var pName: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("p_id") var pId: Int? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null,
    @SerializedName("offer") var offer: Offer? = Offer()

)