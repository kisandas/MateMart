package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.matemart.R
import com.matemart.api.Constants
import com.matemart.model.StateAndCityModel
import com.matemart.utils.MyApplication
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.SharedPrefHelper.Companion.KEY_ACCESS_TOKEN
import com.matemart.utils.SharedPrefHelper.Companion.KEY_CCID
import com.matemart.utils.Toast.Toaster
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {

    var pref: SharedPrefHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
//        if (true) {
//
//            startActivity(Intent(this, LabouresListActivity::class.java))
//            finish()
//        }
        state


        //        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//            }
//        },2000);
    }

    val state: Unit
        get() {
            val queue = Volley.newRequestQueue(this)
            val mStringRequest = StringRequest(
                Request.Method.GET,
                Constants.BASE_URL + "get-states",
                { strresponse ->
                    var response: JSONObject? = null
                    try {
                        response = JSONObject(strresponse)
                        val statusCode = response.optInt("statuscode")
                        val message = response.optString("message")
                        if (statusCode == 11) {
                            val objData = response.optJSONObject("data")
                            val objState = objData.optJSONObject("states")
                            val keys: Iterator<*> = objState.keys()
                            while (keys.hasNext()) {
                                val key = keys.next().toString()
                                Log.e("checkKEY", "State: $key")
                                val arrCity = objState.optJSONArray(key)
                                val cityList = ArrayList<String>()
                                for (i in 0 until arrCity.length()) {
                                    val ans = arrCity.optString(i)
                                    cityList.add(ans)
                                    Log.e("checkKEY", "onResponse: city $ans")
                                }
                                Constants.statList.add(StateAndCityModel(key, cityList))
                            }


                            Log.e("checkAccessToken", ": " + pref?.read(KEY_ACCESS_TOKEN))
                            if (!pref?.read(KEY_CCID).isNullOrEmpty()) {
                                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }

                        } else {
                            Toaster.Builder(this@SplashActivity)
                                .setTitle("Error")
                                .setDescription(message)
                                .setDuration(5000)
                                .setStatus(Toaster.Status.ERROR)
                                .show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { }
            queue.add(mStringRequest)
        }
}