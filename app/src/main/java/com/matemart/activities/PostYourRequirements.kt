package com.matemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.matemart.R
import com.matemart.databinding.ActivityPolicyDetailsBinding
import com.matemart.databinding.ActivityPostYourRequirementsBinding
import com.matemart.utils.MyApplication
import com.matemart.utils.SharedPrefHelper

class PostYourRequirements : AppCompatActivity() {


    private var binding: ActivityPostYourRequirementsBinding? = null
    lateinit var pref: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostYourRequirementsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

        binding!!.llHeader.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.llHeader.title.text = "Post your Requirements"

        binding!!.etName.setText(pref.read(SharedPrefHelper.USER_NAME, "").toString())
        binding!!.etMobile.setText(pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString())



    }
}