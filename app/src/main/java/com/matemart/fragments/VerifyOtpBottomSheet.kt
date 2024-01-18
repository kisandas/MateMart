package com.matemart.fragments;

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import com.matemart.activities.LocationActivity
import com.matemart.api.Constants
import com.matemart.databinding.BottomsheetVerifyOtpBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.CommonResponse
import com.matemart.model.DeleteResponse
import com.matemart.model.login.UserData
import com.matemart.model.login.UserResponse
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.Toast.Toaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

public class VerifyOtpBottomSheet(
    private val type: String,
    private val reason: String,
    var mobileNumber: String,
    var token: String,
    var update: Update
) :
    BottomSheetDialogFragment() {

    lateinit var bottomsheetVerifyOtpBinding: BottomsheetVerifyOtpBinding
    lateinit var pref: SharedPrefHelper

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        bottomsheetVerifyOtpBinding =
            BottomsheetVerifyOtpBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(bottomsheetVerifyOtpBinding.root)
        dialog.setCancelable(false)

        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

        bottomsheetVerifyOtpBinding.btnVerify.setOnClickListener {
            if (bottomsheetVerifyOtpBinding.otpView.getStringFromFields().toString().isNotEmpty()) {
                if (type == "mobile") {
                    changeNumberVerifyOtp()
                } else if (type == "deleteAccount") {
                    deleteUserAccount(reason)
                }
            }
        }
    }


    private fun changeNumberVerifyOtp() {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "mo_no",
            mobileNumber
        )
        jsonObject.addProperty("verification_code", 1234)
        jsonObject.addProperty("token", token)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, activity)
        var call: Call<UserResponse>? = apiInterface.changeNumberVerifyOtp(jsonObject)
        call!!.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                if (response.isSuccessful) {
//                    changeNumber()
                    response.body()?.data?.let {
                        saveUserInfo(it)
                    }

                } else {
                    Toast.makeText(
                        activity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }


    private fun deleteUserAccount(reason:String) {

        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "reason",reason
        )
        jsonObject.addProperty("verification_code", bottomsheetVerifyOtpBinding.otpView.getStringFromFields())
        jsonObject.addProperty("token", token)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, activity)
        var call: Call<CommonResponse>? = apiInterface.deleteUser(jsonObject)
        call!!.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.isSuccessful) {
//                    changeNumber()
                    if (response.body()?.statuscode == 11) {
                        pref.logoutProfile(requireContext())
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

    fun saveUserInfo(response: UserData) {


        val ccid = response.ccid



        ccid?.let {
            if (!it.isNullOrEmpty()) {
                pref.write(SharedPrefHelper.KEY_CCID, it)

                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, mobileNumber)

                var accessToken = pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER) + ":" + it
                val encodedString: String =
                    Base64.getEncoder().encodeToString(accessToken.toByteArray())

                pref.write(SharedPrefHelper.KEY_ACCESS_TOKEN, encodedString)
            }
        }

        Toaster.Builder(requireContext())
            .setTitle("Success")
            .setDescription("Number Updated")
            .setDuration(5000)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
        update.onUpdate(type)
        dismiss()
    }


    public interface Update {
        fun onUpdate(type: String)
    }
}
