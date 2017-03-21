package com.whitebirdtechnology.treepr.Home_Page;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Sign_Up.MainActivitySignUp;
import com.whitebirdtechnology.treepr.Visited_Home_Page.FeedItemVisited;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemAll;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemStory;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemVisited;


public class VisitedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerViewVisited;
   // private RecyclerView.Adapter mAdapterVisited;
    private RecyclerView.LayoutManager mLayoutManagerVisited;
    public static MyAdapterVisited myAdapterVisited;
    public static List<FeedItemVisited> feedItemVisitedList;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharePreferences sharePreferences;
    FrameLayout frameLayout;
    VolleyServices volleyServices;
    Button buttonLogIn,buttonSignUp;
    String stringUID;
    TextView textViewNoContents;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static Boolean FooterVISIBLE = true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewVisited =inflater.inflate(R.layout.fragment_visited,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootViewVisited.findViewById(R.id.refreshVisited);
        mRecyclerViewVisited = (RecyclerView) rootViewVisited.findViewById(R.id.my_recycler_view_visited);
        frameLayout =(FrameLayout)rootViewVisited.findViewById(R.id.frameLayoutVisited);
        textViewNoContents = (TextView)rootViewVisited.findViewById(R.id.textViewNoContents);
        textViewNoContents.setText("You haven't visit any place yet");
        volleyServices = new VolleyServices(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        sharePreferences = new SharePreferences(getActivity());
        SingltonClsVisited.reset();
    //    refreshList();
        buttonLogIn = (Button)rootViewVisited.findViewById(R.id.buttonLogInNoContent);
        buttonSignUp = (Button)rootViewVisited.findViewById(R.id.buttonSignUpNoContent);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainSignInActivity.class));
                getActivity().finish();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivitySignUp.class));
                getActivity().finish();
            }
        });

        feedItemVisitedList = new ArrayList<>();
        stringUID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        mRecyclerViewVisited.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerVisited = new LinearLayoutManager(getActivity());
        mRecyclerViewVisited.setLayoutManager(mLayoutManagerVisited);

        myAdapterVisited = new MyAdapterVisited(feedItemVisitedList);
        mRecyclerViewVisited.setAdapter(myAdapterVisited);


        return rootViewVisited;
    }

    public static  void VisitedFragment(){
        FooterVISIBLE = true;
        feedItemVisitedList = new ArrayList<FeedItemVisited>();
        for (int j = 0; j < SingltonClsVisited.getInstance().arrayListVisited.size(); j++) {
            feedItemVisitedList.add(SingltonClsVisited.getInstance().arrayListVisited.get(j));
        }
        try{
            myAdapterVisited.notifyDataSetChanged();
        }catch (Exception e){

        }

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
            int i = feedItemVisitedList.size() / x;
        if(i==0)
            SingltonClsVisited.reset();
            if(feedItemVisitedList.size()%x!=0) {
                swipeRefreshLayout.setRefreshing(false);
                FooterVISIBLE =false;
                return;
            }
        SingltonClsAll.reset();
        SingltonClsStory.reset();
            itemVisited = i;
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
        String lati =sharePreferences.getDataFromSharePref(getString(R.string.sharPrfActiveCityLati));
            params.put(getString(R.string.serviceKeyCityLati),lati);
            params.put(getString(R.string.serviceKeyCityLongi),sharePreferences.getDataFromSharePref(getString(R.string.sharPrfActiveCityLongi)));
            volleyServices.CallVolleyServices(params,stringURL,"VisitedHome");
            swipeRefreshLayout.setRefreshing(false);




    }

    public class MyAdapterVisited extends RecyclerView.Adapter<VisitedFragment.MyAdapterVisited.ViewHolder> {

        private LayoutInflater inflater;
        private List<FeedItemVisited> feedItems;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewTitleName,textViewLocationName;
            public Button buttonRePlanNow,buttonShare;
            public ImageView imageViewVisited;
            public ViewHolder(View v) {
                super(v);
                textViewTitleName = (TextView)v.findViewById(R.id.textViewVisitedTitleName);
                imageViewVisited =(ImageView) v.findViewById(R.id.imageViewVisited);
                textViewLocationName = (TextView)v.findViewById(R.id.textViewVisitedLocation);
                buttonRePlanNow = (Button)v.findViewById(R.id.buttonVisitedRe_plan);
                buttonShare =(Button)v.findViewById(R.id.buttonVisitedShare);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapterVisited(List<FeedItemVisited> feedItemVisitedList) {

            this.feedItems = feedItemVisitedList;
        }
        private class footerViewHolder extends ViewHolder {
            public ProgressBar progressBar;
            public footerViewHolder(View v) {

                super(v);
                progressBar = (ProgressBar)v.findViewById(R.id.progressBar5);
            }
        }
        // Create new views (invoked by the layout manager)
        @Override
        public VisitedFragment.MyAdapterVisited.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            if(viewType == TYPE_FOOTER) {
                View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.progress_bar_footer, parent, false);
                return new footerViewHolder(v);


            } else if(viewType == TYPE_ITEM) {
                View  v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.visited_layout_items, parent, false);
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
            return position == feedItemVisitedList.size ();
        }

        // Replace the contents of a view (invoked by the layout manager)
        @TargetApi(Build.VERSION_CODES.M)
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
                if (feedItemVisitedList.size() == position)
                    return;

                final FeedItemVisited item = feedItemVisitedList.get(position);
                if (!TextUtils.isEmpty(item.getStringCityName())&&!TextUtils.isEmpty(item.getStringPlaceName())) {
                    holder.textViewLocationName.setText(item.getStringPlaceName()+", "+item.getStringCityName());
                } else
                    holder.textViewLocationName.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(item.getStringSpotName())) {
                    holder.textViewTitleName.setText(item.getStringSpotName());
                } else holder.textViewTitleName.setVisibility(View.GONE);

                if (item.getStringImagePath() != null) {
                    final String stringImageURL = item.getStringImagePath();
                    if(!stringImageURL.isEmpty()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                new ImageDownloaderTask(holder.imageViewVisited, stringImageURL, getActivity()).execute();

                            }
                        });
                        thread.start();
                    }
                } else
                    holder.imageViewVisited.setVisibility(View.GONE);

                holder.buttonRePlanNow.setOnClickListener(new View.OnClickListener() {
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
                            String boby = "Hey,\n" +
                                    "Your friend " +Fname+" "+Lname  + "\nis planning to visit " +item.getStringSpotName()+", "+ item.getStringPlaceName() + "\nat city:" + item.getStringCityName()+" with you\n" +
                                    "From Treepr" +
                                    getString(R.string.WbPlayStoreLink);                            //
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
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if(feedItemVisitedList.isEmpty()){
                FooterVISIBLE = false;
                frameLayout.setVisibility(View.VISIBLE);
                if(!sharePreferences.getDataFromSharePref(getActivity().getString(R.string.sharPrfUID)).equals("0")&&!sharePreferences.getDataFromSharePref(getActivity().getString(R.string.sharPrfUID)).isEmpty()){
                    buttonSignUp.setVisibility(View.GONE);
                    buttonLogIn.setVisibility(View.GONE);
                }
            }else {

                frameLayout.setVisibility(View.GONE);
            }
            return feedItemVisitedList.size()+1;
        }
    }


}
