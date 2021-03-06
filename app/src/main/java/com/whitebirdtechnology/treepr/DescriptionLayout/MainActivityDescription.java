package com.whitebirdtechnology.treepr.DescriptionLayout;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;

import java.util.HashMap;

public class MainActivityDescription extends AppCompatActivity {

    ImageView imageViewFullDescriptionPage,imageViewProfileImage;
    TextView textViewName,textViewLocation,textViewStatus,textViewComment,textViewPlace;
    Button buttonPlanNow,buttonVisited;
    CardView cardViewProf;
    CollapsingToolbarLayout collapsingToolbarLayout;
    VolleyServices volleyServices;
    String stringImgFull = null;
    String stringImgProf,stringName,stringLoc,stringStatus,stringComment,stringSpotName,stringSpotId,stringPlaceId,stringCityId;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main_description);
        final SharePreferences sharePreferences = new SharePreferences(this);
        volleyServices = new VolleyServices(this);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);
        imageViewFullDescriptionPage = (ImageView)findViewById(R.id.image_view_of_description_page);
        imageViewProfileImage = (ImageView)findViewById(R.id.imageViewProfilePage_stories_desp);
        textViewName = (TextView)findViewById(R.id.textViewDespPageNameStories);
        textViewLocation = (TextView)findViewById(R.id.textViewDespPageStoriesLocation);
        textViewStatus = (TextView)findViewById(R.id.textViewDespPageMsgStories);
        textViewComment = (TextView)findViewById(R.id.text_view_comments_on_description_page);
        buttonPlanNow = (Button)findViewById(R.id.buttonDespPageStoriesPlanNow);
        buttonVisited = (Button)findViewById(R.id.buttonDespPageAllVisited);
        cardViewProf = (CardView)findViewById(R.id.view_profile_in_stories);
        textViewPlace = (TextView)findViewById(R.id.textViewDespPageStoriesPlace);
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
            collapsingToolbarLayout.setPadding(0, 0, 0, 0);
        }
        Bundle bundle = getIntent().getExtras().getBundle("Bundle");

        if (bundle != null) {
            stringImgFull = bundle.getString("Img");
        }
        stringImgProf = bundle.getString("ImgProf");
        stringName = bundle.getString("Name");
        stringLoc = bundle.getString("Loc");
        stringStatus = bundle.getString("Status");
        stringComment = bundle.getString("Comment");
        stringSpotName = bundle.getString("SpotName");
        stringSpotId = bundle.getString("SpotId");
        stringPlaceId = bundle.getString("PlaceId");
        stringCityId = bundle.getString("CityId");
        final Boolean visited = bundle.getBoolean("Visited");
        final String stringUID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));


        if(!stringImgFull.isEmpty()) {
            final String finalStringImgFull = stringImgFull;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new ImageDownloaderTask(imageViewFullDescriptionPage, finalStringImgFull, MainActivityDescription.this).execute();
                }
            });
            thread.start();

        }
        assert stringImgProf != null;
        try {
            if(!stringImgProf.isEmpty()) {
                final String stringImageURL2 = stringImgProf;

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                        new ImageDownloaderTask(imageViewProfileImage, stringImageURL2,MainActivityDescription.this).execute();
                    }
                });thread1.start();
            }else {
                imageViewProfileImage.setVisibility(View.GONE);
                cardViewProf.setVisibility(View.GONE);
            }
        }catch (Exception e){
            imageViewProfileImage.setVisibility(View.GONE);
            cardViewProf.setVisibility(View.GONE);
        }

        assert stringName != null;
        try {
            if(!stringName.isEmpty()){
                textViewName.setText(stringName);
            }else {
                textViewName.setVisibility(View.GONE);
            }
        }catch (Exception e){
            textViewName.setVisibility(View.GONE);
        }

        textViewLocation.setText(stringSpotName);
        textViewPlace.setText(stringLoc);
        assert stringStatus != null;
        try {
            if(!stringStatus.isEmpty()){
                textViewStatus.setText(stringStatus);
            }else {
                textViewStatus.setVisibility(View.GONE);
            }
        }catch (Exception e){
            textViewStatus.setVisibility(View.GONE);
        }

        textViewComment.setText(stringComment);
        try {
            if(visited) {
                buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_on),null,null,null);
            }else {
                buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_off),null,null,null);
            }
        }catch (Exception e){
            buttonVisited.setVisibility(View.GONE);
        }
        try {
            if(stringSpotId.isEmpty()){
                buttonVisited.setVisibility(View.GONE);
            }
        }catch (Exception e){
            buttonVisited.setVisibility(View.GONE);
        }


        buttonPlanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringUID.equals("0")){
                    Toast.makeText(MainActivityDescription.this,"Please LogIn OR Sign Up",Toast.LENGTH_SHORT).show();
                }else {
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
                            "Your friend " +Fname+" "+Lname  + "\nis planning to visit " + stringSpotName + "\nat city:" + stringLoc+" with you\n" +
                            "From Treepr" +
                            getString(R.string.WbPlayStoreLink);                    //
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    String shareBody = boby;
                    String shareSub = "Plan With Treeper";
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sendIntent, "Share Using"));
                }
            }
        });

        buttonVisited.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // int positionItem = position;
                if(!visited) {

                        if (stringUID.equals("0")) {
                            Toast.makeText(MainActivityDescription.this, "Please LogIn OR Sign Up", Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String,String> para = new HashMap<>();
                            para.put(getString(R.string.serviceKeyUID),stringUID);
                            para.put(getString(R.string.serviceKeySpotId),stringSpotId);
                            para.put(getString(R.string.serviceKeyPlaceId),stringPlaceId);
                            para.put(getString(R.string.serviceKeyCity),stringCityId);
                            volleyServices.CallVolleyServices(para,getString(R.string.visitedURL),"Button");
                            buttonVisited.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bookmark_on),null,null,null);
                        }
                }
            }
        });
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
