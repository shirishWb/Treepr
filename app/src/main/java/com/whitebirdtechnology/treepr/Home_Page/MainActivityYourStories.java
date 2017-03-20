package com.whitebirdtechnology.treepr.Home_Page;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivityYourStories extends AppCompatActivity implements View.OnClickListener {
    TextView textViewCancel,textViewShare;
    public static AutoCompleteTextView autoCompleteTextViewCity,autoCompletetextViewPlace,autoCompletetextViewSpot;
    EditText editTextStatus,editTextComment;
    String stringUID;
    SharePreferences sharePreferences;
    public static ArrayList<String> arrayListCity,arrayListPlace,arrayListSpot;
    Uri uri;
    public static ArrayAdapter<String> adapterCity,adapterPlace,adapterSpot;
    ImageView imageViewStory;
    VolleyServices volleyServices;
    HashMap<String,String> params;
    public static HashMap<String,String> hashMapCityId,hashMapPlaceId,hashMapSpotId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_your_stories);
        sharePreferences = new SharePreferences(this);
        stringUID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        imageViewStory = (ImageView)findViewById(R.id.imageViewYourStories);
        editTextStatus = (EditText)findViewById(R.id.editTextStatus);
        editTextComment = (EditText)findViewById(R.id.editTextComment);
        textViewCancel = (TextView)findViewById(R.id.textViewCancelStory);
        textViewShare = (TextView)findViewById(R.id.textViewShareStory);
        autoCompleteTextViewCity = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewCityYourStory);
        autoCompletetextViewPlace = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewPlaceYourStory);
        autoCompletetextViewSpot = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewSpotYourStory);
        textViewCancel.setOnClickListener(this);
        textViewShare.setOnClickListener(this);
        arrayListCity = new ArrayList<>();
        arrayListPlace = new ArrayList<>();
        arrayListSpot = new ArrayList<>();
        hashMapCityId = new HashMap<>();
        hashMapPlaceId= new HashMap<>();
        hashMapSpotId= new HashMap<>();
        params = new HashMap<>();
        volleyServices = new VolleyServices(this);
        params.put(getString(R.string.serviceKeyStoryVal),"0");
        volleyServices.CallVolleyServices(params,getString(R.string.storySpot),"");
        autoCompleteTextViewCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextViewCity.showDropDown();
            }
        });
        autoCompletetextViewPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompletetextViewPlace.showDropDown();
            }
        });
        autoCompletetextViewSpot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompletetextViewSpot.showDropDown();
            }
        });
        autoCompleteTextViewCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(arrayListCity.contains(s.toString())){
                    HashMap<String,String> para = new HashMap<String, String>();
                    para.put(getString(R.string.serviceKeyStoryVal),"1");
                    para.put(getString(R.string.serviceKeyCity),hashMapCityId.get(s.toString()));
                    volleyServices.CallVolleyServices(para,getString(R.string.storySpot),"");
                }else {
                    arrayListSpot = new ArrayList<String>();
                    arrayListPlace = new ArrayList<String>();
                    autoCompletetextViewPlace.setText("");
                    autoCompletetextViewSpot.setText("");
                    hashMapPlaceId = new HashMap<String, String>();
                    hashMapSpotId = new HashMap<String, String>();
                    adapterPlace = new ArrayAdapter<String>(MainActivityYourStories.this,android.R.layout.simple_dropdown_item_1line, arrayListPlace);
                    autoCompletetextViewPlace.setAdapter(adapterPlace);
                    adapterSpot = new ArrayAdapter<String>(MainActivityYourStories.this,android.R.layout.simple_dropdown_item_1line, arrayListSpot);
                    autoCompletetextViewSpot.setAdapter(adapterSpot);
                }
            }
        });
        autoCompletetextViewPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String city = String.valueOf(autoCompleteTextViewCity.getText());
                if(arrayListPlace.contains(s.toString())&&arrayListCity.contains(city)){
                    HashMap<String,String> para = new HashMap<String, String>();
                    para.put(getString(R.string.serviceKeyPlaceId),hashMapPlaceId.get(s.toString()));
                    para.put(getString(R.string.serviceKeyStoryVal),"2");
                    volleyServices.CallVolleyServices(para,getString(R.string.storySpot),"");
                }else {
                    arrayListSpot = new ArrayList<String>();
                    autoCompletetextViewSpot.setText("");
                    hashMapSpotId = new HashMap<String, String>();
                    adapterSpot = new ArrayAdapter<String>(MainActivityYourStories.this,android.R.layout.simple_dropdown_item_1line, arrayListSpot);
                    autoCompletetextViewSpot.setAdapter(adapterSpot);
                }
            }
        });

        try {
            uri = Uri.parse(getIntent().getStringExtra("STRING_URI"));
            imageViewStory.setImageURI(uri);
        }catch (Exception e){
            Toast.makeText(MainActivityYourStories.this,"Image Is Deleted",Toast.LENGTH_LONG).show();
        }

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onClick(View v) {
        if(v==textViewCancel){
            startActivity(new Intent(MainActivityYourStories.this,MainHomeScreenActivity.class));
            this.finish();
        }
        if(v==textViewShare){
            String stringImg = null;
            try
            {
               // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , uri);
                imageViewStory.buildDrawingCache();
                Bitmap bitmap = imageViewStory.getDrawingCache();
                stringImg = getStringImage(bitmap);
                Log.d("Image String",stringImg);
            }
            catch (Exception e)
            {
             return;   //handle exception
            }
           // Bitmap bmp = imageViewStory.getDrawingCache();

            String stringSpot = autoCompletetextViewSpot.getText().toString();
            String stringStatus = editTextStatus.getText().toString();
            String stringCity = autoCompleteTextViewCity.getText().toString();
            String stringPlace = autoCompletetextViewPlace.getText().toString();
            String stringComment = editTextComment.getText().toString();
            if(stringStatus.isEmpty()){
                Toast.makeText(MainActivityYourStories.this,"Enter Some Status",Toast.LENGTH_SHORT).show();
                return;
            }
            if(stringComment.isEmpty()){
                Toast.makeText(MainActivityYourStories.this,"Enter Some Comment",Toast.LENGTH_SHORT).show();
                return;
            }
            if(stringCity.isEmpty()){
                Toast.makeText(MainActivityYourStories.this,"Enter City",Toast.LENGTH_SHORT).show();
                return;
            }
            if(stringPlace.isEmpty()){
                Toast.makeText(MainActivityYourStories.this,"Enter Place",Toast.LENGTH_SHORT).show();
                return;
            }
            if(stringSpot.isEmpty()){
                Toast.makeText(MainActivityYourStories.this,"Enter Spot",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!hashMapCityId.containsKey(stringCity)){
                Toast.makeText(MainActivityYourStories.this,"Enter Valid City",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!hashMapPlaceId.containsKey(stringPlace)){
                Toast.makeText(MainActivityYourStories.this,"Enter Valid Place",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!hashMapSpotId.containsKey(stringSpot)){
                Toast.makeText(MainActivityYourStories.this,"Enter Valid Spot",Toast.LENGTH_SHORT).show();
                return;
            }
            SharePreferences sharePreferences = new SharePreferences(this);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.serviceKeyProfileImage),stringImg);
            params.put(getString(R.string.serviceKeySpotId),hashMapSpotId.get(stringSpot));
            params.put(getString(R.string.serviceKeyStatus),stringStatus);
            params.put(getString(R.string.serviceKeyComment),stringComment);
            params.put(getString(R.string.serviceKeyUID),sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID)));
            params.put(getString(R.string.serviceKeyCity),hashMapCityId.get(stringCity));
            params.put(getString(R.string.serviceKeyPlaceId),hashMapPlaceId.get(stringPlace));
            volleyServices.CallVolleyServices(params,getString(R.string.shareStoryURL),"");
        }

    }


}
