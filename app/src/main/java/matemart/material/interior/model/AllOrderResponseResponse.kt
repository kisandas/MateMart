package matemart.material.interior.model

import com.google.gson.annotations.SerializedName


data class AllOrderResponseResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<OrderData> = arrayListOf()

)

data class Content(

    @SerializedName("o_id") var oId: Int? = null,
    @SerializedName("order_number") var orderNumber: String? = null,
    @SerializedName("tracking_id") var trackingId: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("qty") var qty: Int? = null,
    @SerializedName("odstatus") var odstatus: Int? = null

)


data class OrderData(

    @SerializedName("groupID") var groupID: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("content") var content: ArrayList<Content> = arrayListOf()

)