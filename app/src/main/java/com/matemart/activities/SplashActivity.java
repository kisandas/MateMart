package com.matemart.activities;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.GET_STATE;
import static com.matemart.api.Constants.VERIFY_OTP;
import static com.matemart.api.Constants.statList;
import static com.matemart.utils.SharedPreference.KEY_CCID;
import static com.matemart.utils.SharedPreference.KEY_CITY;
import static com.matemart.utils.SharedPreference.KEY_LOGIN_TOKEN;
import static com.matemart.utils.SharedPreference.KEY_PINCODE;
import static com.matemart.utils.SharedPreference.KEY_STATE;
import static com.matemart.utils.Utils.ERROR_MESSAGE;
import static com.matemart.utils.Utils.ERROR_TITLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.matemart.MainActivity;
import com.matemart.R;
import com.matemart.model.StateAndCityModel;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        VerifyOTP();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        },2000);
    }


    public void VerifyOTP() {


        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest mStringRequest = new StringRequest(Request.Method.GET, BASE_URL + GET_STATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String strresponse) {
                JSONObject response = null;
                try {
                    response = new JSONObject(strresponse);

                    int statusCode = response.optInt("statuscode");
                    String message = response.optString("message");
                    if (statusCode==11) {

                        JSONObject objData = response.optJSONObject("data");
                        JSONObject objState = objData.optJSONObject("states");
                        Iterator keys = objState.keys();
                        while (keys.hasNext()){
                            String key = String.valueOf(keys.next());
                            Log.e("checkKEY", "State: "+key );
                            JSONArray arrCity = objState.optJSONArray(key);

                            ArrayList<String> cityList = new ArrayList<>();
                            for (int i = 0; i <arrCity.length() ; i++) {
                                String ans = arrCity.optString(i);
                                cityList.add(ans);
                                Log.e("checkKEY", "onResponse: city "+ans );
                            }
                            statList.add(new StateAndCityModel(key,cityList));
                        }


                        startActivity(new Intent(SplashActivity.this, OTPActivity.class));
                        finish();


                    } else {
                        new Toaster.Builder(SplashActivity.this)
                                .setTitle("Error")
                                .setDescription(message)
                                .setDuration(5000)
                                .setStatus(Toaster.Status.ERROR)
                                .show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        queue.add(mStringRequest);


    }
}