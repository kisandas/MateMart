package com.matemart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matemart.R
import com.matemart.databinding.ActivityContactUsBinding
import com.matemart.databinding.ActivityPostYourRequirementsBinding

class ContactUsActivity : AppCompatActivity() {

    private var binding: ActivityContactUsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}