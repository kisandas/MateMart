package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.matemart.utils.Toast.Toaster
import com.matemart.activities.OTPActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.matemart.R
import com.matemart.databinding.FragmentLoginBinding
import com.matemart.model.login.LoginData
import com.matemart.model.login.LoginResponse
import com.matemart.utils.BaseFragment
import com.matemart.utils.MyApplication
import com.matemart.utils.SharedPrefHelper
import com.matemart.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

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
    }

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

        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .write(SharedPrefHelper.KEY_LOGIN_NUMBER, binding.tvCountryCode.text.toString() + binding.etNumber.text.toString())

        Log.e("checkToken", "onResponse: $token")
        Toaster.Builder(requireActivity())
            .setTitle("Success")
            .setDescription("message")
            .setDuration(5000)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
        startActivity(Intent(requireActivity(), OTPActivity::class.java))
    }
}