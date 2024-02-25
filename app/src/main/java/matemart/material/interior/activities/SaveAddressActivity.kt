package matemart.material.interior.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import matemart.material.interior.R
import matemart.material.interior.databinding.ActivitySaveAddressBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.Address
import matemart.material.interior.model.CommonResponse
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaveAddressActivity : AppCompatActivity() {

    lateinit var binding: ActivitySaveAddressBinding;
    var selectedAddress = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var addressType = -1
    var pref: SharedPrefHelper? = null
    var defaultAddress = 1
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

        selectedAddress = intent.getStringExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.SELECTED_ADDRESS).toString()
        latitude = intent.getDoubleExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LAT_EXTRA, 0.0)
        longitude = intent.getDoubleExtra(matemart.essam.simpleplacepicker.utils.SimplePlacePicker.LOCATION_LNG_EXTRA, 0.0)
        isUpdate = intent.getBooleanExtra("IS_UPDATE", false)
        val address = intent.getSerializableExtra("address") as? Address


        if (isUpdate) {
            binding.etName.setText(address?.fullname)
            binding.etPhone.setText(address?.aMobile)
            binding.etLine1.setText(address?.flatNo)
            binding.etLine2.setText(address?.address)
            binding.etState.setText(address?.state)
            binding.etCity.setText(address?.city)
            binding.etPin.setText(address?.pincode)

            if (address?.default == 1) {
                binding.cbMarkAsDefault.isChecked = true
            }else{
                binding.cbMarkAsDefault.isChecked = false
            }

            if (address?.typeOfAddress == 0) {
                binding.rbHome.isChecked = true
            } else if (address?.typeOfAddress == 1) {
                binding.rbOffice.isChecked = true
            } else if (address?.typeOfAddress == 2) {
                binding.rbOther.isChecked = true
            }
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbHome -> {
                    addressType = 0
                    // Home RadioButton is selected
                }
                R.id.rbOffice -> {
                    addressType = 1
                    // Office RadioButton is selected
                }
                R.id.rbOther -> {
                    addressType = 2
                    // Other RadioButton is selected
                }
                else -> {
                    addressType = -1
                    // None of the RadioButtons is selected
                    // You can handle this case here
                }
            }
        }

        binding.cbMarkAsDefault.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                defaultAddress = 1
            } else {
                defaultAddress = 0
            }
        }

        binding.btnAddAddress.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter Name",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.etPhone.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter Phone No.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.etLine1.text.isEmpty() && binding.etLine2.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter Address.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.etState.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter State.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.etCity.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter City.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.etPin.text.isEmpty()) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Enter PIN Code.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (addressType == -1) {
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Please Select Address Type.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener

            }

            var jsonObject = JsonObject()
            jsonObject.addProperty("flat_no", binding.etLine1.text.toString().trim())
            jsonObject.addProperty("fullname", binding.etName.text.toString().trim())
            jsonObject.addProperty("a_email", pref?.read(SharedPrefHelper.EMAIL, "").toString())
            jsonObject.addProperty("a_mobile", binding.etPhone.text.toString().trim())
            jsonObject.addProperty("state", binding.etState.text.toString().trim())
            jsonObject.addProperty("country", "India")
            jsonObject.addProperty("city", binding.etCity.text.toString().trim())
            jsonObject.addProperty("pincode", binding.etPin.text.toString().trim())
            jsonObject.addProperty("address", binding.etLine2.text.toString().trim())
            jsonObject.addProperty("default", defaultAddress)
            jsonObject.addProperty("type_of_address", addressType)
            jsonObject.addProperty("lat", latitude)
            jsonObject.addProperty("long", longitude)

            if(isUpdate){
                jsonObject.addProperty("a_id",address?.aId)
                updateAddress(jsonObject)
            }else{
                createAddress(jsonObject)
            }


        }

    }


    fun createAddress(jsonObject: JsonObject) {


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SaveAddressActivity)
        var call: Call<CommonResponse> = apiInterface.createAddress(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    finish()
                }
                Toast.makeText(
                    this@SaveAddressActivity,
                    "" + response.body()?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    fun updateAddress(jsonObject: JsonObject) {


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SaveAddressActivity)
        var call: Call<CommonResponse> = apiInterface.updateAddress(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    val intent = Intent()
                    finish()
                }
                Toast.makeText(
                    this@SaveAddressActivity,
                    "" + response.body()?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@SaveAddressActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }
}