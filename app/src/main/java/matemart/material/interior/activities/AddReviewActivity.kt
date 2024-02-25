package matemart.material.interior.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject

import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.CommonResponse
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import matemart.material.interior.databinding.ActivityAddReviewBinding
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
            matemart.material.interior.utils.Toast.Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                .setDescription("Please provide Rating").setDuration(5000)
                .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR).show()
            return
        }

        if (binding!!.etReview.text.isEmpty()) {
            matemart.material.interior.utils.Toast.Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                .setDescription("Please write some Review").setDuration(5000)
                .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR).show()
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
                    matemart.material.interior.utils.Toast.Toaster.Builder(this@AddReviewActivity).setTitle("SUCCESS")
                        .setDescription(response.body()?.message).setDuration(5000)
                        .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.SUCCESS).show()
                    finish()
                } else {
                    matemart.material.interior.utils.Toast.Toaster.Builder(this@AddReviewActivity).setTitle("ERROR")
                        .setDescription(response.body()?.message).setDuration(5000)
                        .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                matemart.material.interior.utils.Toast.Toaster.Builder(this@AddReviewActivity).setTitle("ERROR").setDescription(t.message)
                    .setDuration(5000).setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR).show()
            }

        })

    }

}