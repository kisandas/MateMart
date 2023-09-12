package com.matemart.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ReviewListAdapter
import com.matemart.databinding.ActivityReviewListBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.ResReviewModel
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewListActivity : AppCompatActivity() {


    lateinit var binding: ActivityReviewListBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val p_id = intent.getIntExtra("p_id", 0)

        getAllReview(p_id)

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun getAllReview(p_id: Int) {
        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("p_id", p_id)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResReviewModel> = apiInterface.getAllReview(jsonObject)!!

        call.enqueue(object : Callback<ResReviewModel> {
            override fun onResponse(
                call: Call<ResReviewModel>,
                response: Response<ResReviewModel>
            ) {
                if (response.body()?.statuscode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        binding.tvProductName.text = data.product?.pName

//

//                        binding.tvAvgRating.text = "${data.avgRating} "

                        val adapterReview = ReviewListAdapter(this@ReviewListActivity, data.review)
                        binding.rcReviews.layoutManager = LinearLayoutManager(
                            this@ReviewListActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcReviews.adapter = adapterReview

                        Glide.with(this@ReviewListActivity).load(data.product?.image)
                            .placeholder(R.drawable.img_loading_image)
                            .into(binding.ivProductImage)



                        binding.tvTotalReview.text = "${data.reviewTotal} Reviews"
                        binding.tvAvgRating.text = "${data.avgRating}"

                    }


                } else {
                    Toast.makeText(
                        this@ReviewListActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResReviewModel>, t: Throwable) {
                t.printStackTrace()
                Log.e("lllllllllll", "onFailure: " + t.message)
                Toast.makeText(
                    this@ReviewListActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

}