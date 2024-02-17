package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Boolean =false
)
