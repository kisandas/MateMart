package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class CouponItemData(
    @SerializedName("code") var code: String? = null,
    @SerializedName("noofuses") var noofuses: Int? = null,
    @SerializedName("type") var type: Int? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("minamount") var minamount: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("s_date") var sDate: String? = null,
    @SerializedName("e_date") var eDate: String? = null

)