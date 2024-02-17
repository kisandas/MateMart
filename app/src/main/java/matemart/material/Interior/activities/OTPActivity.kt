package matemart.material.Interior.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import matemart.material.Interior.R
import matemart.material.Interior.databinding.ActivityOtpactivityBinding

import dagger.hilt.android.AndroidEntryPoint
import matemart.material.Interior.model.login.LoginResponse
import matemart.material.Interior.model.login.UserResponse
import matemart.material.Interior.utils.BaseActivity
import matemart.material.Interior.utils.MyApplication
import matemart.material.Interior.utils.SharedPrefHelper
import matemart.material.Interior.viewmodel.AuthViewModel
import java.util.*

@AndroidEntryPoint
class OTPActivity : BaseActivity() {

    var pref: SharedPrefHelper? = null
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning = false
    private val initialTimeInMillis: Long = 2 * 60 * 1000
    var phoneNo = ""

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

    private fun setupTimer() {
        binding.tvResendOtp.setTextColor(getColor(R.color.dark_gray_4d4d4d))
        countDownTimer = object : CountDownTimer(initialTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                binding.tvTimer.text = String.format("%02d:%02d min", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00 min"
                isTimerRunning = false
                binding.tvResendOtp.setTextColor(getColor(R.color.theme_blue_38B449))
                // Add your logic here when the timer finishes
            }
        }

        startTimer()
    }

    private fun startTimer() {
        countDownTimer.start()
        isTimerRunning = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
        phoneNo = intent.getStringExtra("phoneNo").toString()

        binding.tvMobileText.text = "we have sent a verification code to your registered mobile no : ${phoneNo}."

        binding.otpView.setOnFinishListener { s: String? -> null }
        pref = SharedPrefHelper.getInstance(MyApplication())

        binding.tvResendOtp.setOnClickListener {
            if (!isTimerRunning) {
                viewModel.login(phoneNo)
                resetTimer()
            }
        }



        setupTimer()


        binding.btnVerify.setOnClickListener {
            if (binding.otpView.getStringFromFields().isNotEmpty()) {
                viewModel.verify_otp(
                    pref!!.read(SharedPrefHelper.KEY_LOGIN_TOKEN),
                    binding.otpView.getStringFromFields()
                )
            } else {
                matemart.material.Interior.utils.Toast.Toaster.Builder(this@OTPActivity)
                    .setTitle("ERROR")
                    .setDescription("OTP should not be empty...")
                    .setDuration(5000)
                    .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                    .show()
            }
        }

        viewModel.userResponse.observe(this@OTPActivity) {
            if (it.data?.statuscode == 11) {
                saveUserInfo(it.data as UserResponse)
            } else {
                matemart.material.Interior.utils.Toast.Toaster.Builder(this@OTPActivity)
                    .setTitle("ERROR")
                    .setDescription(it?.data?.message)
                    .setDuration(5000)
                    .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                    .show()
            }
        }

        viewModel.loginResponse.observe(this@OTPActivity) {
            Log.e("checkLogin-->", "checkValidation: " + it.data.toString())
//            successResponse(it.data as LoginResponse)
            if (it?.data?.statuscode == 11) {
                successResponse(it.data as LoginResponse)
            } else {
                matemart.material.Interior.utils.Toast.Toaster.Builder(this@OTPActivity)
                    .setTitle("ERROR")
                    .setDescription(it?.data?.message)
                    .setDuration(5000)
                    .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                    .show()
            }
        }


    }

    private fun resetTimer() {
        countDownTimer.cancel()
        binding.tvTimer.text = "02:00 min" // Reset the timer to 2 minutes
        isTimerRunning = false
        setupTimer()
    }


    private fun successResponse(response: LoginResponse) {

        val token = response.data?.token?.toString()

        token?.let {
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .write(SharedPrefHelper.KEY_LOGIN_TOKEN, it)
        }

        Log.e("checkToken", "onResponse: ${response.toString()}")

        response.data?.uId?.let {
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .write(SharedPrefHelper.USER_ID, it)
        }

        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .write(SharedPrefHelper.IS_USER_GUEST, false)

        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .write(
                SharedPrefHelper.KEY_LOGIN_NUMBER,
                phoneNo
            )

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
            Log.e("ccchhhhiiiddd", "saveUserInfo: " + it)
            if (!it.isNullOrEmpty()) {
                pref!!.write(SharedPrefHelper.KEY_CCID, it)

                var accessToken = pref!!.read(SharedPrefHelper.KEY_LOGIN_NUMBER) + ":" + it
                val encodedString: String =
                    Base64.getEncoder().encodeToString(accessToken.toByteArray())

                pref!!.write(SharedPrefHelper.KEY_ACCESS_TOKEN, encodedString)
            }
        }

        matemart.material.Interior.utils.Toast.Toaster.Builder(this@OTPActivity)
            .setTitle("Success")
            .setDescription("Login Success")
            .setDuration(5000)
            .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.SUCCESS)
            .show()
        startActivity(Intent(this@OTPActivity, LocationActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}