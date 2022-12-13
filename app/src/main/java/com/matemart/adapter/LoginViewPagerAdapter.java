package com.matemart.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.matemart.fragments.LoginFragment;
import com.matemart.fragments.RegisterFragment;

public class LoginViewPagerAdapter extends FragmentPagerAdapter {

    public LoginViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new LoginFragment();
        }
        else if (position == 1)
        {
            fragment = new RegisterFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "If you are in";
        }
        else if (position == 1)
        {
            title = "Create New";
        }

        return title;
    }
}