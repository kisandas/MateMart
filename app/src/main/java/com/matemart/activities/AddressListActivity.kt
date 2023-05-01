package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.essam.simpleplacepicker.MapActivity
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.matemart.R

class AddressListActivity : AppCompatActivity() {
    var btn_add_address: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        btn_add_address = findViewById(R.id.btn_add_address)
        btn_add_address?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AddressListActivity, MapActivity::class.java)
            val bundle = Bundle()
            bundle.putString(SimplePlacePicker.API_KEY, "")
            //                bundle.putString(SimplePlacePicker.COUNTRY,"India");
//                bundle.putString(SimplePlacePicker.LANGUAGE,"en");
//                bundle.putStringArray(SimplePlacePicker.SUPPORTED_AREAS,supportedAreas);
            intent.putExtras(bundle)
            startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}