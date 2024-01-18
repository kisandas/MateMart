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
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.Toast.Toaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewActivity : AppCompatActivity() {
    var p_id = 0
    private var binding: ActivityAddReviewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        p_id = intent.getIntExtra("p_id", 0)

        binding!!.etName.setText(
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_NAME)
        )

        binding!!.btnSubmit.setOnClickListener { checkValidation() }


    }

    fun checkValidation() {
        if (binding!!.Ratingbar.rating <= 0f) {
            Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                .setDescription("Please provide Rating").setDuration(5000)
                .setStatus(Toaster.Status.ERROR).show()
            return
        }

        if (binding!!.etReview.text.isEmpty()) {
            Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                .setDescription("Please write some Review").setDuration(5000)
                .setStatus(Toaster.Status.ERROR).show()
            return
        }
        postReview(p_id, binding!!.Ratingbar.rating, binding!!.etReview.text.toString())
    }

    fun postReview(p_id: Int, rating: Float, review: String) {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty(
            "u_id",
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_ID, 0)
        )
        jsonObject.addProperty("p_id", p_id)
        jsonObject.addProperty("rating", rating)
        jsonObject.addProperty("review", review)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AddReviewActivity)
        var call: Call<CommonResponse> = apiInterface.writeReview(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>, response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toaster.Builder(this@AddReviewActivity).setTitle("SUCCESS")
                        .setDescription(response.body()?.message).setDuration(5000)
                        .setStatus(Toaster.Status.SUCCESS).show()
                    finish()
                } else {
                    Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                        .setDescription(response.body()?.message).setDuration(5000)
                        .setStatus(Toaster.Status.ERROR).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toaster.Builder(this@AddReviewActivity).setTitle("ERROR").setDescription(t.message)
                    .setDuration(5000).setStatus(Toaster.Status.ERROR).show()
            }

        })

    }

}