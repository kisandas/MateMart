package com.matemart.activities;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.SEND_OTP;
import static com.matemart.api.Constants.VERIFY_OTP;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.matemart.MainActivity;
import com.matemart.R;
import com.matemart.utils.OTPView;
import com.matemart.utils.SharedPreference;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class OTPActivity extends AppCompatActivity {
TextView btn_verify;
    SharedPreference pref;
    OTPView otp_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        btn_verify = findViewById(R.id.btn_verify);
        otp_view = findViewById(R.id.otp_view);

        otp_view.setOnFinishListener(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                return null;
            }
        });

        pref = new SharedPreference(this);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyOTP("1234");
            }
        });
    }


    public void VerifyOTP(String otp) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("token", pref.getString(KEY_LOGIN_TOKEN));
            jsonObject.put("verification_code", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, BASE_URL + VERIFY_OTP, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int statusCode = response.optInt("statuscode");
                            String message = response.getString("message");
                            if (statusCode==11) {

                                JSONObject objData = response.optJSONObject("data");
                                String state = objData.optString("state");
                                String city = objData.optString("city");
                                String pincode = objData.optString("pincode");
                                String ccid = objData.optString("ccid");
                                if(!state.isEmpty() && !state.equalsIgnoreCase("null")) {
                                    pref.setString(KEY_STATE, state);
                                }
                                if(!city.isEmpty() && !city.equalsIgnoreCase("null")) {
                                    pref.setString(KEY_CITY, city);
                                }
                                if(!pincode.isEmpty() && !pincode.equalsIgnoreCase("null")) {
                                    pref.setString(KEY_PINCODE, pincode);
                                }
                                if(!ccid.isEmpty() && !ccid.equalsIgnoreCase("null")) {
                                    pref.setString(KEY_CCID, ccid);
                                }

                                new Toaster.Builder(OTPActivity.this)
                                        .setTitle("Success")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.SUCCESS)
                                        .show();

                                startActivity(new Intent(OTPActivity.this, LocationActivity.class));
                                finish();


                            } else {
                                new Toaster.Builder(OTPActivity.this)
                                        .setTitle("Error")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.ERROR)
                                        .show();

                            }
                        } catch (Exception e) {
                            new Toaster.Builder(OTPActivity.this)
                                    .setTitle(ERROR_TITLE)
                                    .setDescription(ERROR_MESSAGE)
                                    .setDuration(5000)
                                    .setStatus(Toaster.Status.ERROR)
                                    .show();

                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        new Toaster.Builder(OTPActivity.this)
                                .setTitle("Error")
                                .setDescription(ERROR_MESSAGE)
                                .setDuration(5000)
                                .setStatus(Toaster.Status.ERROR)
                                .show();

                    }

                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
//                header.put(HEADER_APP_PLATFORM, HEADER_ANDROID);
//                header.put(HEADER_APP_VERSION, BuildConfig.VERSION_NAME);
                return header;
            }

        };

        queue.add(jsonObjectRequest);
    }
}