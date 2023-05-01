package com.matemart.activities

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.matemart.interfaces.DismissBottomSheet
import com.matemart.utils.SharedPreference
import com.matemart.fragments.StateSelectionBottomSheetFragment
import com.matemart.fragments.CitySelectionBottomSheetFragment
import com.matemart.activities.HomeActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.matemart.utils.Toast.Toaster
import com.android.volley.VolleyError
import kotlin.Throws
import com.android.volley.AuthFailureError
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.matemart.adapter.LoginViewPagerAdapter
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.matemart.R
import com.matemart.utils.OTPView
import com.matemart.activities.LocationActivity

class OrderTrackingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_tracking)
    }
}