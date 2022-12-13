package com.matemart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.devs.readmoreoption.ReadMoreOption;
import com.matemart.R;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(this)
                .textLength(2, ReadMoreOption.TYPE_LINE) // OR
                //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel(getString(R.string.read_more))
                .lessLabel(getString(R.string.read_less))
                .moreLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
                .lessLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
                .labelUnderLine(false)
                .expandAnimation(true)
                .build();

//        readMoreOption.addReadMoreTo(tv_description, downloadcontentData.getDescription() + " ");
//        tv_title.setText(downloadcontentData.getTitle());
    }
}