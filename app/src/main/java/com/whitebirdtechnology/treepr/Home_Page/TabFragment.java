package com.whitebirdtechnology.treepr.Home_Page;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitebirdtechnology.treepr.R;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        adapter =new MyAdapter(getChildFragmentManager());
         viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int x = Integer.parseInt(getString(R.string.contentRefreshCount));
                int i = AllFragment.feedItemsAll.size() / x;
                if(i==0)
                    SingltonClsAll.reset();
                int y = Integer.parseInt(getString(R.string.contentRefreshCount));
                int j = StoriesFragment.feedItemStoriesList.size() / y;
                if(j==0)
                    SingltonClsStory.reset();
                int z = Integer.parseInt(getString(R.string.contentRefreshCount));
                int k = VisitedFragment.feedItemVisitedList.size() /z;
                if(k==0)
                    SingltonClsVisited.reset();

                if(SingltonClsAll.getInstance().arrayListAll.size()!=0)
                AllFragment.AllFragment();
                if(SingltonClsStory.getInstance().arrayListStory.size()!=0)
                StoriesFragment.StoriesFragment();
               if(SingltonClsVisited.getInstance().arrayListVisited.size()!=0)
                VisitedFragment.VisitedFragment();
              //  Toast.makeText(getActivity(),"PageScroll"+position,Toast.LENGTH_SHORT).show();
            //    Log.d("PageScroll", String.valueOf(position));
              //  adapter.notifyDataSetChanged();



            }

            @Override
            public void onPageSelected(int position) {
                Log.d("PageSelected", String.valueOf(position));
                //Toast.makeText(getActivity(),"PageSelected="+position,Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        int x = Integer.parseInt(getString(R.string.contentRefreshCount));
                        int i = AllFragment.feedItemsAll.size() / x;
                        if(i==0)
                            SingltonClsAll.reset();
                        if(SingltonClsAll.getInstance().arrayListAll.size()!=0)
                            AllFragment.AllFragment();
                        break;
                    case 1:
                        int y = Integer.parseInt(getString(R.string.contentRefreshCount));
                        int j = StoriesFragment.feedItemStoriesList.size() / y;
                        if(j==0)
                            SingltonClsStory.reset();
                           if(SingltonClsStory.getInstance().arrayListStory.size()!=0)
                        StoriesFragment.StoriesFragment();
                        break;
                    case 2:
                        int z = Integer.parseInt(getString(R.string.contentRefreshCount));
                        int k = VisitedFragment.feedItemVisitedList.size() /z;
                        if(k==0)
                            SingltonClsVisited.reset();
                      if(SingltonClsVisited.getInstance().arrayListVisited.size()!=0)
                        VisitedFragment.VisitedFragment();
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
             //   Toast.makeText(getActivity(),"PageScroll"+state,Toast.LENGTH_SHORT).show();
             //   Log.d("PageScrollState", String.valueOf(state));
            }
        });
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new AllFragment();
                case 1 : return new StoriesFragment();
                case 2 : return new VisitedFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "ALL";
                case 1 :
                    return "STORIES";
                case 2 :
                    return "VISITED";
            }
            return null;
        }
    }

}