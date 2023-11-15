package com.matemart.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.matemart.R
import com.matemart.databinding.ActivityHomeBinding
import com.matemart.utils.MyApplication
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.Utils
import com.matemart.utils.Utils.LOGIN_MESSAGE

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

        if (Build.VERSION.SDK_INT > 32) {
            Dexter.withContext(this@HomeActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    }


                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                    }
                }).check()
        } else {
            Dexter.withContext(this@HomeActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    }


                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                    }
                }).check()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Log.e("chevkkkkk", "onNavigationItemSelected: " + item.title)
        Log.e("chevkkkkk", "onNavigationItemSelected: " + id + "    " + R.id.navigation_cart)
        if (id == R.id.navigation_cart) {
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {

                Toast.makeText(
                    this@HomeActivity,
                    LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)


                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(applicationContext, CartActivity::class.java))
            }
            return false
        } else if (id == R.id.navigation_home) {
            findNavController(
                this,
                R.id.nav_host_fragment_activity_home
            ).navigate(R.id.navigation_home)
            return true
        } else if (id == R.id.navigation_order) {
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    this@HomeActivity,
                    LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)

                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finishAffinity()
            } else {
                findNavController(
                    this,
                    R.id.nav_host_fragment_activity_home
                ).navigate(R.id.navigation_order)
            }
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