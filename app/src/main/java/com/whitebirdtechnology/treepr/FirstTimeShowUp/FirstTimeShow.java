package com.whitebirdtechnology.treepr.FirstTimeShowUp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.whitebirdtechnology.treepr.R;

/**
 * Created by dell on 26/3/17.
 */

public class FirstTimeShow extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayout.Tab tab1,tab2,tab3,tabFinish;
    OneTimePagerAdapter oneTimePagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.one_time_splash);
        viewPager = (ViewPager)findViewById(R.id.viewPagerOneTime);
        tabLayout = (TabLayout)findViewById(R.id.tabsOneTime);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tab1 = tabLayout.newTab().setIcon(R.drawable.dot_select);
        tab2 = tabLayout.newTab().setIcon(R.drawable.dot_unselected);
        tab3 = tabLayout.newTab().setIcon(R.drawable.dot_unselected);
        tabFinish = tabLayout.newTab().setIcon(R.drawable.arrow);
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tabFinish);
        oneTimePagerAdapter = new OneTimePagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(oneTimePagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                tab1.setIcon(R.drawable.dot_select);
                tab2.setIcon(R.drawable.dot_unselected);
                tab3.setIcon(R.drawable.dot_unselected);
                tabFinish.setIcon(R.drawable.arrow);
                break;
            case 1:
                tab1.setIcon(R.drawable.dot_unselected);
                tab2.setIcon(R.drawable.dot_select);
                tab3.setIcon(R.drawable.dot_unselected);
                tabFinish.setIcon(R.drawable.arrow);
                break;
            case 2:
                tab1.setIcon(R.drawable.dot_unselected);
                tab2.setIcon(R.drawable.dot_unselected);
                tab3.setIcon(R.drawable.dot_select);
                tabFinish.setIcon(R.drawable.arrow);
                break;
            case 3:
                tab1.setIcon(R.drawable.dot_unselected);
                tab2.setIcon(R.drawable.dot_unselected);
                tab3.setIcon(R.drawable.dot_unselected);
                tabFinish.setIcon(R.drawable.arrow);
                finish();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
