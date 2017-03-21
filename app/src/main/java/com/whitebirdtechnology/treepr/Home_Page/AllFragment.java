package com.whitebirdtechnology.treepr.Home_Page;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.All_Home_Page.FeedItemAll;
import com.whitebirdtechnology.treepr.DescriptionLayout.MainActivityDescription;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemAll;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemStory;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemVisited;


public class AllFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    Button buttonRefresh;
    public static List<FeedItemAll> feedItemsAll;
    public static MyAdapterAllFragment myAdapterAllFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharePreferences sharePreferences;
    VolleyServices volleyServices;
    String stringUID;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static Boolean FooterVISIBLE = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView =inflater.inflate(R.layout.fragment_all,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshAllFragment);
        frameLayout =(FrameLayout)rootView.findViewById(R.id.frameLayoutAll);
        buttonRefresh = (Button)rootView.findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfCity));
                try {
                    if(!city.isEmpty()){
                        MainHomeScreenActivity.CallHomeServiceCityPass(getActivity(),city);
                        MainHomeScreenActivity.adapter.notifyDataSetChanged();
                    }else {
                        city = MainHomeScreenActivity.hashMapActiveCity.get(MainHomeScreenActivity.arrayListActiveCity.get(0));
                        MainHomeScreenActivity.CallHomeServiceCityPass(getActivity(),city);
                        MainHomeScreenActivity.adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    try {
                        city = MainHomeScreenActivity.hashMapActiveCity.get(MainHomeScreenActivity.arrayListActiveCity.get(0));
                        MainHomeScreenActivity.CallHomeServiceCityPass(getActivity(),city);
                        MainHomeScreenActivity.adapter.notifyDataSetChanged();
                    }catch (Exception e1){
                        HashMap<String,String> para = new HashMap<>();
                        volleyServices.CallVolleyServices(para,getString(R.string.cityURL),"Home");

                    }
                }

            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        volleyServices = new VolleyServices(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayout =(LinearLayout)rootView.findViewById(R.id.linearLayoutAllFragment);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
       // feedItemsAll = new ArrayList<FeedItemAll>();
        sharePreferences = new SharePreferences(getActivity());
        mRecyclerView.setHasFixedSize(true);
       // stringAllFragmentURL = sharedPreferences.getString("URL", "")+stringAllFragmentURL;
        stringUID  = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));

        FooterVISIBLE = true;
        // specify an adapter (see also next example)
       // mAdapter = new MyAdapterAllFragment(getActivity(),feedItemsAll,aBooleanFavorite,aBooleanPlanNow,aBooleanVisited);



        myAdapterAllFragment = new MyAdapterAllFragment(feedItemsAll);
        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myAdapterAllFragment);
       // mRecyclerView.addView(mProgressBarFooter,0);
//        mAdapter.notifyDataSetChanged();
            return rootView;


    }
    public static  void AllFragment(Activity activity){
        int x= Integer.parseInt(activity.getString(R.string.contentRefreshCount));
        int k =SingltonClsAll.getInstance().arrayListAll.size();
            FooterVISIBLE = true;
            feedItemsAll = new ArrayList<FeedItemAll>();
            for (int j = 0; j < SingltonClsAll.getInstance().arrayListAll.size(); j++) {
                feedItemsAll.add(SingltonClsAll.getInstance().arrayListAll.get(j));
            }
            myAdapterAllFragment.notifyDataSetChanged();
        //FooterVISIBLE = ((itemAll * x) + x) <= k;
    }



    @Override
    public void onRefresh() {
        //swipeRefreshLayout.setRefreshing(true);
        //refreshList();

        swipeRefreshLayout.setRefreshing(false);
    }
    public void refreshList(){
        //do processing to get new data and set your listview's adapter, maybe  reinitialise the loaders you may be using or so
        //when your data has finished loading, cset the refresh state of the view to false

            int x = Integer.parseInt(getString(R.string.contentRefreshCount));
            int i = feedItemsAll.size() / x;
        if(i==0) {
            SingltonClsAll.reset();
          //  mRecyclerView.scrollToPosition(0);
        }
            if(feedItemsAll.size()%x!=0) {
                swipeRefreshLayout.setRefreshing(false);
               // FOOTER = 2;
                FooterVISIBLE = false;
                return;
            }
        SingltonClsVisited.reset();
        SingltonClsStory.reset();
            itemAll = i;
            HashMap<String,String> params = new HashMap<>();
            String stringURL = getString(R.string.homePageURL);
            // stringURL = sharePreferences.getDataFromSharePref("URL")+stringURL;

            String TREEEPRDATA = "TreeprData";
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(TREEEPRDATA,MODE_PRIVATE);
            String stringCity = sharedPreferences.getString(getString(R.string.sharPrfActiveCity), "");
            String UID = sharedPreferences.getString(getString(R.string.sharPrfUID),"");

            if(stringCity.isEmpty()||UID.isEmpty()) {
                params.put(getString(R.string.serviceKeyCity), stringCity);
                params.put(getString(R.string.serviceKeyUID),"0");
            }else {
                params.put(getString(R.string.serviceKeyUID), UID);
                params.put(getString(R.string.serviceKeyCity), stringCity);
            }
            params.put(getString(R.string.serviceKeyItem), String.valueOf(itemAll));
            params.put(getString(R.string.serviceKeyItem1),String.valueOf(itemStory));
            params.put(getString(R.string.serviceKeyItem2),String.valueOf(itemVisited));
            params.put(getString(R.string.serviceKeyCityLati),sharePreferences.getDataFromSharePref(getString(R.string.sharPrfActiveCityLati)));
            params.put(getString(R.string.serviceKeyCityLongi),sharePreferences.getDataFromSharePref(getString(R.string.sharPrfActiveCityLongi)));
            volleyServices.CallVolleyServices(params,stringURL,"AllHome");
            swipeRefreshLayout.setRefreshing(false);


    }

    public class MyAdapterAllFragment extends RecyclerView.Adapter<MyAdapterAllFragment.ViewHolder> {


     //   ImageLoader imageLoader = AppController.getInstance().getImageLoader();


       // public Boolean[] booleanFavorite,booleanPlanNow,booleanVisited;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewTitleName,textViewLocationName,textViewInfo;
            public Button buttonPlanNow,buttonVisited;
            public ImageButton imageButtonFavorite;
            public ImageView imageViewAll;
            public ViewHolder(View v) {
                super(v);
                textViewTitleName = (TextView)v.findViewById(R.id.textViewAllTitleName);
                textViewInfo = (TextView)v.findViewById(R.id.textViewAllInfo);
                imageViewAll =(ImageView) v.findViewById(R.id.imageViewAll);
                textViewLocationName = (TextView)v.findViewById(R.id.textViewAllLocation);
                buttonPlanNow = (Button)v.findViewById(R.id.buttonAllPlanNow);
                buttonVisited =(Button)v.findViewById(R.id.buttonAllVisited);
                imageButtonFavorite =(ImageButton)v.findViewById(R.id.imageButtonAllFavorite);
            }
        }

         public MyAdapterAllFragment(List<FeedItemAll> feedItems) {


        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapterAllFragment.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view




            if(viewType == TYPE_FOOTER) {
               View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.progress_bar_footer, parent, false);
                return new footerViewHolder(v);


            } else if(viewType == TYPE_ITEM) {
              View  v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_layout_items, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }


            // set the view's size, margins, paddings and layout parameters

            return null;
        }
        @Override
        public int getItemViewType (int position) {
           if(isPositionFooter (position)) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
        private boolean isPositionFooter (int position) {
            return position == feedItemsAll.size ();
        }

        // Replace the contents of a view (invoked by the layout manager)
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            if(position ==(getItemCount()-1)){
                swipeRefreshLayout.setRefreshing(true);
                refreshList();
            }

            if(holder instanceof footerViewHolder){
                if(FooterVISIBLE)
                ((footerViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                else
                ((footerViewHolder) holder).progressBar.setVisibility(View.GONE);
            }else if(holder instanceof  ViewHolder) {
                if(feedItemsAll.size()==position)
                    return;
                final FeedItemAll item = feedItemsAll.get(position);

                holder.imageViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivityDescription.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Img", item.getStringImagePath());
                        bundle.putString("ImgProf", null);
                        bundle.putString("Name", null);
                        bundle.putString("Loc", item.getStringImageName()+",\n"+item.getStringPlaceName() + ", " + item.getStringCityName());
                        bundle.putString("Status", null);
                        bundle.putString("Comment", item.getStringInfo());
                        bundle.putString("SpotId", item.getStringSpotId());
                        bundle.putString("PlaceId", item.getStringPlaceID());
                        bundle.putString("CityId", item.getStringCityID());
                        bundle.putBoolean("Visited", item.getaBooleanVisited());
                        intent.putExtra("Bundle", bundle);
                        startActivity(intent);

                    }
                });

                if (!TextUtils.isEmpty(item.getStringCityName())) {
                    holder.textViewLocationName.setText(item.getStringPlaceName() + "," + item.getStringCityName());
                } else
                    holder.textViewLocationName.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getStringInfo())) {
                    holder.textViewInfo.setText(item.getStringInfo());
                } else
                    holder.textViewInfo.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getStringImageName())) {
                    holder.textViewTitleName.setText(item.getStringImageName());
                } else holder.textViewTitleName.setVisibility(View.GONE);

                if (item.getStringImagePath() != null) {
                    final String stringImageURL = item.getStringImagePath();
                    if(!stringImageURL.isEmpty()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                new ImageDownloaderTask(holder.imageViewAll, stringImageURL, getActivity()).execute();

                            }
                        });
                        thread.start();
                    }
                } else
                    holder.imageViewAll.setVisibility(View.GONE);


                if (item.getaBooleanFavorite()) {
                    holder.imageButtonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_heart_button_on));
                } else
                    holder.imageButtonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_heart_button_off));
                if (item.getaBooleanVisited()) {
                    holder.buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_on),null,null,null);

                }else {
                    holder.buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_off),null,null,null);
                }
                holder.buttonPlanNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stringUID.equals("0")) {
                            Toast.makeText(getActivity(), "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
                        } else {
                            String Fname=sharePreferences.getDataFromSharePref(getString(R.string.sharPrfFrstName));
                            String Lname =sharePreferences.getDataFromSharePref(getString(R.string.sharPrfLstName));
                            try {
                                if(Fname.equals("null")||Fname.isEmpty()){
                                    Fname = "";
                                }
                            }catch (Exception e){
                                Fname = "";
                            }
                            try {
                                if(Lname.equals("null")||Lname.isEmpty()){
                                    Lname = "";
                                }
                            }catch (Exception e){
                                Lname = "";
                            }
                            String boby = "Hey Treepr Planed For You With Your Friend :\n" +Fname+" "+Lname  + "\nHe Want To visit With You On Place:\n" + item.getStringPlaceName() + "\nAt City:" + item.getStringCityName();
                            //
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            String shareBody = boby;
                            String shareSub = "Plan With Treepr";
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sendIntent, "Share Using"));
                        }
                    }
                });

                holder.imageButtonFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // int positionItem = position;
                        if (!item.getaBooleanFavorite()) {
                            if (stringUID.equals("0")) {
                                Toast.makeText(getActivity(), "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
                            } else {
                                HashMap<String, String> para = new HashMap<>();
                                para.put(getString(R.string.serviceKeyUID), stringUID);
                                para.put(getString(R.string.serviceKeySpotId), item.getStringSpotId());
                                para.put(getString(R.string.serviceKeyPlaceId), item.getStringPlaceID());
                                para.put(getString(R.string.serviceKeyCity), item.getStringCityID());
                                volleyServices.CallVolleyServices(para, getString(R.string.favoriteURL), "Button");
                                item.setaBooleanFavorite(true);
                                holder.imageButtonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_heart_button_on));
                            }

                        }
                    }
                });
                holder.buttonVisited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // int positionItem = position;
                        if (!item.getaBooleanVisited()) {

                            if (stringUID.equals("0")) {
                                Toast.makeText(getActivity(), "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
                            } else {
                                HashMap<String, String> para = new HashMap<>();
                                para.put(getString(R.string.serviceKeyUID), stringUID);
                                para.put(getString(R.string.serviceKeySpotId), item.getStringSpotId());
                                para.put(getString(R.string.serviceKeyPlaceId), item.getStringPlaceID());
                                para.put(getString(R.string.serviceKeyCity), item.getStringCityID());
                                volleyServices.CallVolleyServices(para, getString(R.string.visitedURL), "Button");
                                item.setaBooleanVisited(true);
                                holder.buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_on),null,null,null);

                            }

                        }
                    }
                });
            }

        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if(feedItemsAll.isEmpty()){
                FooterVISIBLE = false;
                frameLayout.setVisibility(View.VISIBLE);
            }else {
                frameLayout.setVisibility(View.GONE);
            }
            return feedItemsAll.size()+1;
        }

        private class footerViewHolder extends ViewHolder {
            public ProgressBar progressBar;
            public footerViewHolder(View v) {

                super(v);
                progressBar = (ProgressBar)v.findViewById(R.id.progressBar5);
            }
        }
    }

}