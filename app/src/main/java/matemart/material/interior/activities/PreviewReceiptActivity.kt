package matemart.material.interior.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import matemart.material.interior.databinding.ActivityPreviewReceiptBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.ImageUploadResponseModel
import matemart.material.interior.utils.Service
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PreviewReceiptActivity : AppCompatActivity() {

    private var binding: ActivityPreviewReceiptBinding? = null
    lateinit var imageUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewReceiptBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        imageUri = Uri.parse(intent.getStringExtra("imageUri").toString())

        Glide.with(this@PreviewReceiptActivity).load(imageUri).into(binding!!.ivImage)

        binding!!.include2.title.text ="Preview Receipt"
        binding!!.include2.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding!!.btnUpload.setOnClickListener {
            uploadReceipt(imageUri)
        }

        binding!!.btnRemove.setOnClickListener { finish() }
    }

    fun uploadReceipt(imageUri: Uri) {
        val imageFile = File(imageUri.path)
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var image: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", imageFile.name, requestFile)
        var call: Call<ImageUploadResponseModel> = apiInterface.uploadReceipt(image)!!
        call.enqueue(object : Callback<ImageUploadResponseModel> {
            override fun onResponse(
                call: Call<ImageUploadResponseModel>,
                response: Response<ImageUploadResponseModel>
            ) {
                if (response.isSuccessful) {
//                    finish with Result
//                    finish()

                } else {
                    Toast.makeText(
                        this@PreviewReceiptActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ImageUploadResponseModel>, t: Throwable) {
                Toast.makeText(
                    this@PreviewReceiptActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }
}