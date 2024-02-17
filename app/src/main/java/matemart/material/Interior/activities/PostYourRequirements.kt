package matemart.material.Interior.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import matemart.material.Interior.databinding.ActivityPostYourRequirementsBinding
import matemart.material.Interior.utils.MyApplication
import matemart.material.Interior.utils.SharedPrefHelper

class PostYourRequirements : AppCompatActivity() {


    private var binding: ActivityPostYourRequirementsBinding? = null
    lateinit var pref: SharedPrefHelper

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



    }
}