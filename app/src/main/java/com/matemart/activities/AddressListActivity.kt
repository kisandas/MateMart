package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.essam.simpleplacepicker.MapActivity
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.essam.simpleplacepicker.utils.SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE
import com.matemart.R
import com.matemart.interfaces.ApiInterface
import com.matemart.model.AddressListResponse
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : AppCompatActivity() {
    var btn_add_address: TextView? = null

    var selectedAddress = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        btn_add_address = findViewById(R.id.btn_add_address)
        btn_add_address?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AddressListActivity, MapActivity::class.java)
            val bundle = Bundle()
            bundle.putString(SimplePlacePicker.API_KEY, getString(R.string.places_api_key))
            //                bundle.putString(SimplePlacePicker.COUNTRY,"India");
//                bundle.putString(SimplePlacePicker.LANGUAGE,"en");
//                bundle.putStringArray(SimplePlacePicker.SUPPORTED_AREAS,supportedAreas);
            intent.putExtras(bundle)
            startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE)
        })

        getAddressList()
    }


    fun getAddressList() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AddressListActivity)
        var call: Call<AddressListResponse> = apiInterface.getAddressList()!!

        call.enqueue(object : Callback<AddressListResponse> {
            override fun onResponse(
                call: Call<AddressListResponse>,
                response: Response<AddressListResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {

                        if (it.isNullOrEmpty()) {

                        } else {


                            var cartList = ArrayList(it)

                            if (cartList.isNotEmpty()) {
                                val itemDefault = cartList.find { it.default == 1 }
                                itemDefault?.aId?.let { it1 ->
                                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                                        .write(SharedPrefHelper.ADDRESS_ID, it1)
                                }
                            }


//                            val adapter = CouponItemAdapter(
//                                cartList,
//                                this@AddressListActivity,
//                                this@AddressListActivity,
//                                selectedCouponList
//                            )
//                            val layoutManager = LinearLayoutManager(this@AddressListActivity)
//                            binding?.rcCouponActivity?.layoutManager = layoutManager
//                            binding?.rcCouponActivity?.setAdapter(adapter)


                        }

                    }
                } else {
                    Toast.makeText(
                        this@AddressListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddressListResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@AddressListActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === SELECT_LOCATION_REQUEST_CODE && resultCode === RESULT_OK && data != null) {
            // Retrieve the data from the Intent
            selectedAddress = data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS).toString()
            latitude = data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, 0.0)
            longitude = data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, 0.0)

            startActivity(Intent(this@AddressListActivity,SaveAddressActivity::class.java)
                .putExtra(SimplePlacePicker.SELECTED_ADDRESS,selectedAddress)
                .putExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,latitude)
                .putExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,longitude))

            // Now you can use the retrieved data as needed
            // For example, you can update UI elements with the selected address, latitude, and longitude.
        }
    }
}