package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.matemart.adapter.ItemOrderDetailsListAdapter
import com.matemart.databinding.ActivityMyOrderDetailBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.AddCartResponse
import com.matemart.model.OrderDetail
import com.matemart.model.ResponseSingleOrderData
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.file.Paths


class MyOrderDetailActivity : AppCompatActivity(), ItemOrderDetailsListAdapter.ItemClickListener {
    private var binding: ActivityMyOrderDetailBinding? = null
    var o_id = 0

    var pref: SharedPrefHelper? = null
    var orderDetailList: ArrayList<OrderDetail> = arrayListOf()

    var invoiceURL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        pref = SharedPrefHelper.getInstance(MyApplication())
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding!!.headerlay.title.text = "My Order"
        binding!!.headerlay.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        o_id = intent.getIntExtra("o_id", 0)
        Log.e("llllllllll", "onCreate: " + o_id)
        getOrderDetails(o_id)

        binding!!.llInvoiceDownload.setOnClickListener {
            if (invoiceURL.isNotEmpty())
                downloadInvoice(invoiceURL)
            else
                Toast.makeText(
                    this@MyOrderDetailActivity,
                    "Invoice is not exist, Try Again Later",
                    Toast.LENGTH_SHORT
                ).show()
        }

        binding!!.tvReOrder.setOnClickListener {
            reOrder()

        }

        binding!!.tvReturn.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@MyOrderDetailActivity, ReturnOrderActivity::class.java
                ).putExtra("o_id", o_id), 203
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 203) {
            Log.e("returnnnned", "onActivityResult: order returned Details")
            finish()
        }
    }

    private fun downloadInvoice(urlString:String) {
        try {
            // Get the Downloads directory
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)



            val url = URL(urlString)
            val connection = url.openConnection()
            val inputStream: InputStream = connection.getInputStream()

            // Extract the file name from the URL
            val urlPath = Paths.get(url.path)
            val fileName = urlPath.fileName.toString()

            val destinationPath = Paths.get(downloadsDir.toString(), fileName).toString()

            val outputStream = FileOutputStream(destinationPath)

            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.close()
            inputStream.close()

            println("============File downloaded successfully to $destinationPath")
        } catch (e: Exception) {
            e.printStackTrace()
            println("============Error downloading file: ${e.message}")
        }
    }

    private fun reOrder() {
        if (orderDetailList.size > 0) {
            for ((index, item) in orderDetailList.withIndex()) {

                val isLastIteration = index == orderDetailList.size - 1
                item.productDetailId?.let {
                    item.qty?.let { it1 ->
                        addToCart(
                            it, it1, isLastIteration
                        )
                    }
                }
            }
        }
    }

    var isOrderDelivered: Boolean = false

    private fun addToCart(productDetailId: Int, count: Int, isLastIteration: Boolean) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", productDetailId)
        jsonObject.addProperty("qty", count)
        jsonObject.addProperty("sample", 0)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@MyOrderDetailActivity)
        var call: Call<AddCartResponse> = apiInterface.addIntoCart(jsonObject)!!

        call.enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>, response: Response<AddCartResponse>
            ) {
                if (isLastIteration) {

                    if (response.body()?.statuscode == 11) {
                        Toast.makeText(
                            this@MyOrderDetailActivity, "Item Added to Cart", Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@MyOrderDetailActivity, CartActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@MyOrderDetailActivity, "Something went wrong", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                if (isLastIteration) {
                    Toast.makeText(
                        this@MyOrderDetailActivity, "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

        })

    }

    fun getOrderDetails(o_id: Int) {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("o_id", o_id)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@MyOrderDetailActivity)
        var call: Call<ResponseSingleOrderData> = apiInterface.getSingleOrderDetails(jsonObject)!!

        call.enqueue(object : Callback<ResponseSingleOrderData> {
            override fun onResponse(
                call: Call<ResponseSingleOrderData>, response: Response<ResponseSingleOrderData>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {
                        binding!!.tvOrderNo.text = "Order No #${it.orderNumber}"
                        binding!!.tvOrderDate.text = "Placed On #${it.date}"

                        if (it.paymentMethod == 1) {
                            binding!!.tvPaymentStatus.text = "Payment receipt"
                        } else if (it.paymentMethod == 2) {
                            binding!!.tvPaymentStatus.text = "Cash on delivery"
                        } else {
                            binding!!.tvPaymentStatus.text = "Razor Pay"
                        }


                        val allItemsHaveOdstatus4 = (0 until it.orderDetail.size).all { index ->
                            it.orderDetail[index].odstatus == 4
                        }

                        if (allItemsHaveOdstatus4) {
                            binding!!.tvReOrder.visibility = VISIBLE
                            binding!!.tvReturn.visibility = VISIBLE
                        } else {
                            binding!!.tvReOrder.visibility = GONE
                            binding!!.tvReturn.visibility = GONE
                        }

                        orderDetailList = it.orderDetail

                        binding!!.tvName.text = it.address?.fullname
                        binding!!.tvAddress.text = it.address?.address
                        binding!!.tvPhone.text = "Phone no: ${it.address?.aMobile}"


                        invoiceURL = it.invoiceUrl.toString()

                        val adapter = ItemOrderDetailsListAdapter(
                            this@MyOrderDetailActivity, it.orderDetail, this@MyOrderDetailActivity
                        )
                        val layoutManager = LinearLayoutManager(this@MyOrderDetailActivity)
                        binding?.rvOrderDetails?.layoutManager = layoutManager
                        binding?.rvOrderDetails?.adapter = adapter


                    }
                } else {
                    Toast.makeText(
                        this@MyOrderDetailActivity, "" + response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseSingleOrderData>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@MyOrderDetailActivity, "" + t.message, Toast.LENGTH_LONG
                ).show()
            }

        })

    }


    override fun onClick(orderData: OrderDetail) {
        startActivity(
            Intent(this@MyOrderDetailActivity, OrderTrackingActivity::class.java).putExtra(
                "o_d_id", orderData.oDId
            ).putExtra(
                "o_id", o_id
            ).putExtra("product_detail_id", orderData.productDetailId).putExtra("p_id",orderData.productDetail?.pId)
        )
    }
}