package com.matemart.model

import com.google.gson.annotations.SerializedName


data class CartDataModel(

    @SerializedName("qty") var qty: Int? = null,
    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("sample") var sample: Int? = null,
    @SerializedName("sku_id") var skuId: String? = null,
    @SerializedName("total_quantity") var totalQuantity: Int? = null,
    @SerializedName("gstper") var gstper: Int? = null,
    @SerializedName("product_image") var productImage: String? = null,
    @SerializedName("saleprice") var saleprice: String? = null,
    @SerializedName("p_name") var pName: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("is_compare") var isCompare: Int? = null,
    @SerializedName("is_available") var isAvailable: Int? = null,
    @SerializedName("out_of_stock_status") var outOfStockStatus: Int? = null,
    @SerializedName("p_id") var pId: Int? = null,
    @SerializedName("offer") var offer: Offer? = Offer()

)