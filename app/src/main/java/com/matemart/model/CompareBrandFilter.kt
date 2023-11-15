package com.matemart.model

import com.google.gson.annotations.SerializedName
import com.matemart.model.CartItem


data class CompareBrandFilter(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: CompareData? = CompareData()

)


data class AddtocartData(

    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("total_qty") var totalQty: String? = null,
    @SerializedName("amount") var amount: Double? = null

)

data class CompareResult(

    @SerializedName("brand_name") var brandName: String? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("addtocartdata") var addtocartdata: ArrayList<AddtocartData> = arrayListOf(),
    @SerializedName("total_qty") var totalQty: String? = null,
    @SerializedName("not_compare") var notCompare: ArrayList<String> = arrayListOf(),
    @SerializedName("compare_data") var compareData: ArrayList<String> = arrayListOf()

)


data class CompareData(

    @SerializedName("compare_result") var compareResult: ArrayList<CompareResult> = arrayListOf(),
    @SerializedName("compare_variation_data") var compareVariationData: ArrayList<String> = arrayListOf()

)