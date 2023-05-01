package com.matemart.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

class LoginActivity : AppCompatActivity() {
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var adapter: LoginViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewPager = findViewById<View>(R.id.view_pager_login) as ViewPager
        adapter = LoginViewPagerAdapter(supportFragmentManager)
        viewPager!!.adapter = adapter
        tabLayout = findViewById(R.id.tabs)
        tabLayout?.setupWithViewPager(viewPager)
        val tab0 = tabLayout?.getTabAt(0)
        val tab1 = tabLayout?.getTabAt(1)
        tab0!!.customView = createCustomTabView("If you are in", 18, R.color.theme_blue_38B449)
        tab1!!.customView = createCustomTabView("Create New", 16, R.color.dark_gray_b3b3b3)
        setTabTextSize(tab0, 18, R.color.theme_blue_38B449, true)
        setTabTextSize(tab1, 16, R.color.dark_gray_b3b3b3, false)
        tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setTabTextSize(tab, 18, R.color.theme_blue_38B449, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabTextSize(tab, 16, R.color.dark_gray_b3b3b3, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setTabTextSize(
        tab: TabLayout.Tab?,
        tabSizeSp: Int,
        textColor: Int,
        isSelected: Boolean
    ) {
        val tabCustomView = tab!!.customView
        val tabTextView = tabCustomView!!.findViewById<TextView>(R.id.tabTV)
        tabTextView.textSize = tabSizeSp.toFloat()
        tabTextView.setTextColor(ContextCompat.getColor(tabCustomView.context, textColor))
        val typeface: Typeface?
        typeface = if (isSelected) {
            ResourcesCompat.getFont(this@LoginActivity, R.font.font_inter_bold)
        } else {
            ResourcesCompat.getFont(this@LoginActivity, R.font.font_inter_regular)
        }
        tabTextView.typeface = typeface
    }

    private fun createCustomTabView(tabText: String, tabSizeSp: Int, textColor: Int): View {
        val tabCustomView = layoutInflater.inflate(R.layout.tab_customview, null)
        val tabTextView = tabCustomView.findViewById<TextView>(R.id.tabTV)
        tabTextView.text = tabText
        tabTextView.textSize = tabSizeSp.toFloat()
        tabTextView.setTextColor(ContextCompat.getColor(tabCustomView.context, textColor))
        return tabCustomView
    }
}