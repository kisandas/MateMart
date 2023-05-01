package com.matemart.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.widget.EditText
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devs.readmoreoption.ReadMoreOption
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.makeramen.roundedimageview.RoundedImageView
import com.matemart.utils.SharedPreference
import com.matemart.fragments.ChoosePictureBottomSheetFragment
import com.google.android.material.textfield.TextInputLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.matemart.model.StateAndCityModel
import com.matemart.activities.LoginActivity
import com.matemart.utils.Toast.Toaster
import com.android.volley.VolleyError
import com.matemart.R
import com.matemart.fragments.LoginFragment
import com.matemart.fragments.RegisterFragment
import com.matemart.interfaces.DismissBottomSheet
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

/**
 * ProfileActivity.class is Activity for ProfileActivity Check and Update.
 * Developed By Kisandas
 * last updated 27/09/2022 for GreetBuzz Ltd.
 */
class ProfileActivity : AppCompatActivity() {
    var iv_profile_pic: CircleImageView? = null
    var iv_editProfile: ImageView? = null
    var newImagePath = ""
    var tv_name: TextView? = null
    var tv_social_text: MultiAutoCompleteTextView? = null
    var et_name: EditText? = null
    var tv_category: TextInputEditText? = null
    var iv_countryFlag: RoundedImageView? = null
    var tv_select_country: TextInputEditText? = null
    var tv_select_state: TextInputEditText? = null
    var selected_category: String? = null
    var et_number: MultiAutoCompleteTextView? = null
    var et_email: MultiAutoCompleteTextView? = null
    var et_address: MultiAutoCompleteTextView? = null
    var tv_select_language: TextInputEditText? = null
    var et_website: MultiAutoCompleteTextView? = null
    var rc_socialMedia: RecyclerView? = null
    var btn_save_profile: TextView? = null
    var tv_version: TextView? = null
    var pref: SharedPreference? = null
    var cdd: ChoosePictureBottomSheetFragment? = null
    var isProfileDPUpdate = false
    var isDataUpdate = false
    var til_number: TextInputLayout? = null
    var til_email: TextInputLayout? = null
    var til_website: TextInputLayout? = null
    var til_social: TextInputLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

//        initializeView();
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    //    private void initializeView() {
    //        pref = new SharedPreference(this);
    //
    //
    //        iv_profile_pic = findViewById(R.id.iv_profile_pic);
    //        iv_editProfile = findViewById(R.id.iv_editProfile);
    //        tv_name = findViewById(R.id.tv_name);
    //
    //        et_name = findViewById(R.id.et_name);
    //        tv_category = findViewById(R.id.tv_category);
    //        iv_countryFlag = findViewById(R.id.iv_countryFlag);
    //        tv_select_country = findViewById(R.id.tv_select_country);
    //        tv_select_state = findViewById(R.id.tv_select_state);
    //
    //        rc_socialMedia = findViewById(R.id.rc_socialMedia);
    //
    //        btn_save_profile = findViewById(R.id.btn_save_profile);
    //        tv_version = findViewById(R.id.tv_version);
    //
    //        tv_select_language = findViewById(R.id.tv_select_language);
    //
    //        til_number = findViewById(R.id.til_number);
    //        til_email = findViewById(R.id.til_email);
    //        til_website = findViewById(R.id.til_website);
    //        til_social = findViewById(R.id.til_social);
    //
    //        et_number = (MultiAutoCompleteTextView) findViewById(R.id.et_number);
    //        et_email = (MultiAutoCompleteTextView) findViewById(R.id.et_email);
    //        et_address = (MultiAutoCompleteTextView) findViewById(R.id.et_address);
    //        et_website = (MultiAutoCompleteTextView) findViewById(R.id.et_website);
    //        tv_social_text = (MultiAutoCompleteTextView) findViewById(R.id.tv_social_text);
    //
    //
    //
    //
    //        ColorDrawable cd = new ColorDrawable(Color.parseColor("#F3F3F3"));
    //        tv_social_text.setDropDownBackgroundDrawable(cd);
    //        et_email.setDropDownBackgroundDrawable(cd);
    //        et_number.setDropDownBackgroundDrawable(cd);
    //        et_website.setDropDownBackgroundDrawable(cd);
    //        et_address.setDropDownBackgroundDrawable(cd);
    //
    //
    //    }
    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}