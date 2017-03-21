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

import com.android.volley.RequestQueue;
import com.whitebirdtechnology.treepr.DescriptionLayout.MainActivityDescription;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Stories_Home_page.FeedItemStories;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemAll;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemStory;
import static com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity.itemVisited;


public class StoriesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerViewStories;
    //private RecyclerView.Adapter mAdapterStories;
    private RecyclerView.LayoutManager mLayoutManagerStories;
    public static MyAdapterStories myAdapterStories;
    String stringStoriesFragmentURL = "show.php";
    String stringUID;
    FrameLayout frameLayout;
    Button buttonRefresh;
    RequestQueue requestQueueStories;
    public static List<FeedItemStories> feedItemStoriesList;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharePreferences sharePreferences;
    VolleyServices volleyServices;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static Boolean FooterVISIBLE = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewStories =inflater.inflate(R.layout.fragment_stories, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootViewStories.findViewById(R.id.refreshStories);
        mRecyclerViewStories = (RecyclerView) rootViewStories.findViewById(R.id.my_recycler_view_stories);
        frameLayout =(FrameLayout)rootViewStories.findViewById(R.id.frameLayoutStories);
        buttonRefresh = (Button)rootViewStories.findViewById(R.id.buttonRefresh);
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
        volleyServices = new VolleyServices(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewStories.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerStories = new LinearLayoutManager(getActivity());
        mRecyclerViewStories.setLayoutManager(mLayoutManagerStories);
        mRecyclerViewStories.setHasFixedSize(true);
        sharePreferences = new SharePreferences(getActivity());


        stringUID  = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        // Toast.makeText(getActivity(),stringAllFragmentURL,Toast.LENGTH_SHORT).show();
/*

        HashMap<String,String> params = new HashMap<>();

        // String stringUid = sharedPreferences.getString("UID", "");
        String stringCity = sharedPreferences.getString("CityProfile", "");
        if(stringCity.isEmpty()||stringUID.isEmpty()) {
            params.put("city", "0");
            params.put("userid","0");
            //    Toast.makeText(getActivity(),"skip",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(getActivity(),stringCity,Toast.LENGTH_SHORT).show();
            params.put("userid", stringUID);
            params.put("city", "1");
        }
        params.put("item","0");

        requestQueueStories = Volley.newRequestQueue(getActivity().getApplicationContext());
        volleyService = new VolleyService(stringStoriesFragmentURL,requestQueueStories);
        String stringStoriesResponce= volleyService.callVolleyService(params);


                try {
                    JSONObject object = new JSONObject(stringStoriesResponce);
                    JSONObject jsonObjectPlaces = object.getJSONObject("Story");
                    String stringSuccess = jsonObjectPlaces.getString("success");
                    if(Integer.parseInt(stringSuccess)==0){
                        Toast.makeText(getActivity(),jsonObjectPlaces.getString("message"),Toast.LENGTH_SHORT).show();
                    }else {
                        int size = jsonObjectPlaces.length()-3;
                        // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                        for(int i =0 ; i<size;i++) {
                            JSONObject jsonObject = jsonObjectPlaces.getJSONObject(String.valueOf(i));
                            FeedItemStories feedItemStories = new FeedItemStories();
                            feedItemStories.setStringImagePath(jsonObject.getString("path"));
                            feedItemStories.setStringCityName(jsonObject.getString("city"));
                            feedItemStories.setStringInfo(jsonObject.getString("info"));
                            feedItemStories.setStringImageName(jsonObject.getString("imageName"));


                            spinner.setVisibility(View.GONE);
                            feedItemStoriesList.add(feedItemStories);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    spinner.setVisibility(View.GONE);
                    // success[0] =true;
                }
*/


        // specify an adapter (see also next example)
        // mAdapter = new MyAdapterAllFragment(getActivity(),feedItemsAll,aBooleanFavorite,aBooleanPlanNow,aBooleanVisited);
        myAdapterStories = new MyAdapterStories(feedItemStoriesList);
        // use a linear layout manager

        mLayoutManagerStories = new LinearLayoutManager(getActivity());
        mRecyclerViewStories.setLayoutManager(mLayoutManagerStories);
        mRecyclerViewStories.setAdapter(myAdapterStories);

        return rootViewStories;

    }

    public static  void StoriesFragment(){
        FooterVISIBLE = true;
        feedItemStoriesList = new ArrayList<FeedItemStories>();
        for (int j = 0; j < SingltonClsStory.getInstance().arrayListStory.size(); j++) {
            feedItemStoriesList.add(SingltonClsStory.getInstance().arrayListStory.get(j));
        }
        myAdapterStories.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
    public void refreshList(){
        //do processing to get new data and set your listview's adapter, maybe  reinitialise the loaders you may be using or so
        //when your data has finished loading, cset the refresh state of the view to false

            int x = Integer.parseInt(getString(R.string.contentRefreshCount));
            int i = feedItemStoriesList.size() / x;
        if(i==0)
            SingltonClsStory.reset();
            if(feedItemStoriesList.size()%x!=0) {
                swipeRefreshLayout.setRefreshing(false);
                FooterVISIBLE =false;
                return;
            }
        SingltonClsAll.reset();
        SingltonClsVisited.reset();
            itemStory = i;
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
            volleyServices.CallVolleyServices(params,stringURL,"StoryHome");
            swipeRefreshLayout.setRefreshing(false);

    }
    public class MyAdapterStories extends RecyclerView.Adapter<StoriesFragment.MyAdapterStories.ViewHolder> {

        private LayoutInflater inflater;
        private List<FeedItemStories> feedItems;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewTitleName,textViewLocationName,textViewInfo;
            public Button buttonPlanNow,buttonShare;
            public ImageView imageViewStories;
            public ImageView imageViewPrfStry;
            public TextView textViewMsgSry;
            public ViewHolder(View v) {
                super(v);
                textViewMsgSry = (TextView)v.findViewById(R.id.textViewMsgStories);
                imageViewPrfStry = (ImageView)v.findViewById(R.id.imageViewProfilePage_stories);
                textViewTitleName = (TextView)v.findViewById(R.id.textViewNameStories);
                textViewInfo = (TextView)v.findViewById(R.id.textViewStoriesInfo);
                imageViewStories =(ImageView) v.findViewById(R.id.imageViewStories);
                textViewLocationName = (TextView)v.findViewById(R.id.textViewStoriesLocation);
                buttonPlanNow = (Button)v.findViewById(R.id.buttonStoriesPlanNow);
                buttonShare =(Button)v.findViewById(R.id.buttonStoriesShare);

            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapterStories(List<FeedItemStories> feedItems) {

            this.feedItems = feedItems;
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
        public StoriesFragment.MyAdapterStories.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            // create a new view
            if(viewType == TYPE_FOOTER) {
                View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.progress_bar_footer, parent, false);
                return new footerViewHolder(v);


            } else if(viewType == TYPE_ITEM) {
                View  v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.stories_layout_items, parent, false);
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
            return position == feedItemStoriesList.size ();
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
                if (feedItemStoriesList.size() == position)
                    return;

                final FeedItemStories item = feedItemStoriesList.get(position);

                holder.imageViewStories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivityDescription.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Img", item.getStringImagePath());
                        bundle.putString("ImgProf", item.getStringPrfImgPath());
                        bundle.putString("Name", item.getStringImageName());
                        bundle.putString("Loc", item.getStringPlaceName() + ", " + item.getStringCityName());
                        bundle.putString("SpotName",item.getStringSpotName());
                        bundle.putString("Status", item.getStringStatus());
                        bundle.putString("Comment", item.getStringInfo());
                        bundle.putString("SpotId", null);
                        bundle.putString("PlaceId", null);
                        bundle.putString("CityId", null);
                        bundle.putBoolean("Visited", false);
                        intent.putExtra("Bundle", bundle);
                        startActivity(intent);

                    }
                });

                if (!TextUtils.isEmpty(item.getStringCityName())) {
                    holder.textViewLocationName.setText(item.getStringSpotName());
                } else
                    holder.textViewLocationName.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getStringInfo())) {
                    holder.textViewInfo.setText(item.getStringInfo());
                } else
                    holder.textViewInfo.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getStringImageName())) {
                    holder.textViewTitleName.setText(item.getStringImageName());
                } else holder.textViewTitleName.setVisibility(View.GONE);
                if (item.getStringPrfImgPath() != null) {
                    final String stringImageURL = item.getStringPrfImgPath();
                    if(!stringImageURL.isEmpty()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                new ImageDownloaderTask(holder.imageViewPrfStry, stringImageURL, getActivity()).execute();

                            }
                        });
                        thread.start();
                    }
                } else
                    holder.imageViewPrfStry.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getStringStatus())) {
                    holder.textViewMsgSry.setText(item.getStringStatus());
                } else
                    holder.textViewMsgSry.setVisibility(View.GONE);
                if (item.getStringImagePath() != null) {
                    final String stringImageURL =  item.getStringImagePath();
                    if(!stringImageURL.isEmpty()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                new ImageDownloaderTask(holder.imageViewStories, stringImageURL, getActivity()).execute();

                            }
                        });
                        thread.start();
                    }
                } else
                    holder.imageViewStories.setVisibility(View.GONE);
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
                            String boby = "Hey,\n" +
                                    "Your friend " +Fname+" "+Lname  + "\nis planning to visit "  +item.getStringSpotName()+", "+ item.getStringPlaceName() + "\nat city:" + item.getStringCityName()+" with you\n" +
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
            if(feedItemStoriesList.isEmpty()){
                FooterVISIBLE = false;
               frameLayout.setVisibility(View.VISIBLE);
            }else {
                frameLayout.setVisibility(View.GONE);
            }
            return feedItemStoriesList.size()+1;
        }
    }




}
