package matemart.material.interior.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import matemart.material.interior.databinding.ActivityContactFormBinding
import matemart.material.interior.fragments.ArchitectContactDetailBottomSheet
import matemart.material.interior.fragments.onDismissListener
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.ResGetArchitectContact
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
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