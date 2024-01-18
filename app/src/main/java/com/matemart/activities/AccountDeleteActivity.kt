package com.matemart.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.annotations.Status
import com.matemart.api.WebResponse
import com.matemart.databinding.ActivityAddReviewBinding
import com.matemart.databinding.ActivityDeleteAccountBinding
import com.matemart.databinding.ActivityMyOrderDetailBinding
import com.matemart.fragments.DeleteAccountBottomSheetFragment
import com.matemart.fragments.VerifyOtpBottomSheet
import com.matemart.interfaces.ApiInterface
import com.matemart.model.CommonResponse
import com.matemart.model.DeleteResponse
import com.matemart.model.ResSendOtp
import com.matemart.model.login.LoginResponse
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
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
            Log.e("ccccccccccccc", "onCreate: " + mobileNo)
            Log.e("ccccccccccccc", "onCreate: " + binding!!.etPhone.text.toString())
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

    fun sendOTPForDelete() {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty(
            "mo_no", SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.KEY_LOGIN_NUMBER )
        )


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AccountDeleteActivity)
        var call: Call<ResSendOtp> = apiInterface.sendOtp(jsonObject)!!

        call.enqueue(object : Callback<ResSendOtp> {
            override fun onResponse(
                call: Call<ResSendOtp>,
                response: Response<ResSendOtp>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@AccountDeleteActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()

                    VerifyOtpBottomSheet("deleteAccount",
                        binding!!.etReason.text.toString().trim(),
                        SharedPrefHelper.getInstance(MyApplication.getInstance())
                            .read(SharedPrefHelper.KEY_LOGIN_NUMBER),
                        response.body()?.data?.token.toString(),
                        object : VerifyOtpBottomSheet.Update {
                            override fun onUpdate(type: String) {
                                if (type == "deleteAccount") {
                                    deleteAccountAPICall()
                                }
                            }

                        }).show(supportFragmentManager,"deleteAccount")


                } else {
                    Toast.makeText(
                        this@AccountDeleteActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResSendOtp>, t: Throwable) {
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

    private fun deleteAccountAPICall() {

    }


    override fun onDeleteClicked() {
        sendOTPForDelete()
    }


}

interface onDeleteClickListener {
    fun onDeleteClicked()
}