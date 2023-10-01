package com.matemart.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.ResponseSearch
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.SearchResultListAdapter
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SearchItemListner
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), SearchItemListner {
    lateinit var adapter: SearchResultListAdapter
    var listSearchHint: ArrayList<String> = arrayListOf()
    lateinit var etSearch: AutoCompleteTextView
    lateinit var rvSearchResult: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch = findViewById(R.id.etSearch)
        rvSearchResult = findViewById(R.id.rvSearchResult)

        adapter = SearchResultListAdapter(listSearchHint, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSearchResult.layoutManager = layoutManager
        rvSearchResult.adapter = adapter
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.length?.let {
                    if (it >= 3) {
                        SearchAPI(s.toString())
                    } else {
                        rvSearchResult.visibility = GONE
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
                            rvSearchResult.visibility = VISIBLE
                        } else {
                            rvSearchResult.visibility = GONE
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

    override fun onSearchClicked(result: String, type: String) {
//        type : word, click_id, c_id

    }


}