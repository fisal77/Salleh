package com.seniorproject.salleh.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.salleh.Activities.SettingsFragment;

/**
 * Created by abdul on 18-Feb-2017.
 */

public class SettingsPageAdapter extends FragmentStatePagerAdapter {
    public SettingsPageAdapter(FragmentManager fm) {
        super(fm);
    }
    private static int NUM_ITEMS= 1;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){
//        switch (position){
//
//            case 0:
                return SettingsFragment.newInstance(0, "Settings");
       // }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return "Settings";
    }




}
