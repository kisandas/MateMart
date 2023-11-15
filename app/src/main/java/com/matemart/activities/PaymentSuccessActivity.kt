package com.matemart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import com.matemart.R
import com.matemart.databinding.ActivityPaymentSuccessBinding
import com.matemart.databinding.ActivityWebViewBinding

class PaymentSuccessActivity : AppCompatActivity() {
    private var binding: ActivityPaymentSuccessBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val order_number: String? = intent.getStringExtra("order_number")
        val amount: String? = intent.getStringExtra("amount")
        val date: String? = intent.getStringExtra("date")

        binding!!.tvOrderNo.text = "Order No: $order_number"
        binding!!.tvAmount.text = "₹$amount"
        binding!!.tvDate.text = "₹$date"

        binding!!.llheaderlay.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.llheaderlay.title.text = "Payment Success"
    }
}