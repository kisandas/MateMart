package com.example.example

import com.google.gson.annotations.SerializedName


data class TrendingSearch(

    @SerializedName("words") var words: ArrayList<String> = arrayListOf()

)