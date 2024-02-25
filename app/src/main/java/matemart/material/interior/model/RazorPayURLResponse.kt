package matemart.material.interior.model

import com.google.gson.annotations.SerializedName


data class RazorPayURLResponse(


    @SerializedName("statuscode")
    var statuscode: Int? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("data")
    var data: RazorPayData? = RazorPayData()


)


data class RazorPayData(

    @SerializedName("razorpayLink") var razorpayLink: String? = null

)
