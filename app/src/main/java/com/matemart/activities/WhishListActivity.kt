package com.matemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devs.readmoreoption.ReadMoreOption
import androidx.core.content.ContextCompat
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

class WhishListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whish_list)
    }
}