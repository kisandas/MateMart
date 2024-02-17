package matemart.material.Interior.model.login

import com.google.gson.annotations.SerializedName


data class UserResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: UserData? = UserData()

)