package matemart.material.interior.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import matemart.material.interior.databinding.ActivityPostYourRequirementsBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.CommonResponse
import matemart.material.interior.model.ImageUploadResponseModel
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostYourRequirements : AppCompatActivity() {


    private var binding: ActivityPostYourRequirementsBinding? = null
    lateinit var pref: SharedPrefHelper
     var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostYourRequirementsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

        binding!!.llHeader.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.llHeader.title.text = "Post your Requirements"

        binding!!.etName.setText(pref.read(SharedPrefHelper.USER_NAME, "").toString())
        binding!!.etMobile.setText(pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString())

        binding!!.ivSelect.setOnClickListener {
            ImagePicker.with(this@PostYourRequirements)
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


        binding!!.btnSubmit.setOnClickListener {
            if(binding!!.etDescription.text.toString().isNotEmpty() && imageUri != null) {
                imageUri?.let {
                    postYourRequirements(it
                        ,binding!!.etDescription.text.toString(),
                        binding!!.etEmail.text.toString()
                    )
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
           imageUri = data!!.data

            binding!!.tvSelectionText.text = File(imageUri?.path).name.toString()
        }
    }


    fun postYourRequirements(imageUri: Uri, requirement: String, email: String) {
        val imageFile = File(imageUri.path)
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())


        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "name", pref.read(SharedPrefHelper.USER_NAME, "").toString()
        )
        if(email.isNotEmpty()) {
            jsonObject.addProperty("email_id", email)
        }
        jsonObject.addProperty(
            "mobile_no",
            pref.read(SharedPrefHelper.KEY_LOGIN_NUMBER, "").toString()
        )
        jsonObject.addProperty("requirement", requirement)


        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var image: MultipartBody.Part =
            MultipartBody.Part.createFormData("requirement_image", imageFile.name, requestFile)
        var call: Call<CommonResponse> = apiInterface.postYourRequirements(image, jsonObject)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
//                    finish with Result
                    finish()

                } else {
                    Toast.makeText(
                        this@PostYourRequirements,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(
                    this@PostYourRequirements,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }
}