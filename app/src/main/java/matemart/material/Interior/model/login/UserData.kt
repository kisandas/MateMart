package matemart.material.Interior.model.login

import com.google.gson.annotations.SerializedName


data class UserData(

    @SerializedName("city") var city: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("ccid") var ccid: String? = null

)