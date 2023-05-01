package com.matemart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.matemart.R;

public class AddressListActivity extends AppCompatActivity {
TextView btn_add_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        btn_add_address = findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString(SimplePlacePicker.API_KEY,"");
//                bundle.putString(SimplePlacePicker.COUNTRY,"India");
//                bundle.putString(SimplePlacePicker.LANGUAGE,"en");
//                bundle.putStringArray(SimplePlacePicker.SUPPORTED_AREAS,supportedAreas);

                intent.putExtras(bundle);
                startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}