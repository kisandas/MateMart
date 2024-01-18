package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.matemart.model.ResponseProductList
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ProductItemAdapter
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.FilterBody
import com.matemart.model.ViewListModel
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import okhttp3.internal.notify
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductFromCategoryActivity : AppCompatActivity(), WishListUpdateListner {

    companion object {
        var actualMap: LinkedHashMap<String, List<FilterBody>>? = LinkedHashMap()
        var selectedMap: LinkedHashMap<String, List<FilterBody>>? = LinkedHashMap()
        var minPrice: Double = 0.0
        var maxPrice: Double = 0.0
    }

    lateinit var rcProductList: RecyclerView
    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    var list: ArrayList<ViewListModel> = arrayListOf()
    lateinit var adapter: ProductItemAdapter
    lateinit var llEmptyView: LinearLayout
    lateinit var ivFilter: ImageView

    var clickID = ""
    var c_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product_from_category)
        rcProductList = findViewById(R.id.rcProductList)
        tvTitle = findViewById(R.id.tvTitle)
        ivFilter = findViewById(R.id.ivFilter)
        ivBack = findViewById(R.id.iv_back)
        llEmptyView = findViewById(R.id.llEmptyView)

        actualMap?.clear()


        val searchEditText = findViewById<EditText>(R.id.etSearchProduct)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text changes, you can call the filterList function here
//                val query = s.toString()
//                filterList(query)
                if (s.toString().length > 3) {
                    getProductFromSearch(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed in this case
            }
        })

        val layoutManager = GridLayoutManager(this, 2)
        rcProductList.layoutManager = layoutManager
        adapter = ProductItemAdapter(list, this, this)
        rcProductList.adapter = adapter

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        tvTitle.text = intent.getStringExtra("title")
        c_id = intent.getIntExtra("c_id", 0)
        clickID = intent.getStringExtra("clickId").toString()

        ivFilter.setOnClickListener {
            startActivityForResult(
                Intent(this@SearchProductFromCategoryActivity, FilterActivity::class.java)
                    .putExtra("c_id", c_id)
                    .putExtra("clickId", clickID), 203
            )
        }
        updateList()

    }

    private fun updateList() {
        if (intent.hasExtra("clickId")) {
//            View All Products
            list.clear()
            getViewAllProduct(intent.getStringExtra("clickId")!!)

        } else {
//              Product by subcategory
            list.clear()
            getProductsFromSubCategory(intent.getIntExtra("c_id", 0))
        }
    }

    fun filterList(query: String) {
        val filteredList =
            list.filter { item -> item.p_name?.contains(query, ignoreCase = true) == true }
        adapter.updateList(filteredList)
    }


    fun getProductFromSearch(word: String) {

        val jsonObject = convertToJSON(actualMap!!)
        jsonObject.addProperty(
            "u_id", SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_ID, 0)
        )
        jsonObject.addProperty("word", word)
        if (clickID != "null" && clickID.isNotEmpty()) {
            jsonObject.addProperty("clickId", clickID)
        } else {
            jsonObject.addProperty("clickId", "Category")
        }

        if (c_id != -1) {
            jsonObject.addProperty("c_id", c_id)
        }


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
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

                        if (list.isEmpty()) {
                            llEmptyView.visibility = VISIBLE
                            rcProductList.visibility = GONE
                        } else {
                            llEmptyView.visibility = VISIBLE
                            rcProductList.visibility = GONE
                        }
                        adapter.notifyDataSetChanged()

//                        productListAdapter.notifyDataSetChanged()
//
//                        if(list.isNotEmpty()){
//                            rvProductList.visibility = View.VISIBLE
//                            ll_RecentSearch.visibility = View.GONE
//                            ll_trendingSearch.visibility = View.GONE
//                            ll_emptyLayout.visibility = View.GONE
//                        }else{
//                            rvProductList.visibility = View.GONE
//                        }

                    }
                } else {
                    Toast.makeText(
                        this@SearchProductFromCategoryActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@SearchProductFromCategoryActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(
            "------------->",
            "onActivityResult: " + requestCode + "     resultCode: " + resultCode
        )
        if (requestCode == 203 && resultCode == RESULT_OK) {
            Log.e("------------->", "onActivityResult c_id: " + data?.getIntExtra("c_id", 0))
            Log.e("------------->", "onActivityResult clickId: " + data?.getStringExtra("clickId"))

//
//            actualMap =
//                (data?.getSerializableExtra("filterMap") as? HashMap<String, List<FilterBody>>)!!

//            Log.e("actualMap++++++++", "onActivityResult: "+ actualMap)

            if (actualMap != null) {
                Log.e("checkOBJJJ", "onActivityResult: actualMap    " + actualMap.toString())
                val obj = convertToJSON(actualMap!!)



                Log.e("checkOBJJJ", "onActivityResult: 3333333333333    " + obj.toString())
                if (clickID != null && clickID != "null" && clickID.isNotEmpty()) {
                    obj.addProperty("clickId", clickID)
                } else {
                    obj.addProperty("clickId", "Category")
                }

                if (c_id != null && c_id != -1) {
                    obj.addProperty("c_id", c_id)
                }
                getFilteredData(obj)

            }


        }
    }

    fun convertToJSON(inputMap: HashMap<String, List<FilterBody>>): JsonObject {
        val jsonObject = JsonObject()

        for ((key, valueList) in inputMap) {
            val nameArray = JsonArray()

            for (filterBody in valueList) {
                if (filterBody.name != "price")
                    nameArray.add(filterBody.name.toString())
            }

            if (nameArray.size() > 0)
                jsonObject.add(key, nameArray)


        }

        Log.e("jjjjjjjjjjjjjjjjj", "convertToJSON: " + minPrice + "         " + maxPrice)
        if (minPrice != 0.0 || maxPrice != 0.0) {
            val priceArray = JsonArray()
            priceArray.add(minPrice)
            priceArray.add(maxPrice)
            jsonObject.add("price", priceArray)
        }

        return jsonObject
    }

    private fun getViewAllProduct(clickId: String) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("clickId", clickId)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
        var call: Call<ResponseProductList> = apiInterface.getViewAllProducts(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode == 11) {
                    list.clear()
                    response.body()?.data?.let { list.addAll(it) }
                    if (list.isEmpty()) {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    } else {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@SearchProductFromCategoryActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                Toast.makeText(
                    this@SearchProductFromCategoryActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

    }

    private fun getProductsFromSubCategory(c_id: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("c_id", c_id)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
        var call: Call<ResponseProductList> = apiInterface.getProductsFromSubCategory(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode == 11) {
                    list.clear()
                    response.body()?.data?.let { list.addAll(it) }
                    if (list.isEmpty()) {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    } else {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@SearchProductFromCategoryActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                Toast.makeText(
                    this@SearchProductFromCategoryActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

    fun getFilteredData(jsonObject: JsonObject) {

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
        Log.e("checkkjjkjkj", "getFilteredData: " + jsonObject.toString())
        var call: Call<ResponseProductList> = apiInterface.getFilteredProduct(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {
                        list.clear()
                        list.addAll(it)
                    }

                    if (list.isEmpty()) {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    } else {
                        llEmptyView.visibility = VISIBLE
                        rcProductList.visibility = GONE
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@SearchProductFromCategoryActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                Toast.makeText(
                    this@SearchProductFromCategoryActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    override fun onUpdate() {
        updateList()
    }

}