package com.matemart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matemart.R
import com.matemart.databinding.ActivityPaymentFailedBinding
import com.matemart.databinding.ActivityPolicyDetailsBinding

class PolicyDetailsActivity : AppCompatActivity() {

    private var binding: ActivityPolicyDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        binding!!.headerLay.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding!!.headerLay.title.text = "Policy"


        val shippingURl = "https://www.matemart.in/shippingpolicy"
        val returnPolicy = "https://www.matemart.in/returnpolicy"
        val termsCondition = "https://www.matemart.in/terms and condition"
        val privacyPolicy = "https://www.matemart.in/privacypolicy"
        val FAQ = "https://www.matemart.in/faq"

        binding!!.tvShippingPolicy.setOnClickListener {
            startActivity(
                Intent(
                    this@PolicyDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", shippingURl))
        }

        binding!!.tvReturnPolicy.setOnClickListener {
            startActivity(
                Intent(
                    this@PolicyDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", returnPolicy))
        }

        binding!!.tvTermsAndCondition.setOnClickListener {
            startActivity(
                Intent(
                    this@PolicyDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", termsCondition))
        }

        binding!!.tvPrivacyPolicy.setOnClickListener {
            startActivity(
                Intent(
                    this@PolicyDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", privacyPolicy))
        }

        binding!!.tvFAQ.setOnClickListener {
            startActivity(
                Intent(
                    this@PolicyDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", FAQ))
        }
    }
}