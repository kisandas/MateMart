package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matemart.activities.WhishListActivity
import com.matemart.activities.AddressListActivity
import com.matemart.activities.ProfileActivity
import com.matemart.activities.PostYourRequirements
import com.matemart.activities.ArchitecturalProfessionalListActivity
import com.matemart.activities.LabouresListActivity
import com.matemart.activities.PolicyDetailsActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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