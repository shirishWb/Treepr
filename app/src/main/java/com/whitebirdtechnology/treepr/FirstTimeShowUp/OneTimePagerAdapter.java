package com.whitebirdtechnology.treepr.FirstTimeShowUp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by dell on 26/3/17.
 */

public class OneTimePagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    public OneTimePagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
            case 3:
                return new TabFinish();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
