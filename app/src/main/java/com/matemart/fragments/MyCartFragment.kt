package com.matemart.fragments

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
import com.matemart.activities.OTPActivity
import kotlin.Throws
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
import com.matemart.databinding.FragmentMyCartBinding

class MyCartFragment : Fragment() {
    private var binding: FragmentMyCartBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyCartBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}