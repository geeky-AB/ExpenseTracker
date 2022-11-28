package com.example.expensetracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterLogin extends FragmentPagerAdapter {

    private Context context;
    int pages;

    public AdapterLogin(FragmentManager fm, Context context, int pages) {
        super(fm);
        this.context = context;
        this.pages = pages;
    }


    @Override
    public int getCount() {
        return pages;
    }

    public Fragment getItem(int pos){
        switch(pos){
            case 0:
            case 2:
                LoginFrag loginFrag = new LoginFrag();
                return loginFrag;
            case 1:
                SignUpFrag signUpFrag = new SignUpFrag();
                return signUpFrag;
            default:
                return null;
        }
    }
}
