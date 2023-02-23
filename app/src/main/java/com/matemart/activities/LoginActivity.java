package com.matemart.activities;

import static com.matemart.api.Constants.BASE_URL;
import static com.matemart.api.Constants.SEND_OTP;
import static com.matemart.utils.Utils.ERROR_MESSAGE;
import static com.matemart.utils.Utils.ERROR_TITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.matemart.BuildConfig;
import com.matemart.R;
import com.matemart.adapter.LoginViewPagerAdapter;
import com.matemart.utils.Toast.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    LoginViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.view_pager_login);
        adapter = new LoginViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        TabLayout.Tab tab1 = tabLayout.getTabAt(1);

        tab0.setCustomView(createCustomTabView("If you are in", 18, R.color.theme_blue_38B449));
        tab1.setCustomView(createCustomTabView("Create New", 16, R.color.dark_gray_b3b3b3));

        setTabTextSize(tab0, 18, R.color.theme_blue_38B449, true);
        setTabTextSize(tab1, 16, R.color.dark_gray_b3b3b3, false);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabTextSize(tab, 18, R.color.theme_blue_38B449, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabTextSize(tab, 16, R.color.dark_gray_b3b3b3, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setTabTextSize(TabLayout.Tab tab, int tabSizeSp, int textColor, boolean isSelected) {

        View tabCustomView = tab.getCustomView();
        TextView tabTextView = tabCustomView.findViewById(R.id.tabTV);
        tabTextView.setTextSize(tabSizeSp);
        tabTextView.setTextColor(ContextCompat.getColor(tabCustomView.getContext(), textColor));
        Typeface typeface;
        if (isSelected) {
            typeface = ResourcesCompat.getFont(LoginActivity.this, R.font.font_inter_bold);
        } else {
            typeface = ResourcesCompat.getFont(LoginActivity.this, R.font.font_inter_regular);
        }
        tabTextView.setTypeface(typeface);

    }

    private View createCustomTabView(String tabText, int tabSizeSp, int textColor) {

        View tabCustomView = getLayoutInflater().inflate(R.layout.tab_customview, null);
        TextView tabTextView = tabCustomView.findViewById(R.id.tabTV);
        tabTextView.setText(tabText);
        tabTextView.setTextSize(tabSizeSp);
        tabTextView.setTextColor(ContextCompat.getColor(tabCustomView.getContext(), textColor));
        return tabCustomView;
    }


}