package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.matemart.R
import com.matemart.databinding.ActivityHomeBinding
import com.matemart.utils.Utils

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityHomeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
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

        navView.setOnNavigationItemSelectedListener(this);
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Log.e("chevkkkkk", "onNavigationItemSelected: " + item.title)
        Log.e("chevkkkkk", "onNavigationItemSelected: " + id + "    " + R.id.navigation_cart)
        if (id == R.id.navigation_cart) {
            startActivity(Intent(applicationContext, CartActivity::class.java))
            return false
        } else if (id == R.id.navigation_home) {
            findNavController(
                this,
                R.id.nav_host_fragment_activity_home
            ).navigate(R.id.navigation_home)
            return true
        } else if (id == R.id.navigation_order) {
            findNavController(
                this,
                R.id.nav_host_fragment_activity_home
            ).navigate(R.id.navigation_order)
            return true
        } else if (id == R.id.navigation_profile) {
            findNavController(
                this,
                R.id.nav_host_fragment_activity_home
            ).navigate(R.id.navigation_profile)
            return true
        } else {
            return true
        }


    }
}