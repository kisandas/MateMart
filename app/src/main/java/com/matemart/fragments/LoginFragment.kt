package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.matemart.utils.Toast.Toaster
import com.matemart.activities.OTPActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.matemart.R
import com.matemart.activities.HomeActivity
import com.matemart.activities.LocationActivity
import com.matemart.databinding.FragmentLoginBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.GuestUserModel
import com.matemart.model.ResGetLabourFilter
import com.matemart.model.login.LoginData
import com.matemart.model.login.LoginResponse
import com.matemart.utils.BaseFragment
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import com.matemart.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class LoginFragment : BaseFragment() {


    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun initViewBinding(): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return initViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            checkValidation()
            //                startActivity(new Intent(getContext(), OTPActivity.class));
        })


        binding.tvGuestUser.setOnClickListener {
            getGuestUserLogin()
        }
    }

    fun getGuestUserLogin() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<GuestUserModel> = apiInterface.getGuestUser()!!

        call.enqueue(object : Callback<GuestUserModel> {
            override fun onResponse(
                call: Call<GuestUserModel>,
                response: Response<GuestUserModel>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.data?.apiToken

                    token?.let {
                        SharedPrefHelper.getInstance(MyApplication.getInstance())
                            .write(SharedPrefHelper.KEY_ACCESS_TOKEN, it)
                    }

                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                        .write(SharedPrefHelper.IS_USER_GUEST, true)

                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {

                }
            }

            override fun onFailure(call: Call<GuestUserModel>, t: Throwable) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

var phoneNo =""
    private fun checkValidation() {
        if (binding.etNumber.text.toString()
                .isEmpty() || binding.etNumber.text.toString().length != 10
        ) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Number")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }

        viewModel.login(binding.tvCountryCode.text.toString() + binding.etNumber.text.toString())
        phoneNo = binding.tvCountryCode.text.toString() + binding.etNumber.text.toString()
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            Log.e("checkLogin-->", "checkValidation: " + it.data.toString())
            successResponse(it.data as LoginResponse)
        }

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
                binding.tvCountryCode.text.toString() + binding.etNumber.text.toString()
            )

        Log.e("checkToken", "onResponse: $token")
        Toaster.Builder(requireActivity())
            .setTitle("Success")
            .setDescription("message")
            .setDuration(5000)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
        startActivity(Intent(requireActivity(), OTPActivity::class.java).putExtra("phoneNo",phoneNo))
    }
}