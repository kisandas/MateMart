package com.matemart.activities;

/**
 * ProfileActivity.class is Activity for ProfileActivity Check and Update.
 * Developed By Kisandas
 * last updated 27/09/2022 for GreetBuzz Ltd.
 */


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.matemart.R;
import com.matemart.fragments.ChoosePictureBottomSheetFragment;
import com.matemart.utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfileActivity extends AppCompatActivity  {

    CircleImageView iv_profile_pic;
    ImageView iv_editProfile;
    String newImagePath = "";
    TextView tv_name;
    MultiAutoCompleteTextView tv_social_text;
    EditText et_name;
    TextInputEditText tv_category;
    RoundedImageView iv_countryFlag;
    TextInputEditText tv_select_country;
    TextInputEditText tv_select_state;
    String selected_category;
    MultiAutoCompleteTextView et_number;
    MultiAutoCompleteTextView et_email;
    MultiAutoCompleteTextView et_address;
    TextInputEditText tv_select_language;
    MultiAutoCompleteTextView et_website;
    RecyclerView rc_socialMedia;

    TextView btn_save_profile;
    TextView tv_version;

    SharedPreference pref;
    ChoosePictureBottomSheetFragment cdd;
    boolean isProfileDPUpdate = false;
    boolean isDataUpdate = false;

    TextInputLayout til_number, til_email, til_website, til_social;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeView();




        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initializeView() {
        pref = new SharedPreference(this);


        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        iv_editProfile = findViewById(R.id.iv_editProfile);
        tv_name = findViewById(R.id.tv_name);

        et_name = findViewById(R.id.et_name);
        tv_category = findViewById(R.id.tv_category);
        iv_countryFlag = findViewById(R.id.iv_countryFlag);
        tv_select_country = findViewById(R.id.tv_select_country);
        tv_select_state = findViewById(R.id.tv_select_state);

        rc_socialMedia = findViewById(R.id.rc_socialMedia);

        btn_save_profile = findViewById(R.id.btn_save_profile);
        tv_version = findViewById(R.id.tv_version);

        tv_select_language = findViewById(R.id.tv_select_language);

        til_number = findViewById(R.id.til_number);
        til_email = findViewById(R.id.til_email);
        til_website = findViewById(R.id.til_website);
        til_social = findViewById(R.id.til_social);

        et_number = (MultiAutoCompleteTextView) findViewById(R.id.et_number);
        et_email = (MultiAutoCompleteTextView) findViewById(R.id.et_email);
        et_address = (MultiAutoCompleteTextView) findViewById(R.id.et_address);
        et_website = (MultiAutoCompleteTextView) findViewById(R.id.et_website);
        tv_social_text = (MultiAutoCompleteTextView) findViewById(R.id.tv_social_text);




        ColorDrawable cd = new ColorDrawable(Color.parseColor("#F3F3F3"));
        tv_social_text.setDropDownBackgroundDrawable(cd);
        et_email.setDropDownBackgroundDrawable(cd);
        et_number.setDropDownBackgroundDrawable(cd);
        et_website.setDropDownBackgroundDrawable(cd);
        et_address.setDropDownBackgroundDrawable(cd);


    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}