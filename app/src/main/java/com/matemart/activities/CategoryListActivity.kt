package com.matemart.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.CategoryModel
import com.example.example.ResponseCategory
import com.matemart.R
import com.matemart.adapter.CategoryListAdapter
import com.matemart.adapter.MainStoreAdapter.ItemTopCategoryHolder
import com.matemart.interfaces.ApiInterface
import com.matemart.model.Architect
import com.matemart.model.ResGetArchitectList
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListActivity : AppCompatActivity() {

    lateinit var rvCategories: RecyclerView
    lateinit var ivBack: ImageView
    lateinit var adapter: CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        rvCategories = findViewById(R.id.rvCategories)
        ivBack = findViewById(R.id.iv_back)

        val layoutManager =
            GridLayoutManager(this, 2)
        rvCategories.layoutManager = layoutManager
        adapter = CategoryListAdapter(list, this,false)
        rvCategories.adapter = adapter


        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getAllCategory()
    }


    var list: ArrayList<CategoryModel> = arrayListOf()
    fun getAllCategory() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResponseCategory> = apiInterface.getAllCategory()!!

        call.enqueue(object : Callback<ResponseCategory> {
            override fun onResponse(
                call: Call<ResponseCategory>,
                response: Response<ResponseCategory>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { list.addAll(it) }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@CategoryListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(
                    this@CategoryListActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }
}