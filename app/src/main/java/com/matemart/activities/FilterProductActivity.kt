package com.matemart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.matemart.R;
import com.matemart.databinding.ActivityFilterBinding;
import com.matemart.databinding.ActivityHomeBinding;
import com.matemart.fragments.RegisterFragment;

public class FilterProductActivity extends AppCompatActivity {
ActivityFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frameLayout_filter, new RegisterFragment(), "df_1");
        ft.add(R.id.frameLayout_filter, new RegisterFragment(), "df_2");
        ft.commit();
    }
}