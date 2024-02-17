package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class CompareProductDetailResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: CompareVariationData? = CompareVariationData()

)


data class CompareVariationData(

    @SerializedName("product") var product: CompareProduct? = CompareProduct(),
    @SerializedName("review_total") var reviewTotal: Int? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("avg_rating") var avgRating: Double? = null,
    @SerializedName("ratings") var ratings: ArrayList<String> = arrayListOf(),
    @SerializedName("review") var review: ArrayList<String> = arrayListOf(),
    @SerializedName("variation") var variation: ArrayList<CompareVariation> = arrayListOf()

)

data class CompareVariation(

    @SerializedName("product_detail_id") var productDetailId: Int? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("saleprice") var saleprice: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("total_quantity") var totalQuantity: Int? = null,
    var currentQty: Int = 0

)


data class CompareProduct(

    @SerializedName("p_id") var pId: Int? = null,
    @SerializedName("p_name") var pName: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("detail_desc") var detail_desc: String? = null,
    @SerializedName("saleprice") var saleprice: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("total_quantity") var totalQuantity: Int? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("is_available") var isAvailable: Int? = null,
    @SerializedName("is_compare") var isCompare: Int? = null,
    @SerializedName("b_name") var bName: String? = null,
    @SerializedName("c_name") var cName: String? = null,
    @SerializedName("is_wishlist") var isWishlist: Int? = null,
    @SerializedName("image") var image: ArrayList<String> = arrayListOf(),
    @SerializedName("headerTitle") var headerTitle: String? = null,

    )