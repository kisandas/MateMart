package com.matemart.fragments;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.SEND_OTP;
import static com.matemart.utils.SharedPreference.KEY_LOGIN_TOKEN;
import static com.matemart.utils.Utils.ERROR_MESSAGE;
import static com.matemart.utils.Utils.ERROR_TITLE;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.matemart.R;
import com.matemart.activities.OTPActivity;
import com.matemart.utils.CustomTypefaceSpan;
import com.matemart.utils.SharedPreference;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    View view;
    TextView tv_checktext;
    TextView btn_register;
    SharedPreference pref;

    EditText etName,etNumber;
    CheckBox checkBoxTerms;
    TextView tvCountryCode;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pref = new SharedPreference(getContext());
        view = inflater.inflate(R.layout.fragment_register, container, false);
        etName = view.findViewById(R.id.etName);
        etNumber = view.findViewById(R.id.etNumber);
        tv_checktext = view.findViewById(R.id.tv_checktext);
        checkBoxTerms = view.findViewById(R.id.checkBoxTerms);
        tvCountryCode = view.findViewById(R.id.tvCountryCode);


        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        setTextIntoTextView(tv_checktext, "I agree to the", " Terms & Conditions", " and", " Privacy Policy", "");


        return view;
    }

    private void checkValidation() {
        if(etName.getText().toString().isEmpty() || etName.getText().toString().length()<5){
            new Toaster.Builder(requireActivity())
                    .setTitle("ERROR")
                    .setDescription("Please Enter Valid Name")
                    .setDuration(5000)
                    .setStatus(Toaster.Status.ERROR)
                    .show();
            return;

        }
        if(etNumber.getText().toString().isEmpty() || etNumber.getText().toString().length()!=10){
            new Toaster.Builder(requireActivity())
                    .setTitle("ERROR")
                    .setDescription("Please Enter Valid Number")
                    .setDuration(5000)
                    .setStatus(Toaster.Status.ERROR)
                    .show();
            return;

        }

        if(!checkBoxTerms.isChecked()){
            new Toaster.Builder(requireActivity())
                    .setTitle("ERROR")
                    .setDescription("Please Read and Accept Terms and Conditions.")
                    .setDuration(5000)
                    .setStatus(Toaster.Status.ERROR)
                    .show();
            return;
        }

        SendOTPForLogin(tvCountryCode.getText().toString().trim()+etNumber.getText().toString().trim(),etName.getText().toString().trim());

    }

    public void setTextIntoTextView(TextView tv, String first, String second, String third, String fourth, String fifth) {


        Spannable spannable = new SpannableString(first + second + third + fourth + fifth);
        Typeface typeface_regular = ResourcesCompat.getFont(getContext(), R.font.font_inter_regular);
        Typeface typeface_bold = ResourcesCompat.getFont(getContext(), R.font.font_inter_bold);

        int first_length = 0 + first.length();
        int second_length = first_length + second.length();
        int third_length = second_length + third.length();
        int fourth_length = third_length + fourth.length();
        int fifth_length = fourth_length + fifth.length();

        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), 0, first_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_bold, ContextCompat.getColor(getContext(), R.color.theme_blue_38B449)), first_length, second_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), second_length, third_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_bold, ContextCompat.getColor(getContext(), R.color.theme_blue_38B449)), third_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), fourth_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv.setText(spannable);
    }

    public void SendOTPForLogin(String number, String name) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mo_no", number);
            jsonObject.put("uname", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, BASE_URL + SEND_OTP, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("checkResp", "onResponse: "+response.toString() );
                        try {

                            Log.e("checkResp", "onResponse: "+response.toString() );

                            int statusCode = response.optInt("statuscode");
                            String message = response.getString("message");

                            if (statusCode==11) {
                               String token =response.optJSONObject("data").optString("token");
                                pref.setString(KEY_LOGIN_TOKEN, token);

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
                                    .setDescription(e.getMessage())
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
                                .setDescription(error.getMessage())
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