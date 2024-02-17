package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Any>? = arrayListOf()
)



