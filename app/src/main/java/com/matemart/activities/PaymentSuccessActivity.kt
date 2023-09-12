package com.matemart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matemart.R
import com.matemart.databinding.ActivityPaymentSuccessBinding
import com.matemart.databinding.ActivityWebViewBinding

class PaymentSuccessActivity : AppCompatActivity() {
    private var binding: ActivityPaymentSuccessBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}