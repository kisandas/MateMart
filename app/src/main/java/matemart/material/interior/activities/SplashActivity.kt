package matemart.material.interior.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import matemart.material.interior.BuildConfig
import matemart.material.interior.R
import matemart.material.interior.api.Constants.BASE_URL
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.AppData
import matemart.material.interior.model.AppDataResponse
import matemart.material.interior.model.StateAndCityModel
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import matemart.material.interior.utils.SharedPrefHelper.Companion.KEY_ACCESS_TOKEN
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                matemart.material.interior.api.Constants.BASE_URL + "get-states",
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
                                }
                                matemart.material.interior.api.Constants.statList.add(StateAndCityModel(key, cityList))
                            }


                            getAPPDataAPI()

                            Log.e("checkAccessToken", ": " + pref?.read(KEY_ACCESS_TOKEN))


                        } else {
                            matemart.material.interior.utils.Toast.Toaster.Builder(this@SplashActivity)
                                .setTitle("Error")
                                .setDescription(message)
                                .setDuration(5000)
                                .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR)
                                .show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { }
            queue.add(mStringRequest)
        }


    fun callGetAppDataAPI() {
        if (!pref?.read(KEY_ACCESS_TOKEN).isNullOrEmpty()) {
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
    }

    fun getAPPDataAPI() {



        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SplashActivity)
        val api: ApiInterface by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
        var call: Call<AppDataResponse> = api.getAppUpdateData("Android", "1.0")!!



        call.enqueue(object : Callback<AppDataResponse> {
            override fun onResponse(
                call: Call<AppDataResponse>,
                response: Response<AppDataResponse>
            ) {

                if (response.body()?.statuscode == 11) {

                    if (response.body()?.data?.appVersionData?.release!! > BuildConfig.VERSION_CODE) {
                        showAppUpdateDialog(response.body()!!)
                    } else {
                        if (!pref?.read(KEY_ACCESS_TOKEN).isNullOrEmpty() && pref?.read(KEY_ACCESS_TOKEN) !="") {
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

                    }

                } else {
                    matemart.material.interior.utils.Toast.Toaster.Builder(this@SplashActivity)
                        .setTitle("ERROR")
                        .setDescription(response.body()?.message)
                        .setDuration(5000)
                        .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR)
                        .show()
                }
            }

            override fun onFailure(call: Call<AppDataResponse>, t: Throwable) {
                t.printStackTrace()

                matemart.material.interior.utils.Toast.Toaster.Builder(this@SplashActivity)
                    .setTitle("ERROR")
                    .setDescription(t.message)
                    .setDuration(5000)
                    .setStatus(matemart.material.interior.utils.Toast.Toaster.Status.ERROR)
                    .show()
            }

        })

    }

    fun showAppUpdateDialog(body: AppDataResponse) {

        startActivity(Intent(this@SplashActivity,AppUpdateActivity::class.java).putExtra("data",body))

    }


    companion object {
        var APP_DATA: AppData? = null
    }

}