package com.matemart.fragments;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.SEND_OTP;
import static com.matemart.utils.SharedPreference.KEY_LOGIN_TOKEN;
import static com.matemart.utils.Utils.ERROR_MESSAGE;
import static com.matemart.utils.Utils.ERROR_TITLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.matemart.R;
import com.matemart.activities.LoginActivity;
import com.matemart.activities.OTPActivity;
import com.matemart.utils.SharedPreference;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {
    View view;
    TextView btn_login;
    SharedPreference pref;
    EditText etNumber;
    TextView  tvCountryCode;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        pref = new SharedPreference(getContext());
        etNumber = view.findViewById(R.id.etNumber);
        btn_login = view.findViewById(R.id.btn_login);
        tvCountryCode = view.findViewById(R.id.tvCountryCode);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
//                startActivity(new Intent(getContext(), OTPActivity.class));
            }
        });
        return view;
    }


    private void checkValidation() {

        if(etNumber.getText().toString().isEmpty() || etNumber.getText().toString().length()!=10){
            new Toaster.Builder(requireActivity())
                    .setTitle("ERROR")
                    .setDescription("Please Enter Valid Number")
                    .setDuration(5000)
                    .setStatus(Toaster.Status.ERROR)
                    .show();
            return;

        }



        SendOTPForLogin(tvCountryCode.getText().toString().trim()+etNumber.getText().toString().trim());

    }

    public void SendOTPForLogin(String number) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mo_no", number);
//            jsonObject.put("uname", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, BASE_URL + SEND_OTP, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int statusCode = response.optInt("statuscode");
                            String message = response.getString("message");


                            if (statusCode==11) {
                                String token =response.optJSONObject("data").optString("token");
                                pref.setString(KEY_LOGIN_TOKEN, token);

                                Log.e("checkToken", "onResponse: "+token );

                                new Toaster.Builder(requireActivity())
                                        .setTitle("Success")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.SUCCESS)
                                        .show();

                                startActivity(new Intent(requireActivity(), OTPActivity.class));


                            } else {
                                new Toaster.Builder(requireActivity())
                                        .setTitle("Error")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.ERROR)
                                        .show();

                            }
                        } catch (Exception e) {
                            new Toaster.Builder(requireActivity())
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

                        new Toaster.Builder(requireActivity())
                                .setTitle("Error")
                                .setDescription(ERROR_MESSAGE)
                                .setDuration(5000)
                                .setStatus(Toaster.Status.ERROR)
                                .show();

                    }

                }) {
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