package com.matemart.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matemart.R;
import com.matemart.activities.OTPActivity;
import com.matemart.utils.CustomTypefaceSpan;

public class RegisterFragment extends Fragment {
    View view;
    TextView tv_checktext;
    TextView btn_register;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        tv_checktext = view.findViewById(R.id.tv_checktext);


        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OTPActivity.class));
            }
        });
        setTextIntoTextView(tv_checktext,"I agree to the"," Terms & Conditions"," and"," Privacy Policy","");




        return view;
    }

    public void setTextIntoTextView(TextView tv, String first, String second, String third, String fourth, String fifth) {


        Spannable spannable = new SpannableString(first + second + third + fourth + fifth);
        Typeface typeface_regular = ResourcesCompat.getFont(getContext(), R.font.font_inter_regular);
        Typeface typeface_bold = ResourcesCompat.getFont(getContext(), R.font.font_inter_bold);

        int first_length = 0 + first.length();
        int second_length = first_length + second.length();
        int third_length = second_length + third.length();
        int fourth_length = third_length + fourth.length();
        int fifth_length = fourth_length + fifth.length();

        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), 0, first_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_bold, ContextCompat.getColor(getContext(), R.color.theme_blue_38B449)), first_length, second_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), second_length, third_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_bold, ContextCompat.getColor(getContext(), R.color.theme_blue_38B449)), third_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan(typeface_regular, ContextCompat.getColor(getContext(), R.color.dark_gray_b3b3b3)), fourth_length, fifth_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv.setText(spannable);
    }
}