package matemart.material.Interior.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.model.Architect
import matemart.material.Interior.model.ResGetSingleArchitect
import matemart.material.Interior.utils.Service
import matemart.material.Interior.R
import matemart.material.Interior.databinding.ActivityArchitecturalProffessionalDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchitecturalProfessionalDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityArchitecturalProffessionalDetailsBinding

    var instagramURL =""
    var facebookURL =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitecturalProffessionalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.ivBack.setOnClickListener() {
            finish()
        }

        var architect: Architect = intent.getSerializableExtra("data") as Architect

        binding.header.title.text = "Architecture Professional"

        architect.pro_id?.let { getSingleArchitectDetails(it) }

        binding.btnViewDetails.setOnClickListener() {
          startActivity(Intent(this@ArchitecturalProfessionalDetailsActivity,ContactForm::class.java).putExtra("pro_id",architect.pro_id)
              .putExtra("type","Architect"))

        }

        binding.imgFacebook.setOnClickListener {

            if(facebookURL.isNotEmpty()) {
                startActivity(
                    Intent(
                        this@ArchitecturalProfessionalDetailsActivity,
                        WebViewActivity::class.java
                    ).putExtra("url", facebookURL)
                )
            }
        }

        binding.imgInstagram.setOnClickListener {
            if(instagramURL.isNotEmpty()){
            startActivity(
                Intent(
                    this@ArchitecturalProfessionalDetailsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", instagramURL))
            }
        }

    }

    private fun getSingleArchitectDetails(pro_id:Int) {

        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("pro_id", pro_id)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetSingleArchitect> = apiInterface.getSingleArchitect(jsonObject)!!

        call.enqueue(object : Callback<ResGetSingleArchitect> {
            override fun onResponse(
                call: Call<ResGetSingleArchitect>,
                response: Response<ResGetSingleArchitect>
            ) {
                if (response.isSuccessful) {

                    facebookURL = response.body()?.data?.facebook.toString()
                    instagramURL = response.body()?.data?.instagram.toString()
                    binding.tvName.text = response.body()?.data?.name
                    binding.tvFirmName.text = response.body()?.data?.firm_name
                    binding.tvAbout.text = response.body()?.data?.about
                    Glide.with(this@ArchitecturalProfessionalDetailsActivity).load(response.body()?.data?.profile_image).placeholder(
                        R.drawable.place_holder_image).into(binding.image)

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



}