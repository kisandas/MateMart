package com.matemart.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.matemart.adapter.LoginViewPagerAdapter
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.matemart.R
import com.matemart.databinding.ActivityLoginBinding
import com.matemart.databinding.FragmentLoginBinding
import com.matemart.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    var adapter: LoginViewPagerAdapter? = null

    private lateinit var binding: ActivityLoginBinding
    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
//        setContentView(binding.root)
//        setContentView(R.layout.activity_login)

        adapter = LoginViewPagerAdapter(supportFragmentManager)
        binding.viewPagerLogin.adapter = adapter

        binding.tabs.setupWithViewPager(binding.viewPagerLogin)
        val tab0 = binding.tabs.getTabAt(0)
        val tab1 = binding.tabs.getTabAt(1)
        tab0!!.customView = createCustomTabView("If you are in", 18, R.color.theme_blue_38B449)
        tab1!!.customView = createCustomTabView("Create New", 16, R.color.dark_gray_b3b3b3)
        setTabTextSize(tab0, 18, R.color.theme_blue_38B449, true)
        setTabTextSize(tab1, 16, R.color.dark_gray_b3b3b3, false)
        binding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
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