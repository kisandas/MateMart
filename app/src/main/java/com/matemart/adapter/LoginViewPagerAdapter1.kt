package com.matemart.adapter

import androidx.appcompat.app.AppCompatActivity
import com.devs.readmoreoption.ReadMoreOption
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.textfield.TextInputEditText
import com.makeramen.roundedimageview.RoundedImageView
import com.matemart.utils.SharedPreference
import com.matemart.fragments.ChoosePictureBottomSheetFragment
import com.google.android.material.textfield.TextInputLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.matemart.activities.LoginActivity
import com.matemart.utils.Toast.Toaster
import com.android.volley.VolleyError
import com.matemart.fragments.LoginFragment
import com.matemart.fragments.RegisterFragment
import com.matemart.interfaces.DismissBottomSheet

class LoginViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
    fm!!
) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = LoginFragment()
        } else if (position == 1) {
            fragment = RegisterFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "If you are in"
        } else if (position == 1) {
            title = "Create New"
        }
        return title
    }
}