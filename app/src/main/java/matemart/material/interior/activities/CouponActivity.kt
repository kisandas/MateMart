package matemart.material.interior.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import matemart.material.interior.R
import matemart.material.interior.adapter.CouponItemAdapter
import matemart.material.interior.databinding.ActivityCouponBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.AllCouponResponse
import matemart.material.interior.model.CouponItemData
import matemart.material.interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CouponActivity : AppCompatActivity(), OnCouponSelectedListener {

    private var binding: ActivityCouponBinding? = null
    var selectedCouponList: ArrayList<CouponItemData> = arrayListOf()
    var amount = 0.0
    var promoCode =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding?.llHeader?.title?.text = "Coupon"
        binding?.llHeader?.ivBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        amount = intent.getDoubleExtra("amount", 0.0)


        binding!!.btnApply.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("discountAmount", discountAmount) // Add any data you want to send back
            resultIntent.putExtra("promoCode", promoCode) // Add any data you want to send back
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
        validateButton(false)
        getCouponData()
    }


    fun getCouponData() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@CouponActivity)
        var call: Call<AllCouponResponse> = apiInterface.getCouponData()!!

        call.enqueue(object : Callback<AllCouponResponse> {
            override fun onResponse(
                call: Call<AllCouponResponse>,
                response: Response<AllCouponResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {

                        if (it.isNullOrEmpty()) {

                        } else {

                            var cartList = ArrayList(it)

                            val adapter = CouponItemAdapter(
                                amount,
                                cartList,
                                this@CouponActivity,
                                this@CouponActivity
                            )
                            val layoutManager = LinearLayoutManager(this@CouponActivity)
                            binding?.rcCouponActivity?.layoutManager = layoutManager
                            binding?.rcCouponActivity?.setAdapter(adapter)


                        }

                    }
                } else {
                    Toast.makeText(
                        this@CouponActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AllCouponResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@CouponActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    var couponData: CouponItemData? = null
    var discountAmount =0.0

    override fun onCouponSelected(data: CouponItemData?) {
        couponData = data

        if (couponData != null) {
            validateButton(true)

            if (couponData!!.type == 0) {
                discountAmount =couponData!!.amount?.toDouble() ?: 0.0
            } else if (couponData!!.type == 1) {
                 discountAmount = (amount * couponData!!.amount!!.toInt()) / 100

            }
            binding!!.couponAmount.text = "₹$discountAmount"
            promoCode = couponData?.code.toString()

        } else {
            validateButton(false)
            binding!!.couponAmount.text = "₹0"
            promoCode = ""
        }

    }

    fun validateButton(isEnabled: Boolean) {
        if (isEnabled) {
            val backgroundColor =
                getColor(R.color.theme_blue_38B449) // Replace with your color resource

            val textColor =
                getColor(R.color.white)
            ViewCompat.setBackgroundTintList(
                binding!!.btnApply,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.btnApply.setTextColor(textColor)
            binding!!.btnApply.isEnabled = true
            binding!!.btnApply.isClickable = true
        } else {
            val backgroundColor =
                getColor(R.color.gray_e3e3e3) // Replace with your color resource
            val textColor =
                getColor(R.color.dark_gray)
            ViewCompat.setBackgroundTintList(
                binding!!.btnApply,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.btnApply.setTextColor(textColor)
            binding!!.btnApply.isEnabled = false
            binding!!.btnApply.isClickable = false
        }
    }




}

interface OnCouponSelectedListener {
    fun onCouponSelected(data: CouponItemData?)
}