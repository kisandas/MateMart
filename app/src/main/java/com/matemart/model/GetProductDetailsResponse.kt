package com.matemart.model

import com.google.gson.annotations.SerializedName


data class GetProductDetailsResponse(
    @SerializedName("statuscode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProductData

)

data class ProductData(
    val product: Product,
    val review_total: Int,
    val rating: Double,
    val avg_rating: Double,
    val ratings: List<Rating>,
    val review: List<Review>,
    val filtervariation_data: HashMap<String, List<String>>,
    val variation_name: List<String>,
    val variation: HashMap<String, List<String>>,
    val variation_data: VariationData
)

data class Product(
    val p_id: Int,
    val product_detail_id: Int,
    val p_name: String,
    val description: String,
    val saleprice: String,
    val price: String,
    val total_quantity: Int,
    val url: String,
    val is_available: Int,
    val is_compare: Int,
    val cart_qty: Int,
    val is_wishlist: Int,
    val is_cart: Int,
    val images: List<String>,
    val wishlist: List<Any>,
    val cart: Cart
)

data class Cart(
    val cart_id: Int,
    val user_id: Int,
    val product_detail_id: Int,
    val qty: Int,
    val sample: Int,
    val created_at: String,
    val updated_at: String
)

data class Rating(
    val rating: String,
    val count: Int
)

data class Review(
    val uname: String,
    val profile_image: Any,
    val review: String,
    val date: String,
    val rating: String
)

data class VariationData(
    val variations: HashMap<String, String>,
    val product_detail_id: Int
)