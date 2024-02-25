package matemart.material.interior.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AppDataResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: AppData? = AppData()
): Serializable


data class AppData(

    @SerializedName("app_version_data") var appVersionData: AppVersionData? = AppVersionData(),
    @SerializedName("image_popup") var imagePopup: ImagePopup? = ImagePopup(),
    @SerializedName("dat") var date: String? = null

): Serializable


data class ImagePopup(

    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("image_ratio") var imageRatio: String? = null,
    @SerializedName("clickId") var clickId: String? = null

): Serializable


data class AppVersionData(

    @SerializedName("release") var release: Int? = null,
    @SerializedName("force_update") var forceUpdate: Boolean? = null,
    @SerializedName("description") var description: ArrayList<String> = arrayListOf()

): Serializable