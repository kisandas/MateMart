package com.matemart.model

import com.google.gson.annotations.SerializedName
import com.matemart.model.CartItem


data class ResponseOrderTrackingModel(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: TrackingData? = TrackingData()

)

data class OrderInfo(

    @SerializedName("o_id") var oId: Int? = null,
    @SerializedName("order_number") var orderNumber: String? = null,
    @SerializedName("tracking_id") var trackingId: String? = null

)


data class OrderStatus(

    @SerializedName("o_d_id") var oDId: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("date") var date: String? = null

)

data class TrackingData(

    @SerializedName("o_id") var oId: Int? = null,
    @SerializedName("o_d_id") var oDId: Int? = null,
    @SerializedName("order_info") var orderInfo: OrderInfo? = OrderInfo(),
    @SerializedName("order_status") var orderStatus: ArrayList<OrderStatus> = arrayListOf()

)