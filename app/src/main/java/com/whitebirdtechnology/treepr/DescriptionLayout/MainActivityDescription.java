package com.whitebirdtechnology.treepr.DescriptionLayout;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
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
    TextView textViewName,textViewLocation,textViewStatus,textViewComment;
    Button buttonPlanNow,buttonVisited;
    CardView cardViewProf;
    CollapsingToolbarLayout collapsingToolbarLayout;
    VolleyServices volleyServices;
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
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
            collapsingToolbarLayout.setPadding(0, 0, 0, 0);
        }
        Bundle bundle = getIntent().getExtras().getBundle("Bundle");
        final String stringImgFull = bundle.getString("Img");
        String stringImgProf = bundle.getString("ImgProf");
        String stringName = bundle.getString("Name");
        final String stringLoc = bundle.getString("Loc");
        String stringStatus = bundle.getString("Status");
        String stringComment = bundle.getString("Comment");
        final String stringSpotId = bundle.getString("SpotId");
        final String stringPlaceId = bundle.getString("PlaceId");
        final String stringCityId = bundle.getString("CityId");
        final Boolean visited = bundle.getBoolean("Visited");
        final String stringUID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));


        if(!stringImgFull.isEmpty()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new ImageDownloaderTask(imageViewFullDescriptionPage, stringImgFull, MainActivityDescription.this).execute();
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

        textViewLocation.setText(stringLoc);
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
                    String boby = "Hey Treeper Planed For You With Your Friend :\n"+sharePreferences.getDataFromSharePref(getString(R.string.sharPrfFrstName))+"\nHe Want To visit With You On Place:\n"+stringLoc;
                    //
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
                            volleyServices.CallVolleyServices(para,getString(R.string.visitedURL),"");
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
