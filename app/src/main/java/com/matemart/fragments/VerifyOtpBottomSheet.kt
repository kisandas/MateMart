package com.matemart.fragments;

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import com.matemart.api.Constants
import com.matemart.databinding.BottomsheetVerifyOtpBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class VerifyOtpBottomSheet(var mobileNumber: String, var token: String, var update: Update) :
    BottomSheetDialogFragment() {

    lateinit var bottomsheetVerifyOtpBinding: BottomsheetVerifyOtpBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        bottomsheetVerifyOtpBinding =
            BottomsheetVerifyOtpBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(bottomsheetVerifyOtpBinding.root)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed(Runnable {
            changeNumberVerifyOtp()
        }, 1000)
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
        var call: Call<Void>? = apiInterface.changeNumberVerifyOtp(jsonObject)
        call!!.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    changeNumber()
                } else {
                    Toast.makeText(
                        activity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    private fun changeNumber() {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "mo_no",
            mobileNumber
        )
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, activity)
        var call: Call<Void>? = apiInterface.changeNumber(jsonObject)
        call!!.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    Toast.makeText(
                        activity,
                        "Number Successfully updated",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    update.onUpdate()
                    dialog?.dismiss()

                } else if ( Constants.getErrorMessage(
                        activity,
                        response.errorBody()
                    ).equals("The mobile number is linked to another account.")
                ) {
                    Toast.makeText(
                        activity,
                        "The mobile number is linked to another account.",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    update.onUpdate()
                    dialog?.dismiss()

                } else {

                    Toast.makeText(
                        activity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }


    public interface Update {
        fun onUpdate()
    }
}
