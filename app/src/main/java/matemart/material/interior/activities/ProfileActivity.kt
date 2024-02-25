package matemart.material.interior.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.makeramen.roundedimageview.RoundedImageView
import matemart.material.interior.databinding.ActivityProfileBinding
import matemart.material.interior.fragments.ChoosePictureBottomSheetFragment
import matemart.material.interior.fragments.VerifyOtpBottomSheet
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.ResGetProfileDetails
import matemart.material.interior.model.ResSendOtp
import matemart.material.interior.model.ResUploadProfileImage
import matemart.material.interior.model.UserProfile
import matemart.material.interior.utils.Service
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * ProfileActivity.class is Activity for ProfileActivity Check and Update.
 * Developed By Kisandas
 * last updated 27/09/2022 for GreetBuzz Ltd.
 */
class ProfileActivity : AppCompatActivity() {
    var iv_profile_pic: CircleImageView? = null
    var iv_editProfile: ImageView? = null
    var newImagePath = ""
    var tv_name: TextView? = null
    var tv_social_text: MultiAutoCompleteTextView? = null
    var et_name: EditText? = null
    var tv_category: TextInputEditText? = null
    var iv_countryFlag: RoundedImageView? = null
    var tv_select_country: TextInputEditText? = null
    var tv_select_state: TextInputEditText? = null
    var selected_category: String? = null
    var et_number: MultiAutoCompleteTextView? = null
    var et_email: MultiAutoCompleteTextView? = null
    var et_address: MultiAutoCompleteTextView? = null
    var tv_select_language: TextInputEditText? = null
    var et_website: MultiAutoCompleteTextView? = null
    var rc_socialMedia: RecyclerView? = null
    var btn_save_profile: TextView? = null
    var tv_version: TextView? = null
    var cdd: ChoosePictureBottomSheetFragment? = null
    var isProfileDPUpdate = false
    var isDataUpdate = false
    var til_number: TextInputLayout? = null
    var til_email: TextInputLayout? = null
    var til_website: TextInputLayout? = null
    var til_social: TextInputLayout? = null


    lateinit var binding: ActivityProfileBinding

    private var change: String = "Change"
    private var sendtOtp: String = "send OTP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initializeView();
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        binding.header.title.text = "Profile"

        binding.btnReset.setOnClickListener { setDefaultData(userProfile) }
        binding.sendOtp.text = "Change"
        binding.edtPhone.isEnabled = false
        getUserProfile()

        binding.btnUpdate.setOnClickListener { updateProfile() }

        binding.ivEditProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                ImagePicker.with(this@ProfileActivity)
                    .crop(1f, 1f) //Crop image(Optional), Check Customization for more option
                    .compress(1024) //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
        })


        binding.header.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.sendOtp.setOnClickListener {
            if (binding.sendOtp.text.equals(change)) {
                binding.edtPhone.isEnabled = true
                binding.sendOtp.text = sendtOtp
            } else {
                if (binding.edtPhone.text.length != 10) {
                    binding.edtPhone.error = "Please enter valid number"
                    return@setOnClickListener
                }
                changeNumber()
            }
        }
    }



    private fun changeNumber() {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "mo_no",
            binding.tvCountryCode.text.toString()+binding.edtPhone.text.toString()
        )
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResSendOtp>? = apiInterface.changeNumber(jsonObject)
        call!!.enqueue(object : Callback<ResSendOtp> {
            override fun onResponse(call: Call<ResSendOtp>, response: Response<ResSendOtp>) {

                if (response.isSuccessful) {

                    var verifyOtpBottomSheet: VerifyOtpBottomSheet? =
                        response.body()!!.data!!.token?.let {
                            VerifyOtpBottomSheet("mobile","",
                                binding.tvCountryCode.text.toString() + binding.edtPhone.text.toString(),
                                it, object : VerifyOtpBottomSheet.Update {
                                    override fun onUpdate(type:String) {
                                        getUserProfile()
                                        binding.edtPhone.isEnabled = false
                                        binding.sendOtp.text = change
                                    }

                                })
                        }!!
                    if (verifyOtpBottomSheet != null) {
                        verifyOtpBottomSheet.show(supportFragmentManager, "verifyOtp")
                    }

                } else if ( matemart.material.interior.api.Constants.getErrorMessage(
                        this@ProfileActivity,
                        response.errorBody()
                    ).equals("The mobile number is linked to another account.")
                ) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "The mobile number is linked to another account.",
                        Toast.LENGTH_LONG
                    )
                        .show()



                } else {

                    Toast.makeText(
                        this@ProfileActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ResSendOtp>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }


    private fun SendOtp() {

        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty(
            "mo_no",
            binding.tvCountryCode.text.toString() + binding.edtPhone.text.toString()
        )
        jsonObject.addProperty("uname", userProfile.uname)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResSendOtp>? = apiInterface.changeNumber(jsonObject)
        call!!.enqueue(object : Callback<ResSendOtp> {
            override fun onResponse(call: Call<ResSendOtp>, response: Response<ResSendOtp>) {

                if (response.isSuccessful) {
                    var verifyOtpBottomSheet: VerifyOtpBottomSheet? =
                        response.body()!!.data!!.token?.let {
                            VerifyOtpBottomSheet("mobile","",
                                binding.tvCountryCode.text.toString() + binding.edtPhone.text.toString(),
                                it, object : VerifyOtpBottomSheet.Update {
                                    override fun onUpdate(type:String) {
                                        getUserProfile()
                                        binding.edtPhone.isEnabled = false
                                        binding.sendOtp.text = change
                                    }

                                })
                        }!!
                    if (verifyOtpBottomSheet != null) {
                        verifyOtpBottomSheet.show(supportFragmentManager, "verifyOtp")
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ResSendOtp>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }


    var imageUri: Uri? = null

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val file: Uri? = data!!.data
            Glide.with(this@ProfileActivity)
                .load(file).into(binding.ivProfilePic)
            imageUri = file
            imageUri?.let { uploadProfileImage(it) }
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        val imageFile = File(imageUri.path)
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var image: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", imageFile.name, requestFile)
        var call: Call<ResUploadProfileImage> = apiInterface.updateProfileImage(image)!!
        call.enqueue(object : Callback<ResUploadProfileImage> {
            override fun onResponse(
                call: Call<ResUploadProfileImage>,
                response: Response<ResUploadProfileImage>
            ) {
                if (response.isSuccessful) {
                    userProfile.profile_image = response.body()?.data?.profile_image
                    setDefaultData(userProfile)
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResUploadProfileImage>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }


//    private void initializeView() {
//        pref = new SharedPreference(this);
//
//
//        iv_profile_pic = findViewById(R.id.iv_profile_pic);
//        iv_editProfile = findViewById(R.id.iv_editProfile);
//        tv_name = findViewById(R.id.tv_name);
//
//        et_name = findViewById(R.id.et_name);
//        tv_category = findViewById(R.id.tv_category);
//        iv_countryFlag = findViewById(R.id.iv_countryFlag);
//        tv_select_country = findViewById(R.id.tv_select_country);
//        tv_select_state = findViewById(R.id.tv_select_state);
//
//        rc_socialMedia = findViewById(R.id.rc_socialMedia);
//
//        btn_save_profile = findViewById(R.id.btn_save_profile);
//        tv_version = findViewById(R.id.tv_version);
//
//        tv_select_language = findViewById(R.id.tv_select_language);
//
//        til_number = findViewById(R.id.til_number);
//        til_email = findViewById(R.id.til_email);
//        til_website = findViewById(R.id.til_website);
//        til_social = findViewById(R.id.til_social);
//
//        et_number = (MultiAutoCompleteTextView) findViewById(R.id.et_number);
//        et_email = (MultiAutoCompleteTextView) findViewById(R.id.et_email);
//        et_address = (MultiAutoCompleteTextView) findViewById(R.id.et_address);
//        et_website = (MultiAutoCompleteTextView) findViewById(R.id.et_website);
//        tv_social_text = (MultiAutoCompleteTextView) findViewById(R.id.tv_social_text);
//
//
//
//
//        ColorDrawable cd = new ColorDrawable(Color.parseColor("#F3F3F3"));
//        tv_social_text.setDropDownBackgroundDrawable(cd);
//        et_email.setDropDownBackgroundDrawable(cd);
//        et_number.setDropDownBackgroundDrawable(cd);
//        et_website.setDropDownBackgroundDrawable(cd);
//        et_address.setDropDownBackgroundDrawable(cd);
//
//
//    }


    fun updateProfile() {
        var jsonObject:JSONObject = JSONObject()
        jsonObject.put(
            "uname",
            binding.edtFirstName.text.toString() + " " + binding.edtLastName.text.toString()
        )
        jsonObject.put("email", binding.edtEmail.text.toString())

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonObject.toString().toRequestBody(mediaType)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetProfileDetails> = apiInterface.updateUserProfile(requestBody)!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>,
                response: Response<ResGetProfileDetails>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { setDefaultData(it) }
                    Toast.makeText(
                        this@ProfileActivity,
                        "Data Updated SuccessFully",
                        Toast.LENGTH_LONG
                    ).show()


                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }


    fun getUserProfile() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetProfileDetails> = apiInterface.getUserProfile()!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>,
                response: Response<ResGetProfileDetails>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { setDefaultData(it) }


                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


    lateinit var userProfile: UserProfile
    fun setDefaultData(userProfile: UserProfile) {
        this.userProfile = userProfile

        imageUri = null
        var split: List<String> = userProfile.uname?.split(" ")!!
        if (split.isNotEmpty()) {
            binding.edtFirstName.setText(split.get(0))
            var lastname: String = ""
            for ((index, string) in split.withIndex()) {
                if (index != 0) {
                    lastname = lastname + string + " "
                }
            }
            binding.edtLastName.setText(lastname)
            var mobileNo : String
            if(userProfile.mo_no?.startsWith("+91") == true){
                mobileNo  = userProfile.mo_no?.removePrefix("+91").toString()
            }else{
                mobileNo  = userProfile.mo_no.toString()
            }

            val phoneNumberFilter =
                InputFilter { source, start, end, dest, dstart, dend -> // Removing any non-digit characters
                    val filteredStringBuilder = StringBuilder()
                    for (i in start until end) {
                        val character = source[i]
                        if (Character.isDigit(character)) {
                            filteredStringBuilder.append(character)
                        }
                    }

                    // Format the number as "00000 00000"
                    if (filteredStringBuilder.length >= 5) {
                        // Inserting a space after the 5th digit
                        filteredStringBuilder.insert(5, " ")
                    }

                    // Limiting the input to 10 digits
                    if (filteredStringBuilder.length > 11) {
                        filteredStringBuilder.delete(11, filteredStringBuilder.length)
                    }
                    filteredStringBuilder.toString()
                }

            binding.edtPhone.filters = arrayOf(phoneNumberFilter)

            binding.edtPhone.setText(mobileNo)
            binding.edtEmail.setText(userProfile.email)
            Glide.with(this@ProfileActivity)
                .load(userProfile.profile_image).into(binding.ivProfilePic)
            binding.tvName.text = userProfile.uname
        }
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

}