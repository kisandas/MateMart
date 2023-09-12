package com.matemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.matemart.R

class SaveAddressActivity : AppCompatActivity() {

    var selectedAddress = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_address)


        selectedAddress = intent.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS).toString()
        latitude = intent.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, 0.0)
        longitude = intent.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, 0.0)

    }
}