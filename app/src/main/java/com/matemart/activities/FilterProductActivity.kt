package com.matemart.activities

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.matemart.fragments.RegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import com.matemart.R
import com.matemart.databinding.ActivityFilterBinding

class FilterProductActivity : AppCompatActivity() {
    var binding: ActivityFilterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.frameLayout_filter, RegisterFragment(), "df_1")
        ft.add(R.id.frameLayout_filter, RegisterFragment(), "df_2")
        ft.commit()
    }
}