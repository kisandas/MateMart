package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class ImageUploadResponseModel(


    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ImageData? = ImageData()

)

data class ImageData(

    @SerializedName("image") var image: String? = null

)