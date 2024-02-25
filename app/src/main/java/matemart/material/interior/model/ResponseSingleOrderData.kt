package matemart.material.interior.model

import com.google.gson.annotations.SerializedName


data class ResponseSingleOrderData(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: SingleOrderItem? = null

)


data class SingleOrderItem(

    @SerializedName("o_id") var oId: Int? = null,
    @SerializedName("payment_method") var paymentMethod: Int? = null,
    @SerializedName("tracking_id") var trackingId: String? = null,
    @SerializedName("order_number") var orderNumber: String? = null,
    @SerializedName("a_id") var aId: Int? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("promocode_amount") var promocodeAmount: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("invoice_url") var invoiceUrl: String? = null,
    @SerializedName("order_detail") var orderDetail: ArrayList<OrderDetail> = arrayListOf(),
    @SerializedName("address") var address: OrderAddress? = OrderAddress()

)

data class OrderDetail(

    @SerializedName("o_id") var oId: Int? = null,
    @SerializedName("o_d_id") var oDId: Int? = null,
    @SerializedName("odstatus") var odstatus: Int? = null,
    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("qty") var qty: Int? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("tax") var tax: Int? = null,
    @SerializedName("discount_amount") var discountAmount: Double? = null,
    @SerializedName("total_price") var totalPrice: Double? = null,
    @SerializedName("product_detail") var productDetail: ProductDetail? = ProductDetail()

)


data class ProductDetail(

    @SerializedName("p_id") var pId: Int? = null,
    @SerializedName("saleprice") var saleprice: String? = null,
    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("total_quantity") var totalQuantity: Int? = null,
    @SerializedName("p_name") var pName: String? = null,
    @SerializedName("image") var image: String? = null

)


data class OrderAddress(

    @SerializedName("a_id") var aId: Int? = null,
    @SerializedName("fullname") var fullname: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("a_mobile") var aMobile: String? = null

)