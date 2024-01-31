package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matemart.interfaces.DismissBottomSheet
import com.matemart.fragments.StateSelectionBottomSheetFragment
import com.matemart.fragments.CitySelectionBottomSheetFragment
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.matemart.utils.Toast.Toaster
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.api.Constants
import com.matemart.api.Constants.CHECK_PINCODE
import com.matemart.api.Constants.statList
import com.matemart.interfaces.ApiInterface
import com.matemart.model.ResGetProfileDetails
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap

class LocationActivity : AppCompatActivity(), DismissBottomSheet {
    var btn_save: TextView? = null
    var et_state: TextView? = null
    var et_city: TextView? = null
    var et_pincode: EditText? = null
    var pref: SharedPrefHelper? = null
    var adapter_city: ArrayAdapter<String>? = null
    var stateList = ArrayList<String>()
    var isStateSelected = false
    var isPINCODE_VERIFIED = false
    var isDataLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        pref = SharedPrefHelper.getInstance(MyApplication())
        btn_save = findViewById(R.id.btn_save)
        et_state = findViewById(R.id.et_state)
        et_city = findViewById(R.id.et_city)
        et_pincode = findViewById(R.id.et_pincode)

        if (!pref!!.read(SharedPrefHelper.KEY_STATE).isNullOrEmpty()) {
            et_state?.setText(pref!!.read(SharedPrefHelper.KEY_STATE))
        }
        if (!pref!!.read(SharedPrefHelper.KEY_CITY).isNullOrEmpty()) {
            et_city?.setText(pref!!.read(SharedPrefHelper.KEY_CITY))
        }
        if (!pref!!.read(SharedPrefHelper.KEY_PINCODE).isNullOrEmpty() ) {
            et_pincode?.setText(pref!!.read(SharedPrefHelper.KEY_PINCODE))
        }

        if (!pref!!.read(SharedPrefHelper.KEY_STATE).isNullOrEmpty() &&
           !pref!!.read(SharedPrefHelper.KEY_CITY).isNullOrEmpty() &&
            !pref!!.read(SharedPrefHelper.KEY_PINCODE).isNullOrEmpty()
        ) {
            isDataLoaded = true
            isPINCODE_VERIFIED = true
        }
        initializeView()
    }

    var cddState: StateSelectionBottomSheetFragment? = null
    var cddCity: CitySelectionBottomSheetFragment? = null
    fun initializeView() {
        for (i in Constants.statList.indices) {
            stateList.add(Constants.statList[i].state)
        }
        et_pincode!!.isEnabled = false
        cddState = StateSelectionBottomSheetFragment("state", 40, this, stateList)
        et_state!!.setOnClickListener { cddState!!.show(supportFragmentManager, "TAG1") }
        et_city!!.setOnClickListener {
            if (isStateSelected) {
                var cityList: ArrayList<String> = ArrayList()
                cityList = statList[statePosition].cityList
                cddCity = CitySelectionBottomSheetFragment(
                    "city",
                    40,
                    this@LocationActivity,
                    cityList
                )
                cddCity!!.show(supportFragmentManager, "TAG2")
            }
        }


//
//        et_state.setThreshold(1);
//        et_state.setAdapter(adapter_state);
        et_state!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.e("checkText", "onTextChanged: $charSequence")
            }

            override fun afterTextChanged(editable: Editable) {
                Log.e("checkText", "afterTextChanged: $editable")
                if (stateList.contains(editable.toString())) {
                } else {
                    isStateSelected = false
                }
            }
        })
        et_pincode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isPINCODE_VERIFIED = false
            }

            override fun afterTextChanged(editable: Editable) {
                if (et_city!!.text.toString().isEmpty()) {
                    et_city!!.requestFocus()
                    Toast.makeText(this@LocationActivity, "Please Enter City", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (editable.toString().length == 6) {
                    VerifyPINCODE(et_city!!.text.toString(), editable.toString())
                }
            }
        })
        btn_save!!.setOnClickListener(View.OnClickListener {
            if (et_state!!.text.toString().isEmpty()) {
                Toast.makeText(
                    this@LocationActivity,
                    "Please Enter State First",
                    Toast.LENGTH_SHORT
                ).show()
                et_state!!.requestFocus()
                return@OnClickListener
            }
            if (et_city!!.text.toString().isEmpty()) {
                Toast.makeText(this@LocationActivity, "Please Enter City First", Toast.LENGTH_SHORT)
                    .show()
                et_city!!.requestFocus()
                return@OnClickListener
            }
            if (!isPINCODE_VERIFIED) {
                Toast.makeText(
                    this@LocationActivity,
                    "Please Enter Valid PinCode",
                    Toast.LENGTH_SHORT
                ).show()
                et_pincode!!.requestFocus()
                return@OnClickListener
            }

            if(isDataLoaded){
                startActivity(Intent(this@LocationActivity, HomeActivity::class.java))
                finishAffinity()
            }else{
                updateProfile(
                    et_state!!.text.toString(),
                    et_city!!.text.toString(),
                    et_pincode!!.text.toString()
                )
            }




        })
    }


    fun updateProfile(state: String, city: String, pincode: String) {
        var jsonObject = JSONObject()
        jsonObject.put("state", state)
        jsonObject.put("city", city)
        jsonObject.put("pincode", pincode)



        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonObject.toString().toRequestBody(mediaType)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetProfileDetails> = apiInterface.updateUserProfile(requestBody)!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>,
                response: retrofit2.Response<ResGetProfileDetails>
            ) {

                Log.e("jjjjjjjjjjj", "onResponse: "+response.body()?.data.toString() )
                Log.e("jjjjjjjjjjj", "onResponse: "+response.body().toString() )
                if (response.isSuccessful) {

                    state.let {
                        if (it.isNotEmpty() && !it.equals("null", ignoreCase = true)) {
                            pref!!.write(SharedPrefHelper.KEY_STATE, it)
                        }
                    }

                    city.let {
                        if (it.isNotEmpty() && !it.equals("null", ignoreCase = true)) {
                            pref!!.write(SharedPrefHelper.KEY_CITY, it)
                        }
                    }

                    pincode.let {
                        if (it.isNotEmpty() && !it.equals("null", ignoreCase = true)) {
                            pref!!.write(SharedPrefHelper.KEY_PINCODE, it)
                        }
                    }

                    startActivity(Intent(this@LocationActivity, HomeActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(
                        this@LocationActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Log.e("jjjjjjjjjjj", "onFailure: "+t.message.toString() )
               t.printStackTrace()
                Toast.makeText(this@LocationActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    fun getPositionFromStateListAndSetAdapterForCity(state: String): Int {
        return stateList.indexOf(state)
    }

    fun VerifyPINCODE(CITY: String?, PINCODE: String?) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("city", CITY)
            jsonObject.put("pincode", PINCODE)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            Constants.BASE_URL + CHECK_PINCODE,
            jsonObject,
            Response.Listener { response ->
                try {
                    val statusCode = response.optInt("statuscode")
                    val message = response.getString("message")
                    if (statusCode == 11) {
                        isPINCODE_VERIFIED = true
                        Toaster.Builder(this@LocationActivity)
                            .setTitle("Success")
                            .setDescription(message)
                            .setDuration(5000)
                            .setStatus(Toaster.Status.SUCCESS)
                            .show()

//                                startActivity(new Intent(LocationActivity.this, HomeActivity.class));
                    } else {
                        Toaster.Builder(this@LocationActivity)
                            .setTitle("Error")
                            .setDescription(message)
                            .setDuration(5000)
                            .setStatus(Toaster.Status.ERROR)
                            .show()
                    }
                } catch (e: Exception) {
                    Toaster.Builder(this@LocationActivity)
                        .setTitle(Utils.ERROR_TITLE)
                        .setDescription(Utils.ERROR_MESSAGE)
                        .setDuration(5000)
                        .setStatus(Toaster.Status.ERROR)
                        .show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toaster.Builder(this@LocationActivity)
                    .setTitle("Error")
                    .setDescription(Utils.ERROR_MESSAGE)
                    .setDuration(5000)
                    .setStatus(Toaster.Status.ERROR)
                    .show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                //                header.put(HEADER_APP_PLATFORM, HEADER_ANDROID);
//                header.put(HEADER_APP_VERSION, BuildConfig.VERSION_NAME);
                return HashMap()
            }
        }
        queue.add(jsonObjectRequest)
    }

    var statePosition = -1

    override fun DismissDialog(position: Int, state: String?, type: String?) {
        if (type.equals("state", ignoreCase = true)) {
            et_state!!.text = state
            isStateSelected = true
            statePosition = getPositionFromStateListAndSetAdapterForCity(state!!)
        } else if (type.equals("city", ignoreCase = true)) {
            et_city!!.text = state
            et_pincode!!.isEnabled = true
        }
    }
}