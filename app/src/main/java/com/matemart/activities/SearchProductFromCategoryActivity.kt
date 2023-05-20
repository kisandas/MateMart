package com.matemart.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.CategoryModel
import com.example.example.ResponseCategory
import com.example.example.ResponseProductList
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.MainStoreAdapter.ItemHomeProductListHolder
import com.matemart.adapter.ProductItemAdapter
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.ViewListModel
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductFromCategoryActivity : AppCompatActivity() , WishListUpdateListner {

    lateinit var rcProductList: RecyclerView
    lateinit var tvTitle: TextView
    var list: ArrayList<ViewListModel> = arrayListOf()
    lateinit var adapter : ProductItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product_from_category)
        rcProductList = findViewById(R.id.rcProductList)
        tvTitle = findViewById(R.id.tvTitle)

        val layoutManager = GridLayoutManager(this, 2)
       rcProductList.layoutManager = layoutManager
       adapter = ProductItemAdapter(list, this,this)
       rcProductList.adapter = adapter


        tvTitle.text = intent.getStringExtra("title")
        updateList()

    }

    private  fun updateList(){
        if(intent.hasExtra("clickId")){
//            View All Products
            list.clear()
            getViewAllProduct(intent.getStringExtra("clickId")!!)

        }else {
//              Product by subcategory
            list.clear()
            getProductsFromSubCategory(intent.getIntExtra("c_id",0))
        }
    }


    private fun getViewAllProduct(clickId : String) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("clickId", clickId)


        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
        var call: Call<ResponseProductList> = apiInterface.getViewAllProducts(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode ==11) {
                    response.body()?.data?.let { list.addAll(it) }
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

    private fun getProductsFromSubCategory(c_id : Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("c_id", c_id)


        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this@SearchProductFromCategoryActivity)
        var call: Call<ResponseProductList> = apiInterface.getProductsFromSubCategory(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>,
                response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode ==11) {
                    response.body()?.data?.let { list.addAll(it) }
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