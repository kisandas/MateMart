package com.matemart.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.databinding.ActivityAddReviewBinding
import com.matemart.databinding.ActivityMyOrderDetailBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.CommonResponse
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewActivity : AppCompatActivity() {
    private var binding: ActivityAddReviewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


    }

    fun postReview(u_id: Int,p_id: Int,rating:Double,review:String) {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("u_id", u_id)
        jsonObject.addProperty("p_id", p_id)
        jsonObject.addProperty("rating", rating)
        jsonObject.addProperty("review", review)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AddReviewActivity)
        var call: Call<CommonResponse> = apiInterface.returnOrder(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@AddReviewActivity,
                        ""+response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@AddReviewActivity,
                        ""+response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@AddReviewActivity,
                    ""+t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

}