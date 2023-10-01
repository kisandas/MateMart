package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.TrackingItemAdapter
import com.matemart.databinding.ActivityOrderTrackingBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.CommonResponse
import com.matemart.model.OrderStatus
import com.matemart.model.ResponseOrderTrackingModel
import com.matemart.utils.Service
import com.matemart.utils.Utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderTrackingActivity : AppCompatActivity() {

    private var binding: ActivityOrderTrackingBinding? = null
    var product_detail_id = 0
    var o_d_id = 0
    var o_id = 0

    var orderStatusList: ArrayList<OrderStatusModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        o_d_id = intent.getIntExtra("o_d_id", 0)
        o_id = intent.getIntExtra("o_id", 0)
        product_detail_id = intent.getIntExtra("product_detail_id", 0)



        getOrderTracking(o_d_id, product_detail_id)

        binding!!.tvCancel.setOnClickListener {
            cancelOrder(o_d_id)
        }

        binding!!.tvReturn.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@OrderTrackingActivity,
                    ReturnOrderActivity::class.java
                ).putExtra("o_id", o_id).putExtra("o_d_id", o_d_id), 203
            )

        }

        binding!!.include.tvAddReview.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 203) {
            Log.e("returnnnned", "onActivityResult: order returned TrackingActivity")
            finish()
        }
    }

    fun getOrderTracking(o_d_id: Int, product_detail_id: Int) {

        var jsonObject = JsonObject()
        jsonObject.addProperty("o_d_id", o_d_id)
        jsonObject.addProperty("product_detail_id", product_detail_id)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@OrderTrackingActivity)
        var call: Call<ResponseOrderTrackingModel> = apiInterface.getOrderTracking(jsonObject)!!

        call.enqueue(object : Callback<ResponseOrderTrackingModel> {
            override fun onResponse(
                call: Call<ResponseOrderTrackingModel>,
                response: Response<ResponseOrderTrackingModel>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {


                        binding!!.tvOrderNo.text = "Order No #" + it.orderInfo?.orderNumber

                        addDataIntoOrderStatusList(it.orderStatus)
                        for (item in it.orderStatus) {
                            val data = orderStatusList.find { it -> it.status == item.status }

                            if (data != null) {
                                val index = orderStatusList.indexOf(data)
                                orderStatusList[index].date = item.date.toString()
                                orderStatusList[index].isEnabled = true
                            }

                        }

                        val odStatus = it.orderStatus.last().status
                        Log.e("mmmmmmmm", "onResponse: " + odStatus)

                        when (odStatus) {
                            0 -> {
                                binding!!.tvCancel.visibility = VISIBLE
                                binding!!.tvReturn.visibility = GONE
                            }
                            1 -> {
                                binding!!.tvCancel.visibility = VISIBLE
                                binding!!.tvReturn.visibility = GONE
                            }
                            2 -> {
                                binding!!.tvCancel.visibility = VISIBLE
                                binding!!.tvReturn.visibility = GONE
                            }
                            3 -> {
                                binding!!.tvCancel.visibility = VISIBLE
                                binding!!.tvReturn.visibility = GONE
                            }
                            4 -> {
                                binding!!.tvCancel.visibility = GONE
                                binding!!.tvReturn.visibility = VISIBLE
                                binding!!.include.tvAddReview.visibility = VISIBLE
                            }
                            5 -> {
                                binding!!.tvCancel.visibility = GONE
                                binding!!.tvReturn.visibility = GONE
                            }
                            6 -> {
                                binding!!.tvCancel.visibility = GONE
                                binding!!.tvReturn.visibility = GONE
                            }
                            -1 -> {
                                binding!!.tvCancel.visibility = GONE
                                binding!!.tvReturn.visibility = GONE
                            }
                        }


                        val adapter = TrackingItemAdapter(
                            this@OrderTrackingActivity,
                            orderStatusList
                        )
                        val layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
                        binding?.rvTrackingList?.layoutManager = layoutManager

                        binding?.rvTrackingList?.adapter = adapter

                        Log.e("llllll", "onResponse: " + it.toString())
                    }
                } else {
                    Toast.makeText(
                        this@OrderTrackingActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseOrderTrackingModel>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@OrderTrackingActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    data class OrderStatusModel(
        var status: Int,
        var title: String,
        var subTitle: String,
        var date: String,
        var isEnabled: Boolean
    )


    private fun addDataIntoOrderStatusList(orderStatus: ArrayList<OrderStatus>) {

        var Placed = false
        var Processing = false
        var OrderCanCancel = false
        var Confirmed = false
        var OutForDelivery = false
        var Delivered = false
        var ReturnPending = false
        var Returned = false
        var Cancelled = false

        for (item in orderStatus) {
            when (item.status) {
                0 -> {
                    Placed = true
                    OrderCanCancel = true
                }
                1 -> {
                    Processing = true
                    OrderCanCancel = true
                }

                2 -> {
                    Confirmed = true
                    OrderCanCancel = false
                }

                3 -> {
                    OutForDelivery = true
                }

                4 -> {
                    Delivered = true
                }

                5 -> {
                    ReturnPending = true
                }

                6 -> {
                    Returned = true
                }
                -1 -> {
                    Cancelled = true
                }
            }
        }

        orderStatusList.add(
            OrderStatusModel(
                0,
                ORDER_STATUS_PLACED,
                ORDER_STATUS_PLACED_TEXT,
                "",
                false
            )
        )

        if (Processing) {
            orderStatusList.add(
                OrderStatusModel(
                    1,
                    ORDER_STATUS_PROCESSED,
                    ORDER_STATUS_PROCESSED_TEXT,
                    "",
                    false
                )
            )
        }


        if (!Cancelled) {

            orderStatusList.add(
                OrderStatusModel(
                    2,
                    ORDER_STATUS_CONFIRMED,
                    ORDER_STATUS_CONFIRMED_TEXT,
                    "",
                    false
                )
            )
            orderStatusList.add(
                OrderStatusModel(
                    3,
                    ORDER_STATUS_OUT_FOR_DELIVERY,
                    ORDER_STATUS_OUT_FOR_DELIVERY_1_TEXT,
                    "",
                    false
                )
            )
            orderStatusList.add(
                OrderStatusModel(
                    4,
                    ORDER_STATUS_DELIVERED,
                    ORDER_STATUS_DELIVERED_TEXT,
                    "",
                    false
                )
            )
            if (ReturnPending || Returned) {
                orderStatusList.add(
                    OrderStatusModel(
                        5,
                        ORDER_STATUS_RETURN,
                        ORDER_STATUS_RETURN_TEXT,
                        "",
                        false
                    )
                )

                orderStatusList.add(
                    OrderStatusModel(
                        6,
                        ORDER_STATUS_RETURNED,
                        ORDER_STATUS_RETURNED_TEXT,
                        "",
                        false
                    )
                )

            }
        }


        if (Cancelled) {
            orderStatusList.add(
                OrderStatusModel(
                    -1,
                    ORDER_STATUS_CANCELLED,
                    ORDER_STATUS_CANCELLED_TEXT,
                    "",
                    false
                )
            )
        }
    }

    fun cancelOrder(o_d_id: Int) {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("o_d_id", o_d_id)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@OrderTrackingActivity)
        var call: Call<CommonResponse> = apiInterface.cancelOrder(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@OrderTrackingActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@OrderTrackingActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@OrderTrackingActivity,
                    "" + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }


}

