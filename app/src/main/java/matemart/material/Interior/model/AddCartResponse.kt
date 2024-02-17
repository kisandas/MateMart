package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class AddCartResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: CartItem? = null

)