package com.matemart.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.CartItemAdapter
import com.matemart.databinding.ActivityCartBinding
import com.matemart.fragments.ChoosePictureBottomSheetFragment
import com.matemart.fragments.CitySelectionBottomSheetFragment
import com.matemart.interfaces.ApiInterface
import com.matemart.model.AllOrderResponseResponse
import com.matemart.model.CartDataModel
import com.matemart.model.CartResponseModel
import com.matemart.model.RazorPayURLResponse
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartActivity : AppCompatActivity() {
    private var binding: ActivityCartBinding? = null
    var paymentMode = 0

    companion object {

        val REQUEST_CODE_COUPON = 203
    }

    var amount = 0.0
    var cartListItem = 0
    var PromoCode = ""
    var discountAmount = 0

    var ProductList: ArrayList<CartDataModel>? = arrayListOf()
    var cdd: ChoosePictureBottomSheetFragment? = null
    var pref: SharedPrefHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        pref = SharedPrefHelper.getInstance(MyApplication())

        binding!!.cartHeader.title.text = "Cart"

        binding!!.llEmptyView.visibility = GONE
        binding!!.llCartItemLayout.visibility = GONE

        binding!!.cartHeader.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        validateButton(false)

        binding!!.llNonEmptyCart.rbRazorPay.setOnClickListener {
            paymentMode = 1
            validateButton(true)
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = true
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = false
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = false
        }

        binding!!.llNonEmptyCart.rbPaymentReceipt.setOnClickListener {
            paymentMode = 2
            validateButton(true)
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = false
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = true
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = false
        }

        binding!!.llNonEmptyCart.rbCashOnDelivery.setOnClickListener {
            paymentMode = 3
            validateButton(true)
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = false
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = false
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = true
        }

        binding!!.llNonEmptyCart.llRazorPay.setOnClickListener {
            paymentMode = 1
            validateButton(true)
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = true
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = false
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = false
        }

        binding!!.llNonEmptyCart.llPaymentReceipt.setOnClickListener {
            paymentMode = 2
            validateButton(true)
            uploadPaymentReciept()
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = false
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = true
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = false
        }

        binding!!.llNonEmptyCart.llCashOnDelivery.setOnClickListener {
            paymentMode = 3
            validateButton(true)
            binding!!.llNonEmptyCart.rbRazorPay.isChecked = false
            binding!!.llNonEmptyCart.rbPaymentReceipt.isChecked = false
            binding!!.llNonEmptyCart.rbCashOnDelivery.isChecked = true
        }


        binding!!.llNonEmptyCart.llApplyCoupon.setOnClickListener {
            val intent = Intent(this, CouponActivity::class.java)
            intent.putExtra("amount", amount)
            startActivityForResult(intent, REQUEST_CODE_COUPON)
//            startActivity(Intent(this@CartActivity, CouponActivity::class.java).)
        }

        getCartData()

        binding?.llNonEmptyCart?.btnConfirmOrder?.setOnClickListener {
            if (paymentMode == 0) {
                Toast.makeText(
                    this@CartActivity,
                    "Please Select Payment Method",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (paymentMode == 1) {
//                RazorPay
                createRazorPayPaymentLink(amount)
            } else if (paymentMode == 2) {
//                Payment Receipt

//                uploadPaymentReciept()
            } else if (paymentMode == 3) {
//                Cash on Delivery

                createOrder(1, 0, null)
            }


        }

    }

    fun getCartData() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@CartActivity)
        var call: Call<CartResponseModel> = apiInterface.getCartData()!!

        call.enqueue(object : Callback<CartResponseModel> {
            override fun onResponse(
                call: Call<CartResponseModel>,
                response: Response<CartResponseModel>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {

                        if (it.isNullOrEmpty()) {
                            binding?.llEmptyView?.visibility = VISIBLE
                            binding?.llCartItemLayout?.visibility = GONE
                        } else {
                            binding?.llEmptyView?.visibility = GONE
                            binding?.llCartItemLayout?.visibility = VISIBLE
                            var cartList = ArrayList(it)
                            val adapter = CartItemAdapter(cartList, this@CartActivity)
                            val layoutManager = LinearLayoutManager(this@CartActivity)
                            binding?.llNonEmptyCart?.rcCart?.layoutManager = layoutManager
                            binding?.llNonEmptyCart?.rcCart?.setAdapter(adapter)

                            var total = 0.0
                            var total_gst = 0.0
                            var coupon_discount = 0
                            var discount_amount = 0
                            var servicecharge = 0

                            ProductList = cartList
                            for (item in cartList) {


                                total += ((item.qty)?.times((item.saleprice?.toDouble()!!))!!)
                                total_gst += (item?.qty!!.times(item.saleprice?.toDouble()!!)).times(
                                    item.gstper?.div(
                                        100
                                    )!!
                                )
                            }

                            binding?.llNonEmptyCart?.tvSubtotal?.text =
                                String.format("%.2f", total).toDouble().toString()
                            binding?.llNonEmptyCart?.tvTax?.text =
                                String.format("%.2f", total_gst).toDouble().toString()


                            binding?.llNonEmptyCart?.tvPayableAmount?.text =
                                String.format("%.2f", (total_gst + total)).toDouble().toString()
                            cartListItem = cartList.size
                            binding?.llNonEmptyCart?.btnConfirmOrder?.text =
                                "Buy " + cartList.size + " items for " +  String.format("%.2f", (total_gst + total)).toDouble().toString()
                            amount = String.format("%.2f", (total_gst + total)).toDouble()
                        }

                    }
                } else {
                    Toast.makeText(
                        this@CartActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CartResponseModel>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@CartActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    fun createRazorPayPaymentLink(amount: Double) {
        val u_id = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(SharedPrefHelper.USER_ID, 0)
        var a_id = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(SharedPrefHelper.ADDRESS_ID, 0)
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("user_id", u_id.toString())
        jsonObject.addProperty("amount", amount)
        jsonObject.addProperty("a_id", a_id)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@CartActivity)
        var call: Call<RazorPayURLResponse> = apiInterface.getRazorPayPaymentURL(jsonObject)!!

        call.enqueue(object : Callback<RazorPayURLResponse> {
            override fun onResponse(
                call: Call<RazorPayURLResponse>,
                response: Response<RazorPayURLResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    val paymentUrl = response.body()?.data?.razorpayLink
                    startActivity(
                        Intent(
                            this@CartActivity,
                            WebViewActivity::class.java
                        ).putExtra("url", paymentUrl)
                    )
                } else {
                    Toast.makeText(
                        this@CartActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RazorPayURLResponse>, t: Throwable) {
                Toast.makeText(
                    this@CartActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

    fun uploadPaymentReciept() {

        cdd = ChoosePictureBottomSheetFragment(
            "receipt",
            40
        )
        cdd!!.show(supportFragmentManager, "TAG2")

//        ImagePicker.with(this@CartActivity)
//            .crop(1f, 1f) //Crop image(Optional), Check Customization for more option
//            .compress(1024) //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            ) //Final image resolution will be less than 1080 x 1080(Optional)
//            .start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            discountAmount = data?.getIntExtra("discountAmount", 0)!!
            Log.e("discountAmount", "onActivityResult: " + discountAmount)
            binding!!.llNonEmptyCart.tvCouponDiscount.text = "-$discountAmount"

            PromoCode = data.getStringExtra("promoCode").toString()
            amount = amount.minus(discountAmount)
            binding!!.llNonEmptyCart.tvPayableAmount.text = "₹$amount"
            binding?.llNonEmptyCart?.btnConfirmOrder?.text =
                "Buy $cartListItem items for ₹$amount"
            // Handle the result here
        }
    }


    fun validateButton(isEnabled: Boolean) {
        if (isEnabled) {
            val backgroundColor =
                getColor(R.color.theme_blue_38B449) // Replace with your color resource

            val textColor =
                getColor(R.color.white)
            ViewCompat.setBackgroundTintList(
                binding!!.llNonEmptyCart.btnConfirmOrder,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.llNonEmptyCart.btnConfirmOrder.setTextColor(textColor)
            binding!!.llNonEmptyCart.btnConfirmOrder.isEnabled = true
            binding!!.llNonEmptyCart.btnConfirmOrder.isClickable = true
        } else {
            val backgroundColor =
                getColor(R.color.gray_e3e3e3) // Replace with your color resource
            val textColor =
                getColor(R.color.dark_gray)
            ViewCompat.setBackgroundTintList(
                binding!!.llNonEmptyCart.btnConfirmOrder,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.llNonEmptyCart.btnConfirmOrder.setTextColor(textColor)
            binding!!.llNonEmptyCart.btnConfirmOrder.isEnabled = false
            binding!!.llNonEmptyCart.btnConfirmOrder.isClickable = false
        }
    }


    fun createOrder(p_status: Int, paymetStatus: Int, imageUrl: String?) {
        val u_id = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(SharedPrefHelper.USER_ID, 0)
        var a_id = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(SharedPrefHelper.ADDRESS_ID, 0)
        var jsonObject: JsonObject = JsonObject()
        var productArray = JsonArray()


        ProductList?.forEach { productDetail ->
            val productObject = JsonObject()
            productObject.addProperty("product_detail_id", productDetail.productDetailId)
            productObject.addProperty("qty", productDetail.qty)
            productObject.addProperty("sample", productDetail.sample)
            productObject.addProperty("discount_amount", 0)
            productArray.add(productObject)
        }

        jsonObject.addProperty("amount", amount)
        if (PromoCode.isNotEmpty()) {
            jsonObject.addProperty("promo", PromoCode)
            jsonObject.addProperty("promocode_amount", discountAmount)
        }
        jsonObject.addProperty("p_status", p_status)
        jsonObject.addProperty("paymentstatus", paymetStatus)
        jsonObject.addProperty("a_id", a_id)
        jsonObject.addProperty(
            "delivery_instructions",
            binding?.llNonEmptyCart?.etDeliveryInstruction?.text.toString().trim()
        )
        if (imageUrl != null)
            jsonObject.addProperty("image_url", imageUrl)
        jsonObject.add("product", productArray)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@CartActivity)
        var call: Call<AllOrderResponseResponse> = apiInterface.createOrder(jsonObject)!!

        call.enqueue(object : Callback<AllOrderResponseResponse> {
            override fun onResponse(
                call: Call<AllOrderResponseResponse>,
                response: Response<AllOrderResponseResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Log.e("ccchhhhchh", "onResponse: " + response.body().toString())

                } else {

                }
            }

            override fun onFailure(call: Call<AllOrderResponseResponse>, t: Throwable) {
                Toast.makeText(
                    this@CartActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

}

