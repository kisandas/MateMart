package matemart.material.interior.model

import com.google.gson.annotations.SerializedName

data class SearchWord(
    @SerializedName("words") var words: ArrayList<String> = arrayListOf()

)
