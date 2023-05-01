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
import com.matemart.databinding.ActivityHomeBinding
import com.matemart.utils.Utils

class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_order,
            R.id.navigation_cart,
            R.id.navigation_profile
        )
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment_activity_home)
        //        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setupWithNavController(binding!!.navView, navController)
        Utils.showBadge(this, navView, R.id.navigation_home, "")
        Utils.showBadge(this, navView, R.id.navigation_order, "")
        Utils.showBadge(this, navView, R.id.navigation_cart, "")
        Utils.showBadge(this, navView, R.id.navigation_profile, "")
    }
}