package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.matemart.utils.Toast.Toaster
import com.matemart.activities.OTPActivity
import androidx.core.content.res.ResourcesCompat
import com.matemart.utils.CustomTypefaceSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.matemart.R
import com.matemart.databinding.FragmentLoginBinding
import com.matemart.databinding.FragmentRegisterBinding
import com.matemart.model.login.LoginResponse
import com.matemart.utils.BaseFragment
import com.matemart.utils.MyApplication
import com.matemart.utils.SharedPrefHelper
import com.matemart.viewmodel.AuthViewModel
import org.json.JSONObject

class RegisterFragment : BaseFragment() {


    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentRegisterBinding
    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun initViewBinding(): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getRootView(): View {
        return binding.root
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

        binding.btnRegister.setOnClickListener {
            checkValidation()
        }
        setTextIntoTextView(
            binding.tvChecktext,
            "I agree to the",
            " Terms & Conditions",
            " and",
            " Privacy Policy",
            ""
        )
    }

    private fun checkValidation() {
        if (binding.etName.text.toString().isEmpty() || binding.etName.text.toString().length < 5) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Name")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }
        if (binding.etNumber.text.toString().isEmpty() || binding.etNumber.text.toString().length != 10) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Number")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }
        if (!binding.checkBoxTerms.isChecked) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Read and Accept Terms and Conditions.")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }

        viewModel.register(binding.tvCountryCode.text.toString() + binding.etNumber.text.toString(),binding.etName.text.toString())
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            Log.e("checkLogin-->", "checkValidation: " + it.data.toString())
            successResponse(it.data as LoginResponse)
        }

    }

    fun setTextIntoTextView(
        tv: TextView?,
        first: String,
        second: String,
        third: String,
        fourth: String,
        fifth: String
    ) {
        val spannable: Spannable = SpannableString(first + second + third + fourth + fifth)
        val typeface_regular = ResourcesCompat.getFont(requireContext(), R.font.font_inter_regular)
        val typeface_bold = ResourcesCompat.getFont(requireContext(), R.font.font_inter_bold)
        val first_length = 0 + first.length
        val second_length = first_length + second.length
        val third_length = second_length + third.length
        val fourth_length = third_length + fourth.length
        val fifth_length = fourth_length + fifth.length
        spannable.setSpan(
            CustomTypefaceSpan(
                typeface_regular,
                ContextCompat.getColor(requireContext(), R.color.dark_gray_b3b3b3)
            ), 0, first_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan(
                typeface_bold,
                ContextCompat.getColor(requireContext(), R.color.theme_blue_38B449)
            ), first_length, second_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan(
                typeface_regular,
                ContextCompat.getColor(requireContext(), R.color.dark_gray_b3b3b3)
            ), second_length, third_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan(
                typeface_bold,
                ContextCompat.getColor(requireContext(), R.color.theme_blue_38B449)
            ), third_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan(
                typeface_regular,
                ContextCompat.getColor(requireContext(), R.color.dark_gray_b3b3b3)
            ), fourth_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv!!.text = spannable
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