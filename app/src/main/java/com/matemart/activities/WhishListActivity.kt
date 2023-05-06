package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.matemart.databinding.ActivityWhishListBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.matemart.R

class WhishListActivity : AppCompatActivity() {

    lateinit var binding: ActivityWhishListBinding;
    var list: MutableList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhishListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWishList()
    }


    fun getWishList() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<JsonObject> = apiInterface.getWishList()!!

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
//                    list.addAll(response.body())
                } else {
                    Toast.makeText(
                        this@WhishListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@WhishListActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


}
