package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.databinding.ActivityArchitecturalProffessionalDetailsBinding
import com.matemart.databinding.ActivityContactFormBinding
import com.matemart.fragments.ArchitectContactDetailBottomSheet
import com.matemart.fragments.CitySelectionBottomSheetFragment
import com.matemart.fragments.onDismissListener
import com.matemart.interfaces.ApiInterface
import com.matemart.model.ResGetArchitectContact
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactForm : AppCompatActivity() , onDismissListener {

    lateinit var binding: ActivityContactFormBinding
    lateinit var pref: SharedPrefHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactFormBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
        var Type = intent.getStringExtra("type")
        var pro_id = intent.getIntExtra("pro_id", 0)

        binding.headerLay.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.headerLay.title.text = "Your Details"


        binding.etName.setText(pref.read(SharedPrefHelper.USER_NAME, "").toString())
        binding.etMobile.setText(pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString())

        binding.btnSubmit.setOnClickListener {
            if (binding.etDescription.text.toString().isNotEmpty()) {
                if (Type == "Architect") {
                    getArchitectDetails(pro_id)
                }else if(Type =="labour"){
                    getLabourDetails(pro_id)
                }


            }
        }

    }

    private fun getArchitectDetails(pro_id: Int) {

        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("pro_id", pro_id)
        jsonObject.addProperty("name", pref.read(SharedPrefHelper.USER_NAME, "").toString())

        jsonObject.addProperty(
            "mobile_no",
            pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString()
        )
        jsonObject.addProperty("occupation", binding.etOccupation.text.toString())
        jsonObject.addProperty("purpose", binding.etDescription.text.toString())
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetArchitectContact> = apiInterface.getArchitectDetails(jsonObject)!!

        call.enqueue(object : Callback<ResGetArchitectContact> {
            override fun onResponse(
                call: Call<ResGetArchitectContact>,
                response: Response<ResGetArchitectContact>
            ) {
                if (response.isSuccessful) {

                    if (response.body()?.data != null) {
                        var cddCity = ArchitectContactDetailBottomSheet(
                            "Architect", response.body()?.data!!,this@ContactForm
                        )
                        cddCity.show(supportFragmentManager, "TAG2")
                    }
                } else {
                    Toast.makeText(
                        this@ContactForm,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetArchitectContact>, t: Throwable) {
                Toast.makeText(
                    this@ContactForm,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


    private fun getLabourDetails(pro_id: Int) {

        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("id", pro_id)
        jsonObject.addProperty("name", pref.read(SharedPrefHelper.USER_NAME, "").toString())

        jsonObject.addProperty(
            "mobile_no",
            pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString()
        )
        jsonObject.addProperty("occupation", binding.etOccupation.text.toString())
        jsonObject.addProperty("purpose", binding.etDescription.text.toString())
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetArchitectContact> = apiInterface.getLabourDetails(jsonObject)!!

        call.enqueue(object : Callback<ResGetArchitectContact> {
            override fun onResponse(
                call: Call<ResGetArchitectContact>,
                response: Response<ResGetArchitectContact>
            ) {
                if (response.isSuccessful) {

                    if (response.body()?.data != null) {
                        var cddCity = ArchitectContactDetailBottomSheet(
                            "Type", response.body()?.data!!,this@ContactForm
                        )
                        cddCity.show(supportFragmentManager, "TAG2")
                    }
                } else {
                    Toast.makeText(
                        this@ContactForm,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetArchitectContact>, t: Throwable) {
                Toast.makeText(
                    this@ContactForm,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


    override fun onDismissed() {
      finish()
    }

}