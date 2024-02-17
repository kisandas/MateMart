package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class RecentlySearch(

    @SerializedName("words") var words: ArrayList<String> = arrayListOf()

)