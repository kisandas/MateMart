package com.matemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devs.readmoreoption.ReadMoreOption
import androidx.core.content.ContextCompat
import com.matemart.R

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val readMoreOption = ReadMoreOption.Builder(this)
            .textLength(2, ReadMoreOption.TYPE_LINE) // OR
            //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel(getString(R.string.read_more))
            .lessLabel(getString(R.string.read_less))
            .moreLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .lessLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .labelUnderLine(false)
            .expandAnimation(true)
            .build()

//        readMoreOption.addReadMoreTo(tv_description, downloadcontentData.getDescription() + " ");
//        tv_title.setText(downloadcontentData.getTitle());
    }
}