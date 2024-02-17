package matemart.material.Interior.model.login

import com.google.gson.annotations.SerializedName


data class LoginData(

    @SerializedName("u_id") var uId: Int? = null,
    @SerializedName("otp") var otp: Int? = null,
    @SerializedName("token") var token: String? = null

)