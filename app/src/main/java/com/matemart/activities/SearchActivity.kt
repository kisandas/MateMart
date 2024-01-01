package com.matemart.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.example.ResponseSearch
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ProductItemAdapter
import com.matemart.adapter.SearchRecentAdapter
import com.matemart.adapter.SearchResultListAdapter
import com.matemart.adapter.SearchTrendingAdapter
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SearchItemListner
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.Address
import com.matemart.model.ResponseProductList
import com.matemart.model.ResponseRecentSearch
import com.matemart.model.ViewListModel
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), ItemClickListener, WishListUpdateListner {
    lateinit var adapter: SearchResultListAdapter
    lateinit var recentAdapter: SearchRecentAdapter
    lateinit var trendingAdapter: SearchTrendingAdapter
    var listSearchHint: ArrayList<String> = arrayListOf()
    lateinit var etSearch: AutoCompleteTextView
    lateinit var iv_back: ImageView
    lateinit var rvSearchResult: RecyclerView
    lateinit var rvRecentSearch: RecyclerView
    lateinit var rvTrendingSearch: RecyclerView
    lateinit var rvProductList: RecyclerView
    lateinit var ll_emptyLayout: LinearLayout
    lateinit var ll_trendingSearch: LinearLayout
    lateinit var ll_RecentSearch: LinearLayout
    lateinit var cardView: CardView
    var recentItemList: ArrayList<String> = arrayListOf()
    var trendingItemList: ArrayList<String> = arrayListOf()
    lateinit var productListAdapter: ProductItemAdapter
    var list: ArrayList<ViewListModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        iv_back = findViewById(R.id.iv_back)
        etSearch = findViewById(R.id.etSearch)
        rvSearchResult = findViewById(R.id.rvSearchResult)
        rvProductList = findViewById(R.id.rvProductList)
        cardView = findViewById(R.id.cardView)
        rvRecentSearch = findViewById(R.id.rvRecentSearch)
        rvTrendingSearch = findViewById(R.id.rvTrendingSearch)
        ll_emptyLayout = findViewById(R.id.ll_emptyLayout)
        ll_trendingSearch = findViewById(R.id.ll_trendingSearch)
        ll_RecentSearch = findViewById(R.id.ll_RecentSearch)

        iv_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recentAdapter = SearchRecentAdapter(this, recentItemList, this)
        trendingAdapter = SearchTrendingAdapter(this, trendingItemList, this)
        productListAdapter = ProductItemAdapter(list, this, this)

        val productLayoutManager = GridLayoutManager(this, 2)
        rvProductList.layoutManager = productLayoutManager
        rvProductList.adapter = productListAdapter

        rvRecentSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val flexLayoutManager = FlexboxLayoutManager(this)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent =
            JustifyContent.FLEX_START // Align items to the start of the flex container


        rvTrendingSearch.layoutManager = flexLayoutManager



        rvRecentSearch.adapter = recentAdapter
        rvTrendingSearch.adapter = trendingAdapter

        getRecentAndTrendingSearchData()

        cardView.visibility = GONE

        adapter = SearchResultListAdapter(listSearchHint, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSearchResult.layoutManager = layoutManager
        rvSearchResult.adapter = adapter
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.length?.let {
                    if (it >= 3) {
                        getProductFromSearch(s.toString())
                    } else {
                        rvProductList.visibility = GONE
                        if(recentItemList.isNotEmpty() || trendingItemList.isNotEmpty()) {
                            ll_RecentSearch.visibility = VISIBLE
                            ll_trendingSearch.visibility = VISIBLE
                            ll_emptyLayout.visibility = GONE
                        }else{
                            ll_emptyLayout.visibility = VISIBLE
                            ll_RecentSearch.visibility = GONE
                            ll_trendingSearch.visibility = GONE
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


    }

    private fun SearchAPI(word: String) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("words", word)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchActivity)
        var call: Call<ResponseSearch> = apiInterface.getSearch(jsonObject)!!

        call.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                if (response.body()?.statuscode == 11) {
                    listSearchHint.clear()
                    response.body()?.data?.words?.let {
                        listSearchHint.addAll(it)

                        if (listSearchHint.size > 0) {
                            cardView.visibility = VISIBLE
                            rvSearchResult.visibility = VISIBLE
                        } else {
                            rvSearchResult.visibility = GONE
                            cardView.visibility = GONE
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    rvSearchResult.visibility = GONE
                    cardView.visibility = GONE
                    Toast.makeText(
                        this@SearchActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                rvSearchResult.visibility = GONE
                cardView.visibility = GONE
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


    fun getRecentAndTrendingSearchData() {

        var jsonObject = JsonObject()
        jsonObject.addProperty(
            "u_id", SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_ID, 0)
        )


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchActivity)
        var call: Call<ResponseRecentSearch> = apiInterface.getRecentSearch(jsonObject)!!

        call.enqueue(object : Callback<ResponseRecentSearch> {
            override fun onResponse(
                call: Call<ResponseRecentSearch>,
                response: Response<ResponseRecentSearch>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {

                        trendingItemList.clear()
                        recentItemList.clear()
                        it.trendingSearch?.words?.let { it1 -> trendingItemList.addAll(it1) }
                        it.recentlySearch?.words?.let { it1 -> recentItemList.addAll(it1) }

                        if (recentItemList.isNotEmpty() || trendingItemList.isNotEmpty()) {
                            ll_emptyLayout.visibility = GONE
                            ll_RecentSearch.visibility = VISIBLE
                            ll_trendingSearch.visibility = VISIBLE
                        } else {
                            ll_emptyLayout.visibility = VISIBLE
                            ll_RecentSearch.visibility = GONE
                            ll_trendingSearch.visibility = GONE

                        }
                        recentAdapter.notifyDataSetChanged()
                        trendingAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseRecentSearch>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }


    fun getProductFromSearch(word: String) {

        var jsonObject = JsonObject()
        jsonObject.addProperty(
            "u_id", SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_ID, 0)
        )
        jsonObject.addProperty("word", word)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchActivity)
        var call: Call<ResponseProductList> = apiInterface.getProductFromSearch(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {

                        list.clear()
                        list.addAll(it)
                        productListAdapter.notifyDataSetChanged()

                        if(list.isNotEmpty()){
                            rvProductList.visibility = VISIBLE
                            ll_RecentSearch.visibility = GONE
                            ll_trendingSearch.visibility = GONE
                            ll_emptyLayout.visibility = GONE
                        }else{
                            rvProductList.visibility = GONE
                        }

                    }
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    //    From Recent or Trending Seach Item Click
    override fun onSearchItemClick(word: String) {
        getProductFromSearch(word = word)
    }

    override fun onUpdate() {
//        updateList()
    }


}

interface ItemClickListener {
    fun onSearchItemClick(word: String)
}