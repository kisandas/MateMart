package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.matemart.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {
    private var binding: FragmentMyProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    fun initView() {
        binding!!.tvWishList.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    WhishListActivity::class.java
                )
            )
        }
        binding!!.tvAddress.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    AddressListActivity::class.java
                )
            )
        }
        binding!!.tvEdit.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ProfileActivity::class.java
                )
            )
        }
        binding!!.tvPostRequirements.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PostYourRequirements::class.java
                )
            )
        }
        binding!!.tvArch.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ArchitecturalProfessionalListActivity::class.java
                )
            )
        }
        binding!!.tvLabour.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    LabouresListActivity::class.java
                )
            )
        }
        binding!!.tvPolicy.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PolicyDetailsActivity::class.java
                )
            )
        }
        binding!!.tvContactUs.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PolicyDetailsActivity::class.java
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}