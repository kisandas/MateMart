package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class RecentSearchData(

    @SerializedName("recently_search") var recentlySearch: RecentlySearch? = RecentlySearch(),
    @SerializedName("trending_search") var trendingSearch: TrendingSearch? = TrendingSearch()

)