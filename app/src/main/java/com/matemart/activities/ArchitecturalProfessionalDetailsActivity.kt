package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.matemart.databinding.ActivityArchitecturalProffessionalDetailsBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.Architect
import com.matemart.model.ResGetArchitectDetails
import com.matemart.model.ResGetSingleArchitect
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchitecturalProfessionalDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityArchitecturalProffessionalDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitecturalProffessionalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include5.ivBack.setOnClickListener() {
            finish()
        }

        binding.include5.title.text = "Architecture Professional"

        getSingleArchitectDetails()
        getArchitectDetails()

        /* *********************

        BUTTON CLICK


        ***********************/

        /*   binding.viewDetails.setOnClickListener(){
               getArchitectDetails()
           }       */

    }

    private fun getSingleArchitectDetails() {
        var architect: Architect = intent.getSerializableExtra("data") as Architect
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("pro_id", architect.pro_id)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetSingleArchitect> = apiInterface.getSingleArchitect(jsonObject)!!

        call.enqueue(object : Callback<ResGetSingleArchitect> {
            override fun onResponse(
                call: Call<ResGetSingleArchitect>,
                response: Response<ResGetSingleArchitect>
            ) {
                if (response.isSuccessful) {

                } else {
                    Toast.makeText(
                        this@ArchitecturalProfessionalDetailsActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetSingleArchitect>, t: Throwable) {
                Toast.makeText(
                    this@ArchitecturalProfessionalDetailsActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

    private fun getArchitectDetails() {
        var architect: Architect = intent.getSerializableExtra("data") as Architect
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("pro_id", architect.pro_id)
        jsonObject.addProperty("name", architect.name)

        /*       *****************



        CHANGE MOBILE NUMBER



        **********************/


        jsonObject.addProperty("mobile_no", 1234567890)
        jsonObject.addProperty("occupation", "etru76iuy")
        jsonObject.addProperty("purpose", "ewtrty68678")
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetArchitectDetails> = apiInterface.getArchitectDetails(jsonObject)!!

        call.enqueue(object : Callback<ResGetArchitectDetails> {
            override fun onResponse(
                call: Call<ResGetArchitectDetails>,
                response: Response<ResGetArchitectDetails>
            ) {
                if (response.isSuccessful) {

                } else {
                    Toast.makeText(
                        this@ArchitecturalProfessionalDetailsActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetArchitectDetails>, t: Throwable) {
                Toast.makeText(
                    this@ArchitecturalProfessionalDetailsActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


}