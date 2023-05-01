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
import com.matemart.utils.SharedPreference
import com.matemart.fragments.StateSelectionBottomSheetFragment
import com.matemart.fragments.CitySelectionBottomSheetFragment
import com.matemart.activities.HomeActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.matemart.utils.Toast.Toaster
import com.android.volley.VolleyError
import kotlin.Throws
import com.android.volley.AuthFailureError
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.matemart.adapter.LoginViewPagerAdapter
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Response
import com.matemart.R
import com.matemart.utils.OTPView
import com.matemart.activities.LocationActivity
import com.matemart.api.Constants
import com.matemart.api.Constants.statList
import com.matemart.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap

class LocationActivity : AppCompatActivity(), DismissBottomSheet {
    var btn_save: TextView? = null
    var et_state: TextView? = null
    var et_city: TextView? = null
    var et_pincode: EditText? = null
    var pref: SharedPreference? = null
    var adapter_city: ArrayAdapter<String>? = null
    var stateList = ArrayList<String>()
    var isStateSelected = false
    var isPINCODE_VERIFIED = false
    var isDataLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        pref = SharedPreference(this)
        btn_save = findViewById(R.id.btn_save)
        et_state = findViewById(R.id.et_state)
        et_city = findViewById(R.id.et_city)
        et_pincode = findViewById(R.id.et_pincode)
        if (pref!!.getString(SharedPreference.KEY_STATE).isEmpty() && pref!!.getString(
                SharedPreference.KEY_STATE
            ) != "null"
        ) {
            et_state?.setText(pref!!.getString(SharedPreference.KEY_STATE))
        }
        if (pref!!.getString(SharedPreference.KEY_CITY).isEmpty() && pref!!.getString(
                SharedPreference.KEY_CITY
            ) != "null"
        ) {
            et_city?.setText(pref!!.getString(SharedPreference.KEY_CITY))
        }
        if (pref!!.getString(SharedPreference.KEY_PINCODE).isEmpty() && pref!!.getString(
                SharedPreference.KEY_PINCODE
            ) != "null"
        ) {
            et_pincode?.setText(pref!!.getString(SharedPreference.KEY_PINCODE))
        }
        if (pref!!.getString(SharedPreference.KEY_STATE).isEmpty() && pref!!.getString(
                SharedPreference.KEY_STATE
            ) != "null" &&
            pref!!.getString(SharedPreference.KEY_CITY).isEmpty() && pref!!.getString(
                SharedPreference.KEY_CITY
            ) != "null" &&
            pref!!.getString(SharedPreference.KEY_PINCODE).isEmpty() && pref!!.getString(
                SharedPreference.KEY_PINCODE
            ) != "null"
        ) {
            isDataLoaded = true
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
        cddState = StateSelectionBottomSheetFragment("state", 40,  this, stateList)
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
            if (et_state!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(
                    this@LocationActivity,
                    "Please Enter State First",
                    Toast.LENGTH_SHORT
                ).show()
                et_state!!.requestFocus()
                return@OnClickListener
            }
            if (et_city!!.text.toString().trim { it <= ' ' }.isEmpty()) {
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
            startActivity(Intent(this@LocationActivity, HomeActivity::class.java))
            finish()
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
            Constants.BASE_URL + Constants.CHECK_PINCODE,
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