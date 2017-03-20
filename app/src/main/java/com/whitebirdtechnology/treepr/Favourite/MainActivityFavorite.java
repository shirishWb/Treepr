package com.whitebirdtechnology.treepr.Favourite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Sign_Up.MainActivitySignUp;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityFavorite extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayout linearLayout;
    public static List<FeedItemAll> feedItemsFav;
    public static MyAdapterFav myAdapterFav;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharePreferences sharePreferences;
    VolleyServices volleyServices;
    FrameLayout frameLayout;
    Button buttonLogIn,buttonSignUp;
    String stringUID;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static Boolean FooterVISIBLE = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Favourite");
        setContentView(R.layout.activity_main_favorite);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_favorite);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_fav);
        volleyServices = new VolleyServices(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayout =(LinearLayout)findViewById(R.id.linearLayoutAllFragment);
        sharePreferences = new SharePreferences(this);

        mRecyclerView.setHasFixedSize(true);
        // stringAllFragmentURL = sharedPreferences.getString("URL", "")+stringAllFragmentURL;
        stringUID  = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        FavoriteSingletonCls.reset();
        feedItemsFav = new ArrayList<>();
        refreshList();
        frameLayout =(FrameLayout)findViewById(R.id.frameLayoutVisited);
        buttonLogIn = (Button)findViewById(R.id.buttonLogInNoContent);
        buttonSignUp = (Button)findViewById(R.id.buttonSignUpNoContent);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityFavorite.this, MainSignInActivity.class));
                MainActivityFavorite.this.finish();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityFavorite.this, MainActivitySignUp.class));
                MainActivityFavorite.this.finish();
            }
        });
        // specify an adapter (see also next example)
        // mAdapter = new MyAdapterAllFragment(getActivity(),feedItemsAll,aBooleanFavorite,aBooleanPlanNow,aBooleanVisited);
        myAdapterFav = new MyAdapterFav();
        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myAdapterFav);
    }
    public static  void Favorite(){

        feedItemsFav = new ArrayList<FeedItemAll>();
        for (int j = 0; j < FavoriteSingletonCls.getInstance().arrayListFav.size(); j++) {
            feedItemsFav.add(FavoriteSingletonCls.getInstance().arrayListFav.get(j));
        }
        myAdapterFav.notifyDataSetChanged();
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
    public void refreshList(){
        //do processing to get new data and set your listview's adapter, maybe  reinitialise the loaders you may be using or so
        //when your data has finished loading, cset the refresh state of the view to false

            int x = Integer.parseInt(getString(R.string.contentRefreshCount));
            int i = feedItemsFav.size() / x;
        if(i==0)
            FavoriteSingletonCls.reset();
            if(feedItemsFav.size()%x!=0) {
                swipeRefreshLayout.setRefreshing(false);
                FooterVISIBLE =false;
                return;
            }
            HashMap<String,String> params = new HashMap<>();
            String stringURL = getString(R.string.favoritePageURL);
            // stringURL = sharePreferences.getDataFromSharePref("URL")+stringURL;

            String TREEEPRDATA = "TreeprData";
            SharedPreferences sharedPreferences = this.getSharedPreferences(TREEEPRDATA,MODE_PRIVATE);
            String UID = sharedPreferences.getString(getString(R.string.sharPrfUID),"");

            if(UID.isEmpty()) {
                params.put(getString(R.string.serviceKeyUID),"0");
            }else {
                params.put(getString(R.string.serviceKeyUID), UID);
            }
            params.put(getString(R.string.serviceKeyItem), String.valueOf(i));
            volleyServices.CallVolleyServices(params,stringURL,"");
            swipeRefreshLayout.setRefreshing(false);

    }

    public class MyAdapterFav extends RecyclerView.Adapter<MyAdapterFav.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewTitleName,textViewLocationName,textViewInfo;
            public Button buttonPlanNow,buttonVisited;
            public ImageButton imageButtonFavorite;
            public ImageView imageViewAll;
            public CardView cardViewFav;
            public ViewHolder(View v) {
                super(v);
                textViewTitleName = (TextView)v.findViewById(R.id.textViewAllTitleName);
                textViewInfo = (TextView)v.findViewById(R.id.textViewAllInfo);
                imageViewAll =(ImageView) v.findViewById(R.id.imageViewAll);
                textViewLocationName = (TextView)v.findViewById(R.id.textViewAllLocation);
                buttonPlanNow = (Button)v.findViewById(R.id.buttonAllPlanNow);
                buttonVisited =(Button)v.findViewById(R.id.buttonAllVisited);
                imageButtonFavorite =(ImageButton)v.findViewById(R.id.imageButtonAllFavorite);
                cardViewFav = (CardView)v.findViewById(R.id.viewFavorite);
            }
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
        public MyAdapterFav.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
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
            return position == feedItemsFav.size ();
        }

        // Replace the contents of a view (invoked by the layout manager)
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(final MyAdapterFav.ViewHolder holder, final int position) {
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
                if (feedItemsFav.size() == position)
                    return;
                holder.cardViewFav.setVisibility(View.GONE);
                holder.imageButtonFavorite.setVisibility(View.GONE);
                final FeedItemAll item = feedItemsFav.get(position);
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

                holder.imageViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivityFavorite.this, MainActivityDescription.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Img", item.getStringImagePath());
                        bundle.putString("ImgProf", null);
                        bundle.putString("Name", null);
                        bundle.putString("Loc", item.getStringPlaceName() + ", " + item.getStringCityName());
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
                if (item.getStringImagePath() != null) {
                    final String stringImageURL = item.getStringImagePath();

                    if(!stringImageURL.isEmpty()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                new ImageDownloaderTask(holder.imageViewAll, stringImageURL, MainActivityFavorite.this).execute();

                            }
                        });
                        thread.start();
                    }

                } else
                    holder.imageViewAll.setVisibility(View.GONE);

                holder.buttonPlanNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stringUID.equals("0")) {
                            Toast.makeText(MainActivityFavorite.this, "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
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

                if (item.getaBooleanVisited()) {
                    holder.buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_on),null,null,null);
                }else {
                    holder.buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_off),null,null,null);
                }
                holder.buttonVisited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // int positionItem = position;
                        if (!item.getaBooleanVisited()) {

                            if (stringUID.equals("0")) {
                                Toast.makeText(MainActivityFavorite.this, "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
                            } else {
                                HashMap<String, String> para = new HashMap<>();
                                para.put(getString(R.string.serviceKeyUID), stringUID);
                                para.put(getString(R.string.serviceKeySpotId), item.getStringSpotId());
                                para.put(getString(R.string.serviceKeyPlaceId), item.getStringPlaceID());
                                para.put(getString(R.string.serviceKeyCity), item.getStringCityID());
                                volleyServices.CallVolleyServices(para, getString(R.string.visitedURL), "");
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
            if(feedItemsFav.isEmpty()){
                FooterVISIBLE = false;
                frameLayout.setVisibility(View.VISIBLE);
                if(!sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID)).equals("0")&&!sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID)).isEmpty()){
                    buttonSignUp.setVisibility(View.GONE);
                    buttonLogIn.setVisibility(View.GONE);
                }
             //   refreshList();

            }else {
                frameLayout.setVisibility(View.GONE);
            }
            return feedItemsFav.size()+1;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
