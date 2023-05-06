package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.matemart.utils.Toast.Toaster
import com.matemart.databinding.ActivityOtpactivityBinding
import com.matemart.model.login.UserResponse
import com.matemart.utils.*
import com.matemart.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OTPActivity : BaseActivity() {

    var pref: SharedPrefHelper? = null
    private val viewModel: AuthViewModel by viewModels()


    private lateinit var binding: ActivityOtpactivityBinding
    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun getRootView(): View {
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()

        binding.otpView.setOnFinishListener { s: String? -> null }
        pref = SharedPrefHelper.getInstance(MyApplication())


        binding.btnVerify.setOnClickListener {
            viewModel.verify_otp(pref!!.read(SharedPrefHelper.KEY_LOGIN_TOKEN), "1234")
        }

        viewModel.userResponse.observe(this@OTPActivity) {
            saveUserInfo(it.data as UserResponse)
        }


    }

    fun saveUserInfo(response: UserResponse) {

        val state = response.data?.state
        val city = response.data?.city
        val pincode = response.data?.pincode
        val ccid = response.data?.ccid
        state?.let {
            if (!it.isNullOrEmpty()) {
                pref!!.write(SharedPrefHelper.KEY_STATE, it)
            }
        }

        city?.let {
            if (!it.isNullOrEmpty()) {
                pref!!.write(SharedPrefHelper.KEY_CITY, it)
            }
        }

        pincode?.let {
            if (!it.isNullOrEmpty()) {
                pref!!.write(SharedPrefHelper.KEY_PINCODE, it)
            }
        }


        ccid?.let {
            if (!it.isNullOrEmpty()) {
                pref!!.write(SharedPrefHelper.KEY_CCID, it)

                var accessToken =   pref!!.read(SharedPrefHelper.KEY_LOGIN_NUMBER) + ":" +it
                val encodedString: String = Base64.getEncoder().encodeToString(accessToken.toByteArray())

                pref!!.write(SharedPrefHelper.KEY_ACCESS_TOKEN,encodedString)
            }
        }

        Toaster.Builder(this@OTPActivity)
            .setTitle("Success")
            .setDescription("Login Success")
            .setDuration(5000)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
        startActivity(Intent(this@OTPActivity, LocationActivity::class.java))
        finish()
    }
}