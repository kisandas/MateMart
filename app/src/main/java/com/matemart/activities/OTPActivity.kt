package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.matemart.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class OTPActivity : AppCompatActivity() {
    var btn_verify: TextView? = null
    var pref: SharedPreference? = null
    var otp_view: OTPView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)
        btn_verify = findViewById(R.id.btn_verify)
        otp_view = findViewById(R.id.otp_view)
        otp_view?.setOnFinishListener { s: String? -> null }
        pref = SharedPreference(this)
        btn_verify?.setOnClickListener(View.OnClickListener { VerifyOTP("1234") })
    }

    fun VerifyOTP(otp: String?) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("token", pref!!.getString(SharedPreference.KEY_LOGIN_TOKEN))
            jsonObject.put("verification_code", otp)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            Constants.BASE_URL + Constants.VERIFY_OTP,
            jsonObject,
            Response.Listener { response ->
                try {
                    val statusCode = response.optInt("statuscode")
                    val message = response.getString("message")
                    if (statusCode == 11) {
                        val objData = response.optJSONObject("data")
                        val state = objData.optString("state")
                        val city = objData.optString("city")
                        val pincode = objData.optString("pincode")
                        val ccid = objData.optString("ccid")
                        if (!state.isEmpty() && !state.equals("null", ignoreCase = true)) {
                            pref!!.setString(SharedPreference.KEY_STATE, state)
                        }
                        if (!city.isEmpty() && !city.equals("null", ignoreCase = true)) {
                            pref!!.setString(SharedPreference.KEY_CITY, city)
                        }
                        if (!pincode.isEmpty() && !pincode.equals("null", ignoreCase = true)) {
                            pref!!.setString(SharedPreference.KEY_PINCODE, pincode)
                        }
                        if (!ccid.isEmpty() && !ccid.equals("null", ignoreCase = true)) {
                            pref!!.setString(SharedPreference.KEY_CCID, ccid)
                        }
                        Toaster.Builder(this@OTPActivity)
                            .setTitle("Success")
                            .setDescription(message)
                            .setDuration(5000)
                            .setStatus(Toaster.Status.SUCCESS)
                            .show()
                        startActivity(Intent(this@OTPActivity, LocationActivity::class.java))
                        finish()
                    } else {
                        Toaster.Builder(this@OTPActivity)
                            .setTitle("Error")
                            .setDescription(message)
                            .setDuration(5000)
                            .setStatus(Toaster.Status.ERROR)
                            .show()
                    }
                } catch (e: Exception) {
                    Toaster.Builder(this@OTPActivity)
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
                Toaster.Builder(this@OTPActivity)
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
}