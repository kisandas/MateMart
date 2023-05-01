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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultCallback
import com.theartofdev.edmodo.cropper.CropImage
import com.matemart.interfaces.DismissBottomSheet
import com.matemart.adapter.StateSelectionAdapter
import com.matemart.utils.SharedPreference
import com.matemart.utils.Toast.Toaster
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.matemart.activities.OTPActivity
import com.android.volley.VolleyError
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.matemart.activities.WhishListActivity
import com.matemart.activities.AddressListActivity
import com.matemart.activities.ProfileActivity
import com.matemart.activities.PostYourRequirements
import com.matemart.activities.ArchitecturalProfessionalListActivity
import com.matemart.activities.LabouresListActivity
import com.matemart.activities.PolicyDetailsActivity
import androidx.core.content.res.ResourcesCompat
import com.matemart.utils.CustomTypefaceSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.matemart.R
import com.matemart.api.Constants
import com.matemart.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class RegisterFragment : Fragment() {
    var tv_checktext: TextView? = null
    var btn_register: TextView? = null
    var pref: SharedPreference? = null
    var etName: EditText? = null
    var etNumber: EditText? = null
    var checkBoxTerms: CheckBox? = null
    var tvCountryCode: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        pref = SharedPreference(context)
        var view = inflater.inflate(R.layout.fragment_register, container, false)
        etName = view.findViewById(R.id.etName)
        etNumber = view.findViewById(R.id.etNumber)
        tv_checktext = view.findViewById(R.id.tv_checktext)
        checkBoxTerms = view.findViewById(R.id.checkBoxTerms)
        tvCountryCode = view.findViewById(R.id.tvCountryCode)
        btn_register = view.findViewById(R.id.btn_register)
        btn_register?.setOnClickListener(View.OnClickListener { checkValidation() })
        setTextIntoTextView(
            tv_checktext,
            "I agree to the",
            " Terms & Conditions",
            " and",
            " Privacy Policy",
            ""
        )
        return view
    }

    private fun checkValidation() {
        if (etName!!.text.toString().isEmpty() || etName!!.text.toString().length < 5) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Name")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }
        if (etNumber!!.text.toString().isEmpty() || etNumber!!.text.toString().length != 10) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Number")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }
        if (!checkBoxTerms!!.isChecked) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Read and Accept Terms and Conditions.")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
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

    fun successResponse(response: JSONObject) {
        val token = response.optJSONObject("data").optString("token")
        pref!!.setString(SharedPreference.KEY_LOGIN_TOKEN, token)
        Toaster.Builder(requireActivity())
            .setTitle("Success")
            .setDescription("message")
            .setDuration(5000)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
        startActivity(Intent(requireActivity(), OTPActivity::class.java))
    }
}