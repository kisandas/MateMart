package com.matemart.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.databinding.ActivityAddReviewBinding
import com.matemart.databinding.ActivityDeleteAccountBinding
import com.matemart.databinding.ActivityMyOrderDetailBinding
import com.matemart.fragments.DeleteAccountBottomSheetFragment
import com.matemart.interfaces.ApiInterface
import com.matemart.model.CommonResponse
import com.matemart.model.DeleteResponse
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountDeleteActivity : AppCompatActivity(), onDeleteClickListener {
    private var binding: ActivityDeleteAccountBinding? = null
    lateinit var pref: SharedPrefHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
        val mobileNo = pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString()

        binding!!.include2.title.text = "Delete Account"
        binding!!.include2.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding!!.btnDelete.setOnClickListener {
            Log.e("ccccccccccccc", "onCreate: "+mobileNo )
            Log.e("ccccccccccccc", "onCreate: "+binding!!.etPhone.text.toString() )
            if (binding!!.etPhone.text.toString() == mobileNo) {
                val cdd = DeleteAccountBottomSheetFragment("delete", this)
                cdd.show(supportFragmentManager, "TAG3")
            } else {
                Toast.makeText(
                    this@AccountDeleteActivity,
                    "Please Enter Your Own Number",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    fun deleteUserAccount() {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty(
            "id", SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.USER_ID, 0)
        )


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AccountDeleteActivity)
        var call: Call<DeleteResponse> = apiInterface.deleteUser(jsonObject)!!

        call.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@AccountDeleteActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                        .logoutProfile(this@AccountDeleteActivity)

                } else {
                    Toast.makeText(
                        this@AccountDeleteActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@AccountDeleteActivity,
                    "" + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    override fun onDeleteClicked() {
        deleteUserAccount()
    }

}

interface onDeleteClickListener {
    fun onDeleteClicked()
}