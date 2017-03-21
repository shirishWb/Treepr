package com.whitebirdtechnology.treepr.YourStory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.DescriptionLayout.MainActivityDescription;
import com.whitebirdtechnology.treepr.Home_Page.MainActivityYourStories;
import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Sign_Up.MainActivitySignUp;
import com.whitebirdtechnology.treepr.Stories_Home_page.FeedItemStories;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityYourStoryPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static List<FeedItemStories> feedItemsYourStory;
    public static MyAdapterYourStory myAdapterYourStory;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharePreferences sharePreferences;
    VolleyServices volleyServices;
    String stringUID;
    FrameLayout frameLayout;
    TextView textViewNoContents;
    Button buttonLogIn,buttonSignUp;
    Dialog alertDialogDismiss;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static Boolean FooterVISIBLE = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Stories");
        setContentView(R.layout.activity_main_your_story_page);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
                if(UID.isEmpty()||UID.equals("0")) {
                    Toast.makeText(MainActivityYourStoryPage.this,"Please Login",Toast.LENGTH_SHORT).show();
                    return;
                }
                selectImage();
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //  Toast.makeText(MainHomeScreenActivity.this,"float",Toast.LENGTH_LONG).show();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_your_story);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_YourStory);
        volleyServices = new VolleyServices(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharePreferences = new SharePreferences(this);
        mRecyclerView.setHasFixedSize(true);
        stringUID  = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        YourStorySingltonCls.reset();
        feedItemsYourStory = new ArrayList<>();
        refreshList();
        frameLayout =(FrameLayout)findViewById(R.id.frameLayoutVisited);
        textViewNoContents = (TextView)findViewById(R.id.textViewNoContents);
        textViewNoContents.setText("You haven't create any stories yet");
        buttonLogIn = (Button)findViewById(R.id.buttonLogInNoContent);
        buttonSignUp = (Button)findViewById(R.id.buttonSignUpNoContent);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityYourStoryPage.this, MainSignInActivity.class));
                MainActivityYourStoryPage.this.finish();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityYourStoryPage.this, MainActivitySignUp.class));
                MainActivityYourStoryPage.this.finish();
            }
        });
        // specify an adapter (see also next example)
        // mAdapter = new MyAdapterAllFragment(getActivity(),feedItemsAll,aBooleanFavorite,aBooleanPlanNow,aBooleanVisited);
        myAdapterYourStory = new MyAdapterYourStory();
        // use a linear layout manager


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myAdapterYourStory);

    }

    public static  void YourStories(Activity activity){
        FooterVISIBLE = true;
        feedItemsYourStory = new ArrayList<FeedItemStories>();
        for (int j = 0; j < YourStorySingltonCls.getInstance().arrayListYourStory.size(); j++) {
            feedItemsYourStory.add(YourStorySingltonCls.getInstance().arrayListYourStory.get(j));
        }
        myAdapterYourStory.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
    public void refreshList() {
        //do processing to get new data and set your listview's adapter, maybe  reinitialise the loaders you may be using or so
        //when your data has finished loading, cset the refresh state of the view to false

                int x = Integer.parseInt(getString(R.string.contentRefreshCount));
                int i = feedItemsYourStory.size() / x;
        if(i==0)
            YourStorySingltonCls.reset();
                if (feedItemsYourStory.size() % x != 0) {
                    swipeRefreshLayout.setRefreshing(false);
                    FooterVISIBLE =false;
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                String stringURL = getString(R.string.yourStoryURL);
                // stringURL = sharePreferences.getDataFromSharePref("URL")+stringURL;

                String TREEEPRDATA = "TreeprData";
                SharedPreferences sharedPreferences = this.getSharedPreferences(TREEEPRDATA, MODE_PRIVATE);

                String UID = sharedPreferences.getString(getString(R.string.sharPrfUID), "");

                if (UID.isEmpty()) {
                    params.put(getString(R.string.serviceKeyUID), "0");
                } else {
                    params.put(getString(R.string.serviceKeyUID), UID);
                }
                params.put(getString(R.string.serviceKeyItem), String.valueOf(i));
                volleyServices.CallVolleyServices(params, stringURL, "");
                swipeRefreshLayout.setRefreshing(false);

    }



    public class MyAdapterYourStory extends RecyclerView.Adapter<MyAdapterYourStory.ViewHolder> {


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
                CardView cardViewProf;
                public ViewHolder(View v) {
                    super(v);
                    cardViewProf = (CardView)v.findViewById(R.id.view_profile_in_stories);
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


        private class footerViewHolder extends ViewHolder {
            public ProgressBar progressBar;
            public footerViewHolder(View v) {

                super(v);
                progressBar = (ProgressBar)v.findViewById(R.id.progressBar5);
            }
        }
            // Create new views (invoked by the layout manager)
            @Override
            public MyAdapterYourStory.ViewHolder onCreateViewHolder(ViewGroup parent,
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
            return position == feedItemsYourStory.size ();
        }

            // Replace the contents of a view (invoked by the layout manager)
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onBindViewHolder(final MyAdapterYourStory.ViewHolder holder, final int position) {
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
                    if (feedItemsYourStory.size() == position) {

                        return;
                    }
                    holder.cardViewProf.setVisibility(View.GONE);
                    final FeedItemStories item = feedItemsYourStory.get(position);

                    holder.imageViewStories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivityYourStoryPage.this, MainActivityDescription.class);
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
                        holder.textViewLocationName.setText( item.getStringSpotName()+", "+item.getStringPlaceName() + ", " +item.getStringCityName());
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
                                    new ImageDownloaderTask(holder.imageViewPrfStry, stringImageURL, MainActivityYourStoryPage.this).execute();

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
                        final String stringImageURL = item.getStringImagePath();
                        if(!stringImageURL.isEmpty()) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                                    new ImageDownloaderTask(holder.imageViewStories, stringImageURL, MainActivityYourStoryPage.this).execute();

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
                                Toast.makeText(MainActivityYourStoryPage.this, "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
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
                                        "Your friend " +Fname+" "+Lname  + "\nis planning to visit " +item.getStringSpotName()+", " + item.getStringPlaceName() + "\nat city:" + item.getStringCityName()+" with you\n" +
                                        "From Treepr" +
                                        getString(R.string.WbPlayStoreLink);
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
                }
            }

            // Return the size of your dataset (invoked by the layout manager)
            @Override
            public int getItemCount() {
                if(feedItemsYourStory.isEmpty()){
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
                return feedItemsYourStory.size()+1;
            }
        }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityYourStoryPage.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.choose_profile_pick_dialog,null);
        builder.setView(view);
        alertDialogDismiss = builder.create();
        alertDialogDismiss.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textViewTitle = (TextView)view.findViewById(R.id.textViewTitleDialog);
        textViewTitle.setText("Add Your Story");
        TextView textViewFromGallery = (TextView) view.findViewById(R.id.textViewFromGallery);
        TextView textViewCamera = (TextView) view.findViewById(R.id.textViewCamera);
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDismiss.dismiss();

                String path = Environment.getExternalStorageDirectory()+ "/Images";

                File imgFile = new File(path);
                if (!imgFile.exists()) {
                    File wallpaperDirectory = new File("/sdcard/Images/");
                    wallpaperDirectory.mkdirs();
                }
                File file = new File(new File("/sdcard/Images/"), "one.jpg");
                Uri uri = Uri.fromFile(file);
                try {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(takePicture, 0);
                }catch (Exception e){
                    Toast.makeText(MainActivityYourStoryPage.this,"Please Check Permission",Toast.LENGTH_SHORT).show();
                }

            }
        });
        textViewFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDismiss.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        alertDialogDismiss.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    //   Uri uri =data.getData();
                    //  Toast.makeText(MainHomeScreenActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
                    // Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // StartImageCropActivity(null,photo,"0");
                    String path = Environment.getExternalStorageDirectory()+ "/Images";

                    File imgFile = new File(path);
                    if (!imgFile.exists()) {
                        File wallpaperDirectory = new File("/sdcard/Images/");
                        wallpaperDirectory.mkdirs();
                    }
                    File file = new File(new File("/sdcard/Images/"), "one.jpg");
                    Uri uri = Uri.fromFile(file);
                    StartImageCropActivity(uri);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    StartImageCropActivity(selectedImage);
                }
                break;
        }
    }
    public void  StartImageCropActivity(Uri uri){
        Intent intent = new Intent(MainActivityYourStoryPage.this,MainActivityYourStories.class);
        try {
            String stringUri = uri.toString();

            intent.putExtra("STRING_URI",stringUri);
        } catch (Exception e){

        }
        startActivity(intent);

    }
}
