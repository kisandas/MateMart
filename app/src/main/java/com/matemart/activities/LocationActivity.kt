package com.matemart.activities;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.CHECK_PINCODE;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.matemart.MainActivity;
import com.matemart.R;
import com.matemart.utils.SharedPreference;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {
    TextView btn_save;
    AutoCompleteTextView et_state;
    AutoCompleteTextView et_city;
    AutoCompleteTextView et_pincode;
    SharedPreference pref;

    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_state;
    ArrayList<String> stateList = new ArrayList<>();
    boolean isStateSelected = false;
    boolean isCountrySelected = false;
    boolean isPINCODE_VERIFIED = false;
    boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        pref = new SharedPreference(this);
        btn_save = findViewById(R.id.btn_save);
        et_state = findViewById(R.id.et_state);
        et_city = findViewById(R.id.et_city);
        et_pincode = findViewById(R.id.et_pincode);


        if (pref.getString(KEY_STATE).isEmpty() && !pref.getString(KEY_STATE).equals("null")) {
            et_state.setText(pref.getString(KEY_STATE));
        }

        if (pref.getString(KEY_CITY).isEmpty() && !pref.getString(KEY_CITY).equals("null")) {
            et_city.setText(pref.getString(KEY_CITY));
        }

        if (pref.getString(KEY_PINCODE).isEmpty() && !pref.getString(KEY_PINCODE).equals("null")) {
            et_pincode.setText(pref.getString(KEY_PINCODE));
        }

        if(pref.getString(KEY_STATE).isEmpty() && !pref.getString(KEY_STATE).equals("null") &&
                pref.getString(KEY_CITY).isEmpty() && !pref.getString(KEY_CITY).equals("null") &&
                pref.getString(KEY_PINCODE).isEmpty() && !pref.getString(KEY_PINCODE).equals("null")){

            isDataLoaded = true;
        }

        initializeView();

    }

    public void initializeView(){
        for (int i = 0; i < statList.size(); i++) {
            stateList.add(statList.get(i).getState());
        }


        adapter_state = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, stateList);


        et_state.setThreshold(1);
        et_state.setAdapter(adapter_state);


        et_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("checkText", "onTextChanged: " + charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("checkText", "afterTextChanged: " + editable.toString());
                if (stateList.contains(editable.toString())) {
                    isStateSelected = true;
                    getPositionFromStateListAndSetAdapterForCity(editable.toString());
                } else {
                    isStateSelected = false;
                }
            }
        });

        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isStateSelected) {
                    et_state.requestFocus();
                    Toast.makeText(LocationActivity.this, "Please Select State First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!isStateSelected) {
                    et_city.setText("");
                    Toast.makeText(LocationActivity.this, "Please Select State First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isPINCODE_VERIFIED = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_city.getText().toString().isEmpty()) {
                    et_city.requestFocus();
                    Toast.makeText(LocationActivity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editable.toString().length() == 6) {
                    VerifyPINCODE(et_city.getText().toString(), editable.toString());
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_state.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LocationActivity.this, "Please Enter State First", Toast.LENGTH_SHORT).show();
                    et_state.requestFocus();
                    return;
                }

                if (et_city.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LocationActivity.this, "Please Enter City First", Toast.LENGTH_SHORT).show();
                    et_city.requestFocus();
                    return;
                }

                if (!isPINCODE_VERIFIED) {
                    Toast.makeText(LocationActivity.this, "Please Enter Valid PinCode", Toast.LENGTH_SHORT).show();
                    et_pincode.requestFocus();
                    return;
                }
                startActivity(new Intent(LocationActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    public void getPositionFromStateListAndSetAdapterForCity(String state) {
        int position = stateList.indexOf(state);
        adapter_city = new ArrayAdapter<String>
                (LocationActivity.this, android.R.layout.select_dialog_item, statList.get(position).getCityList());
        et_city.setThreshold(1);
        et_city.setAdapter(adapter_city);
    }

    public void VerifyPINCODE(String CITY, String PINCODE) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("city", CITY);
            jsonObject.put("pincode", PINCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, BASE_URL + CHECK_PINCODE, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int statusCode = response.optInt("statuscode");
                            String message = response.getString("message");
                            if (statusCode == 11) {
                                isPINCODE_VERIFIED = true;
                                new Toaster.Builder(LocationActivity.this)
                                        .setTitle("Success")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.SUCCESS)
                                        .show();

//                                startActivity(new Intent(LocationActivity.this, HomeActivity.class));

                            } else {
                                new Toaster.Builder(LocationActivity.this)
                                        .setTitle("Error")
                                        .setDescription(message)
                                        .setDuration(5000)
                                        .setStatus(Toaster.Status.ERROR)
                                        .show();

                            }
                        } catch (Exception e) {
                            new Toaster.Builder(LocationActivity.this)
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

                        new Toaster.Builder(LocationActivity.this)
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