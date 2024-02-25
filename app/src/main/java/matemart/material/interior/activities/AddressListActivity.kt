package matemart.material.interior.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import matemart.essam.simpleplacepicker.utils.SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE
import com.google.gson.JsonObject
import matemart.material.interior.adapter.AddressItemAdapter
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.Address
import matemart.material.interior.model.AddressListResponse
import matemart.material.interior.model.CommonResponse
import matemart.material.interior.utils.Service
import matemart.material.interior.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : AppCompatActivity(), AddressItemAdapter.ItemClickListener {
    var btn_add_address: TextView? = null
    var rvAddressList: RecyclerView? = null
    var llEmptyLay: LinearLayout? = null

    var selectedAddress = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        btn_add_address = findViewById(R.id.btn_add_address)
        rvAddressList = findViewById(R.id.rvAddressList)
        llEmptyLay = findViewById(R.id.llEmptyLay)


        btn_add_address?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AddressListActivity, matemart.essam.simpleplacepicker.MapActivity::class.java)
            val bundle = Bundle()
            bundle.putString(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.API_KEY, getString(R.string.places_api_key))
            //                bundle.putString(SimplePlacePicker.COUNTRY,"India");
//                bundle.putString(SimplePlacePicker.LANGUAGE,"en");
//                bundle.putStringArray(SimplePlacePicker.SUPPORTED_AREAS,supportedAreas);
            intent.putExtras(bundle)
            startActivityForResult(intent, SELECT_LOCATION_REQUEST_CODE)
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

                        if (it.isNotEmpty()) {

                            val adapter = AddressItemAdapter(
                                this@AddressListActivity,
                                it,
                                this@AddressListActivity
                            )
                            val layoutManager = LinearLayoutManager(this@AddressListActivity)
                            rvAddressList?.layoutManager = layoutManager
                            rvAddressList?.adapter = adapter

                            rvAddressList?.visibility = VISIBLE
                            llEmptyLay?.visibility = GONE

                        } else {

                            rvAddressList?.visibility = GONE
                            llEmptyLay?.visibility = VISIBLE
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
            selectedAddress = data.getStringExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.SELECTED_ADDRESS).toString()
            latitude = data.getDoubleExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LAT_EXTRA, 0.0)
            longitude = data.getDoubleExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LNG_EXTRA, 0.0)

            startActivity(
                Intent(this@AddressListActivity, SaveAddressActivity::class.java)
                    .putExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.SELECTED_ADDRESS, selectedAddress)
                    .putExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LAT_EXTRA, latitude)
                    .putExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LNG_EXTRA, longitude)
            )

            // Now you can use the retrieved data as needed
            // For example, you can update UI elements with the selected address, latitude, and longitude.
        }else if(requestCode == 200){
            getAddressList()
        }
    }

    override fun onItemEdit(address: Address) {

        startActivityForResult(
            Intent(this@AddressListActivity, SaveAddressActivity::class.java)
                .putExtra("address", address)
                .putExtra("IS_UPDATE", true),200

        )

    }

    override fun onItemDelete(address: Address) {
        deleteAddress(address)
    }

    override fun onItemSetAsDefault(address: Address) {
        markAsDefaultAddress(address)
    }

    fun markAsDefaultAddress(address: Address) {

        var jsonObject = JsonObject()
        jsonObject.addProperty("a_id", address.aId)
        jsonObject.addProperty("default", 1)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AddressListActivity)
        var call: Call<CommonResponse> = apiInterface.markAddressDefault(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {

                    Toast.makeText(
                        this@AddressListActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    getAddressList()
                } else {
                    Toast.makeText(
                        this@AddressListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
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

    fun deleteAddress(address: Address) {

        var jsonObject = JsonObject()
        jsonObject.addProperty("a_id", address.aId)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@AddressListActivity)
        var call: Call<CommonResponse> = apiInterface.deleteAddress(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {

                    Toast.makeText(
                        this@AddressListActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    getAddressList()
                } else {
                    Toast.makeText(
                        this@AddressListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
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

}