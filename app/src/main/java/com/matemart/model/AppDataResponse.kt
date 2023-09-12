package com.matemart.model

import com.google.gson.annotations.SerializedName


data class AppDataResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: AppData? = AppData()
)


data class AppData(

    @SerializedName("app_version_data") var appVersionData: AppVersionData? = AppVersionData(),
    @SerializedName("image_popup") var imagePopup: ImagePopup? = ImagePopup(),
    @SerializedName("date") var date: String? = null

)


data class ImagePopup(

    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("image_ratio") var imageRatio: String? = null,
    @SerializedName("clickId") var clickId: String? = null

)


data class AppVersionData(

    @SerializedName("release") var release: Int? = null,
    @SerializedName("force_update") var forceUpdate: Boolean? = null,
    @SerializedName("description") var description: ArrayList<String> = arrayListOf()

)