package com.example.example

import com.google.gson.annotations.SerializedName


data class RecentSearchData(

    @SerializedName("recently_search") var recentlySearch: RecentlySearch? = RecentlySearch(),
    @SerializedName("trending_search") var trendingSearch: TrendingSearch? = TrendingSearch()

)