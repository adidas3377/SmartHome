package ru.adscity.smart_house.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class FPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragmentPages;

    public FPagerAdapter(FragmentManager fm, Fragment[] fragmentPages) {
        super(fm);
        this.fragmentPages = fragmentPages;
    }

    @Override
    public Fragment getItem(int p) {
        return fragmentPages[p];
    }

    @Override
    public int getCount() {
        return fragmentPages.length;
    }
}
