package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class LoginFragment : Fragment() {

    var btn_login: TextView? = null
    var pref: SharedPreference? = null
    var etNumber: EditText? = null
    var tvCountryCode: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_login, container, false)
        pref = SharedPreference(context)
        etNumber = view?.findViewById(R.id.etNumber)
        btn_login = view?.findViewById(R.id.btn_login)
        tvCountryCode = view?.findViewById(R.id.tvCountryCode)
        btn_login?.setOnClickListener(View.OnClickListener {
            checkValidation()
            //                startActivity(new Intent(getContext(), OTPActivity.class));
        })
        return view
    }

    private fun checkValidation() {
        if (etNumber!!.text.toString().isEmpty() || etNumber!!.text.toString().length != 10) {
            Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Number")
                .setDuration(5000)
                .setStatus(Toaster.Status.ERROR)
                .show()
            return
        }

    }

    fun successResponse(response: JSONObject){
        val token = response.optJSONObject("data").optString("token")
        pref!!.setString(SharedPreference.KEY_LOGIN_TOKEN, token)
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