package matemart.material.interior.activities

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
import matemart.material.interior.R
import matemart.material.interior.databinding.ActivityHomeBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.ResGetProfileDetails
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import matemart.material.interior.utils.Utils.LOGIN_MESSAGE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityHomeBinding? = null
    var navView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        navView = findViewById(R.id.nav_view)
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
//        Utils.showBadge(this, navView, R.id.navigation_home, "")
//        Utils.showBadge(this, navView, R.id.navigation_order, "")
        matemart.material.interior.utils.Utils.showBadge(this, navView, R.id.navigation_cart, "")
//        Utils.showBadge(this, navView, R.id.navigation_profile, "")

        navView?.setOnNavigationItemSelectedListener(this);

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

        if (id == R.id.navigation_cart) {
            getUserProfile()
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
                pref.logoutProfile(this@HomeActivity)
                finishAffinity()
            } else {
                startActivity(Intent(applicationContext, CartActivity::class.java))
            }
            return false
        } else if (id == R.id.navigation_home) {
            getUserProfile()
            findNavController(
                this,
                R.id.nav_host_fragment_activity_home
            ).navigate(R.id.navigation_home)
            return true
        } else if (id == R.id.navigation_order) {
            getUserProfile()
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    this@HomeActivity,
                    LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.logoutProfile(this@HomeActivity)

//                startActivity(Intent(applicationContext, LoginActivity::class.java))
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


    override fun onResume() {
        super.onResume()
        getUserProfile()
    }

    private fun getUserProfile() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@HomeActivity)
        var call: Call<ResGetProfileDetails> = apiInterface.getUserProfile()!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>, response: Response<ResGetProfileDetails>
            ) {

                if (response.isSuccessful) {
                    if (response.body()?.statuscode == 11) {
                        response.body()?.data?.let {

                            if (it.cart_badge_count !== null && it.cart_badge_count!! > 0) {
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .write(SharedPrefHelper.BADGE_COUNT, it.cart_badge_count!!)
                                if (it.cart_badge_count!! > 0 && it.cart_badge_count!! > 99) {
                                    matemart.material.interior.utils.Utils.showBadge(
                                        this@HomeActivity,
                                        navView,
                                        R.id.navigation_cart,
                                        "99+"
                                    )
                                } else if (it.cart_badge_count!! > 0) {
                                    matemart.material.interior.utils.Utils.showBadge(
                                        this@HomeActivity,
                                        navView,
                                        R.id.navigation_cart,
                                        it.cart_badge_count.toString()
                                    )
                                } else {
                                    matemart.material.interior.utils.Utils.hideBadge(
                                        navView,
                                        R.id.navigation_cart
                                    )
                                }

                            } else {
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .write(SharedPrefHelper.BADGE_COUNT,0)
                                matemart.material.interior.utils.Utils.hideBadge(
                                    navView,
                                    R.id.navigation_cart
                                )
                            }
                        }
                    }


                } else {
                    Toast.makeText(
                        this@HomeActivity, "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Log.e("jjjjjjjjjjj", "onFailure: " + t.message)
                Toast.makeText(this@HomeActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })

    }

}