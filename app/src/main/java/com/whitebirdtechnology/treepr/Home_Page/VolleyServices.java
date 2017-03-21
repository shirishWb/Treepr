package com.whitebirdtechnology.treepr.Home_Page;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whitebirdtechnology.treepr.All_Home_Page.FeedItemAll;
import com.whitebirdtechnology.treepr.Favourite.FavoriteSingletonCls;
import com.whitebirdtechnology.treepr.Favourite.MainActivityFavorite;
import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Sign_Up.MainActivitySignUp;
import com.whitebirdtechnology.treepr.SplashScreen.MainActivitySplashScreen;
import com.whitebirdtechnology.treepr.Stories_Home_page.FeedItemStories;
import com.whitebirdtechnology.treepr.Visited_Home_Page.FeedItemVisited;
import com.whitebirdtechnology.treepr.YourStory.MainActivityYourStoryPage;
import com.whitebirdtechnology.treepr.YourStory.YourStorySingltonCls;
import com.whitebirdtechnology.treepr.ProfileUpdate.MainProfileEditActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.whitebirdtechnology.treepr.Favourite.MainActivityFavorite.feedItemsFav;
import static com.whitebirdtechnology.treepr.YourStory.MainActivityYourStoryPage.feedItemsYourStory;



public class VolleyServices {
    private Activity activity;
    private RequestQueue requestQueue;
    private SharePreferences sharePreferences;
    private ProgressDialog progressDialogs;
    private String key;
    private String successKey,msgKey,nameKey,cityKey,dobKey,profileKey,succValueKey,mobNoKey,genderKey,emailKey,uidKey,placeKey,storyKey,visitedKey,imgPathkey,placeIdKey,placeNameKey,imgNameKey,infoKey,cityNameKey,arrayListKey,commentKey,profileImgPathKey,favBoolKey,visitedBoolKey,spotIdKey,stateNamekey,stateCityNameKey,stateIdkey,stringAtvCityLati,stringAtvCityLongi,spotNameKey;


    public  VolleyServices(Activity activity){
        this.activity = activity;
        progressDialogs = new ProgressDialog(activity);
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        sharePreferences = new SharePreferences(activity);
        successKey = activity.getString(R.string.serviceKeySuccess);
        nameKey = activity.getString(R.string.serviceKeyName);
        mobNoKey   = activity.getString(R.string.serviceKeyMoblNo);
        msgKey  = activity.getString(R.string.serviceKeyMsg);
        cityKey = activity.getString(R.string.serviceKeyCity);
        dobKey = activity.getString(R.string.serviceKeyDOB);
        profileKey  = activity.getString(R.string.serviceKeyProfile);
        succValueKey = activity.getString(R.string.serviceKeySuccessValue);
        genderKey = activity.getString(R.string.serviceKeyGender);
        emailKey = activity.getString(R.string.serviceKeyEmail);
        uidKey  = activity.getString(R.string.serviceKeyUID);
        placeKey  = activity.getString(R.string.serviceKeyPlace);
        storyKey = activity.getString(R.string.serviceKeyStory);
        visitedKey = activity.getString(R.string.serviceKeyVisited);
        imgPathkey = activity.getString(R.string.serviceKeyImgPath);
        placeIdKey= activity.getString(R.string.serviceKeyPlaceId);
        placeNameKey =activity.getString(R.string.serviceKeyPlaceName);
        cityNameKey=activity.getString(R.string.serviceKeyCityName);
        infoKey=activity.getString(R.string.serviceKeyInfo);
        imgNameKey=activity.getString(R.string.serviceKeyImgName);
        arrayListKey = activity.getString(R.string.serviceKeyArrayList);
        commentKey = activity.getString(R.string.serviceKeyComment);
        profileImgPathKey = activity.getString(R.string.serviceKeyProfileImagePath);
        favBoolKey = activity.getString(R.string.serviceKeyFavBool);
        visitedBoolKey = activity.getString(R.string.serviceKeyVisitedBool);
        spotIdKey = activity.getString(R.string.serviceKeySpotId);
        stateNamekey = activity.getString(R.string.serviceKeyStateName);
        stateCityNameKey = activity.getString(R.string.serviceKeyStateCityName);
        stateIdkey = activity.getString(R.string.serviceKeyStateId);
        stringAtvCityLati = activity.getString(R.string.serviceKeyCityLati);
        stringAtvCityLongi = activity.getString(R.string.serviceKeyCityLongi);
        spotNameKey = activity.getString(R.string.serviceKeySpotName);

    }
    public void CallVolleyServices(HashMap<String,String> params,String stringURL,String key){
        progressDialogs.setMessage("Processing...");
        progressDialogs.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogs.setIndeterminate(true);
        progressDialogs.setCancelable(false);
        progressDialogs.setProgress(20);
        if(!key.equals("AllHome")&&!key.equals("StoryHome")&&!key.equals("VisitedHome")&&!key.equals("Splash")&&!key.equals("Button"))
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialogs.show();
            }
        });
        this.key = key;
       new BackgrounVolley(params,stringURL,activity).execute();
    }
    private class BackgrounVolley extends AsyncTask<Void,Void,Void>{
        HashMap<String,String> parameters;
        String stringURL;
        Activity activity;

        BackgrounVolley(HashMap<String, String> params, String stringURL, Activity activity){
            this.parameters = params;
            this.stringURL = stringURL;
            this.activity =activity;
        }
        @Override
        protected Void doInBackground(Void... params) {
            Volley(parameters,stringURL,activity);
            return null;
        }
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private  static  String[] PERMISION_CAMERA = {Manifest.permission.CAMERA
    };
    private  static  String[] PERMISION_LOCATION = {Manifest.permission.LOCATION_HARDWARE};
    //persmission method.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void verifyStoragePermissions(final Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.LOCATION_HARDWARE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"Please Give Storage Permission",Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, MainSignInActivity.class));
                    activity.finish();
                }
            });

        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startActivity(new Intent(activity, MainHomeScreenActivity.class));
                    activity.finish();
                }
            });
        }
        if(cameraPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_CAMERA,REQUEST_EXTERNAL_STORAGE);
        }
        if(locationPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_LOCATION,REQUEST_EXTERNAL_STORAGE);
        }
    }
    private void Volley(final HashMap<String, String> params, final String stringURL, final Activity activity){
        String URL = sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfURL))+stringURL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resp" + stringURL, response);
                if (stringURL.equals(activity.getString(R.string.facebookURL))) {
                    try {
                        JSONObject object = new JSONObject(response);

                        String success = object.getString(successKey);
                        if(success.equals(succValueKey)){
                            String profile = object.getString(profileKey);
                            JSONObject object1 = new JSONObject(profile);
                            String stringGenderSharPrf = object1.getString(genderKey);
                            String stringEmailSharPref = object1.getString(emailKey);
                            String stringMobileNoProfile = object1.getString(mobNoKey);
                            String stringDateOfBirth = object1.getString(dobKey);
                            String stringCityNameSharePrf = object1.getString(cityKey);
                            String UID = object1.getString(uidKey);
                            String stringImagePath = object1.getString(profileImgPathKey);
                            String stringCityName = object1.getString(cityNameKey);
                            if(stringCityName.isEmpty()||stringCityName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),stringCityName);

                            String stringStateName = object1.getString(stateNamekey);
                            if(stringStateName.isEmpty()||stringStateName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),stringStateName);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfImageSel),stringImagePath);
                            // Writing data to SharedPreferences
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfUID), UID);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfGender), stringGenderSharPrf);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfEmail), stringEmailSharPref);
                            if (!stringMobileNoProfile.isEmpty() && !stringMobileNoProfile.equals("null")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), stringMobileNoProfile);
                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), null);

                            //  editor.putString("ImageProfile", stringImageNameProf);
                            if (!stringDateOfBirth.isEmpty() && !stringDateOfBirth.equals("0000-00-00")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), stringDateOfBirth);

                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), null);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCity), stringCityNameSharePrf);
                            String[] stringsName = object1.getString(nameKey).split("\\s");
                            try {
                                String stringFirstNameSHarPrf = stringsName[0];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), stringFirstNameSHarPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), null);
                            }
                            try {
                                String stringLastNameSharPrf = stringsName[1];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), stringLastNameSharPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), null);

                            }
                            verifyStoragePermissions(activity);


                            //   spinner.setVisibility(View.GONE);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();

                    }

                }else if(stringURL.equals(activity.getString(R.string.facebookLogInURL))){
                    try {
                        JSONObject object = new JSONObject(response);

                        String success = object.getString(successKey);
                        final String stringMSG = object.getString(msgKey);
                        if (success.equals(succValueKey)) {

                            String profile = object.getString(profileKey);
                            JSONObject object1 = new JSONObject(profile);
                            String stringGenderSharPrf = object1.getString(genderKey);
                            String stringEmailSharPref = object1.getString(emailKey);
                            String stringMobileNoProfile = object1.getString(mobNoKey);
                            String stringDateOfBirth = object1.getString(dobKey);
                            String stringCityNameSharePrf = object1.getString(cityKey);
                            String UID = object1.getString(uidKey);
                            String stringImagePath = object1.getString(profileImgPathKey);
                            String stringStateName = object1.getString(stateNamekey);
                            String stringCityName = object1.getString(cityNameKey);
                            if(stringCityName.isEmpty()||stringCityName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),stringCityName);
                            if(stringStateName.isEmpty()||stringStateName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),stringStateName);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfImageSel),stringImagePath);
                            // Writing data to SharedPreferences
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfUID), UID);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfGender), stringGenderSharPrf);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfEmail), stringEmailSharPref);
                            if (!stringMobileNoProfile.isEmpty() && !stringMobileNoProfile.equals("null")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), stringMobileNoProfile);
                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), null);

                            //  editor.putString("ImageProfile", stringImageNameProf);
                            if (!stringDateOfBirth.isEmpty() && !stringDateOfBirth.equals("0000-00-00")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), stringDateOfBirth);

                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), null);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCity), stringCityNameSharePrf);
                            String[] stringsName = object1.getString(nameKey).split("\\s");
                            try {
                                String stringFirstNameSHarPrf = stringsName[0];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), stringFirstNameSHarPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), null);
                            }
                            try {
                                String stringLastNameSharPrf = stringsName[1];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), stringLastNameSharPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), null);

                            }
                           verifyStoragePermissions(activity);
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, stringMSG, Toast.LENGTH_SHORT).show();
                                }
                            });
                            String fbName= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbName));
                            String fbEmail= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbEmail));
                            String fbGender= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbGender));
                            // String fbDOB= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbDob));
                            String fbCity= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbCity));
                            if(fbCity.isEmpty()||fbEmail.isEmpty()||fbGender.isEmpty()||fbName.isEmpty()){
                                DismissDialog();
                                MainSignInActivity.CallFillBlankSignUpDialog(activity);
                            }else {
                                DismissDialog();
                                HashMap<String,String> param = new HashMap<>();
                                param.put(activity.getString(R.string.serviceKeyName),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbName)));
                                param.put(activity.getString(R.string.serviceKeyEmail),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbEmail)));
                                param.put(activity.getString(R.string.serviceKeyGender),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbGender)));
                                param.put(activity.getString(R.string.serviceKeyDOB),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbDob)));
                                param.put(activity.getString(R.string.serviceKeyFacebookId),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbId)));
                                param.put(activity.getString(R.string.serviceKeyProfileImage),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbPrfImg)));
                                CallVolleyServices(param,activity.getString(R.string.facebookURL),"");
                            }


                        }
                    }catch (Exception e){
                      //  Log.d("error",e.getMessage());
                    }
                }else if (stringURL.equals(activity.getString(R.string.loginURL))) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String stringSuccess = object.getString(successKey);
                        final String stringMSG = object.getString(msgKey);
                        if(stringSuccess.equals(succValueKey)) {
                            String profile = object.getString(profileKey);
                            JSONObject object1 = new JSONObject(profile);
                            String stringGenderSharPrf = object1.getString(genderKey);
                            String stringEmailSharPref = object1.getString(emailKey);
                            String stringMobileNoProfile = object1.getString(mobNoKey);
                            String stringDateOfBirth = object1.getString(dobKey);
                            String stringCityNameSharePrf = object1.getString(cityKey);
                            String stringStateName = object1.getString(stateNamekey);
                            String stringImagePath = object1.getString(profileImgPathKey);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfImageSel),stringImagePath);
                            if(stringStateName.isEmpty()||stringStateName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),stringStateName);
                            String UID = object1.getString(uidKey);
                            // Writing data to SharedPreferences

                            String stringCityName = object1.getString(cityNameKey);
                            if(stringCityName.isEmpty()||stringCityName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),stringCityName);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfUID), UID);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfGender), stringGenderSharPrf);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfEmail), stringEmailSharPref);
                            if (!stringMobileNoProfile.isEmpty() && !stringMobileNoProfile.equals("null")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), stringMobileNoProfile);
                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), null);

                            //  editor.putString("ImageProfile", stringImageNameProf);
                            if (!stringDateOfBirth.isEmpty() && !stringDateOfBirth.equals("0000-00-00")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), stringDateOfBirth);

                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), null);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCity), stringCityNameSharePrf);
                            String[] stringsName = object1.getString(nameKey).split("\\s");
                            try {
                                String stringFirstNameSHarPrf = stringsName[0];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), stringFirstNameSHarPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), null);
                            }
                            try {
                                String stringLastNameSharPrf = stringsName[1];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), stringLastNameSharPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), null);

                            }
                          verifyStoragePermissions(activity);

                            //   spinner.setVisibility(View.GONE);
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, stringMSG, Toast.LENGTH_SHORT).show();
                                }
                            });



                        }



                      //  DismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // DismissDialog();
                    }

                }else if(stringURL.equals(activity.getString(R.string.signUpURL))){
                    try {
                        JSONObject object = new JSONObject(response);
                        String stringSuccess = object.getString(successKey);
                        final String stringMSG = object.getString(msgKey);
                        if(stringSuccess.equals(succValueKey)) {
                            String profile = object.getString(profileKey);
                            JSONObject object1 = new JSONObject(profile);
                            String stringGenderSharPrf = object1.getString(genderKey);
                            String stringEmailSharPref = object1.getString(emailKey);
                            String stringMobileNoProfile = object1.getString(mobNoKey);
                            String stringDateOfBirth = object1.getString(dobKey);
                            String stringCityNameSharePrf = object1.getString(cityKey);
                            String stringStateName = object1.getString(stateNamekey);
                            String stringImagePath = object1.getString(profileImgPathKey);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfImageSel),stringImagePath);
                            if(stringStateName.isEmpty()||stringStateName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),stringStateName);
                            String UID = object1.getString(uidKey);
                            // Writing data to SharedPreferences

                            String stringCityName = object1.getString(cityNameKey);
                            if(stringCityName.isEmpty()||stringCityName.equals("null")){
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),null);
                            }else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),stringCityName);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfUID), UID);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfGender), stringGenderSharPrf);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfEmail), stringEmailSharPref);
                            if (!stringMobileNoProfile.isEmpty() && !stringMobileNoProfile.equals("null")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), stringMobileNoProfile);
                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), null);

                            //  editor.putString("ImageProfile", stringImageNameProf);
                            if (!stringDateOfBirth.isEmpty() && !stringDateOfBirth.equals("0000-00-00")) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), stringDateOfBirth);

                            } else
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), null);
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCity), stringCityNameSharePrf);
                            String[] stringsName = object1.getString(nameKey).split("\\s");
                            try {
                                String stringFirstNameSHarPrf = stringsName[0];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), stringFirstNameSHarPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), null);
                            }
                            try {
                                String stringLastNameSharPrf = stringsName[1];
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), stringLastNameSharPrf);
                            } catch (Exception e) {
                                sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), null);

                            }
                            verifyStoragePermissions(activity);

                            //   spinner.setVisibility(View.GONE);
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, stringMSG, Toast.LENGTH_SHORT).show();
                                }
                            });



                        }



                        //  DismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // DismissDialog();
                    }
                }else if(stringURL.equals(activity.getString(R.string.passwordChangeURL))){
                    try {
                        JSONObject jsonObjectPassRes = new JSONObject(response);
                        String stringSuccessPassChange = jsonObjectPassRes.getString(successKey);
                        final String msg = jsonObjectPassRes.getString(msgKey);
                        if (stringSuccessPassChange.equals(succValueKey)) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                    activity.finish();
                                }
                            });


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                       // DismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  DismissDialog();
                    }
                }else if(stringURL.equals(activity.getString(R.string.profileUpdateURL))){
                    try {
                        JSONObject jsonObjectUpdateRes = new JSONObject(response);
                        String stringSuccessPassChange =jsonObjectUpdateRes.getString(successKey);
                        final String msg = jsonObjectUpdateRes.getString(msgKey);

                       // String jsonArray = jsonObjectUpdateRes.getString(arrayListKey);
                        JSONObject jsonObjectProfile = jsonObjectUpdateRes.getJSONObject(profileKey);
                        String stringGenderSharPrf = jsonObjectProfile.getString(genderKey);
                        String stringEmailSharPref = jsonObjectProfile.getString(emailKey);
                        String stringMobileNoProfile = jsonObjectProfile.getString(mobNoKey);
                        String stringDateOfBirth = jsonObjectProfile.getString(dobKey);
                        String stringCityNameSharePrf = jsonObjectProfile.getString(cityKey);
                        String imagePathProf = jsonObjectProfile.getString(profileImgPathKey);
                        String stringCityName = jsonObjectProfile.getString(cityNameKey);
                        if(stringCityName.isEmpty()||stringCityName.equals("null")){
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),null);
                        }else
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCityName),stringCityName);
                        String stringStateName = jsonObjectProfile.getString(stateNamekey);
                        if(stringStateName.isEmpty()||stringStateName.equals("null")){
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),null);
                        }else
                        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfStateName),stringStateName);
                        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfImageSel),imagePathProf);
                        // Writing data to SharedPreferences
                        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfGender), stringGenderSharPrf);
                        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfEmail), stringEmailSharPref);
                        if (!stringMobileNoProfile.isEmpty() && !stringMobileNoProfile.equals("null")) {
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), stringMobileNoProfile);
                        } else
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfMobNo), null);


                        if (!stringDateOfBirth.isEmpty() && !stringDateOfBirth.equals("0000-00-00")) {
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), stringDateOfBirth);
                            // Toast.makeText(MainSignInActivity.this, stringDateOfBirth, Toast.LENGTH_SHORT).show();
                        } else
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfDOB), null);
                        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfCity), stringCityNameSharePrf);
                        String[] stringsName = jsonObjectProfile.getString(nameKey).split("\\s");
                        try {
                            String stringFirstNameSHarPrf = stringsName[0];
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), stringFirstNameSHarPrf);
                        } catch (Exception e) {
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFrstName), null);
                        }
                        try {
                            String stringLastNameSharPrf = stringsName[1];
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), stringLastNameSharPrf);
                        } catch (Exception e) {
                            sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfLstName), null);

                        }
                        if(Integer.parseInt(stringSuccessPassChange)==1){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                    activity.finish();
                                }
                            });


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                      //  DismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // DismissDialog();

                    }
                }else if(stringURL.equals(activity.getString(R.string.homePageURL))&&key.equals("")){
                    SingltonClsAll.reset();
                    SingltonClsVisited.reset();
                    SingltonClsStory.reset();
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONObject jsonObjectPlaces = object.getJSONObject(placeKey);
                        String stringSuccess = jsonObjectPlaces.getString(successKey);

                        //  final String stringMsg = jsonObjectPlaces.getString("message");
                        if(stringSuccess.equals(succValueKey)) {
                            JSONArray jsonArrayPlace = jsonObjectPlaces.getJSONArray(arrayListKey);
                            for(int i =0 ; i<jsonArrayPlace.length();i++) {

                                JSONObject jsonObject = jsonArrayPlace.getJSONObject(i);
                                FeedItemAll feedItemAll = new FeedItemAll();
                                feedItemAll.setStringImagePath(jsonObject.getString(imgPathkey));
                                feedItemAll.setStringCityName(jsonObject.getString(cityNameKey));
                                feedItemAll.setStringCityID(jsonObject.getString(cityKey));
                                feedItemAll.setStringPlaceID(jsonObject.getString(placeIdKey));
                                feedItemAll.setStringPlaceName(jsonObject.getString(placeNameKey));
                                feedItemAll.setStringInfo(jsonObject.getString(infoKey));
                                feedItemAll.setStringSpotId(jsonObject.getString(spotIdKey));
                                feedItemAll.setStringImageName(jsonObject.getString(imgNameKey));
                                String isFav = jsonObject.getString(favBoolKey);
                                String isVisited = jsonObject.getString(visitedBoolKey);
                                if(isFav.isEmpty()||isFav.equals("null"))
                                feedItemAll.setaBooleanFavorite(false);
                                else
                                feedItemAll.setaBooleanFavorite(true);
                                if(isVisited.isEmpty()||isVisited.equals("null"))
                                feedItemAll.setaBooleanVisited(false);
                                else
                                feedItemAll.setaBooleanVisited(true);


                                SingltonClsAll.getInstance().arrayListAll.add(feedItemAll);
                            }
                            AllFragment.AllFragment(activity);
                          //  DismissDialog();

                        }else{
                          //  DismissDialog();

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //     Toast.makeText(activity,stringMsg,Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  Log.d("errormsg",e.getMessage());
                     //   DismissDialog();
                    }
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject jsonObjectStory = object.getJSONObject(storyKey);
                        String stringSuccess = jsonObjectStory.getString(successKey);

                        if(stringSuccess.equals(succValueKey)) {
                            JSONArray jsonArray = jsonObjectStory.getJSONArray(arrayListKey);
                            // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                            for(int i =0 ; i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FeedItemStories feedItemStories = new FeedItemStories();
                                feedItemStories.setStringImagePath(jsonObject.getString(imgPathkey));
                                feedItemStories.setStringCityName(jsonObject.getString(cityNameKey));
                                feedItemStories.setStringInfo(jsonObject.getString(commentKey));
                                feedItemStories.setStringImageName(jsonObject.getString(nameKey));
                                feedItemStories.setStringStatus(jsonObject.getString(activity.getString(R.string.serviceKeyStatus)));
                                feedItemStories.setStringSpotId(jsonObject.getString(spotIdKey));
                                feedItemStories.setStringSpotName(jsonObject.getString(spotNameKey));
                           //     feedItemStories.setStringCityID(jsonObject.getString(cityKey));
                            //    feedItemStories.setStringPlaceID(jsonObject.getString(placeIdKey));
                                feedItemStories.setStringPrfImgPath(jsonObject.getString(activity.getString(R.string.serviceKeyProfileImagePath)));
                                feedItemStories.setStringPlaceName(jsonObject.getString(placeNameKey));
                                SingltonClsStory.getInstance().arrayListStory.add(feedItemStories);
                            }
                            StoriesFragment.StoriesFragment();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  DismissDialog();

                    }



                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject jsonObjectVisited = object.getJSONObject(visitedKey);
                        String stringSuccess = jsonObjectVisited.getString(successKey);

                        if(stringSuccess.equals(succValueKey)){
                            JSONArray jsonArray = jsonObjectVisited.getJSONArray(arrayListKey);
                            // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                            for(int i =0 ; i<jsonArray.length();i++) {
                                // String stringFavorite, stringVisited;

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FeedItemVisited feedItemVisited = new FeedItemVisited();
                                feedItemVisited.setStringImagePath(jsonObject.getString(imgPathkey));
                                feedItemVisited.setStringCityName(jsonObject.getString(cityNameKey));
                                feedItemVisited.setStringSpotId(jsonObject.getString(spotIdKey));
                                feedItemVisited.setStringSpotName(jsonObject.getString(spotNameKey));

                                feedItemVisited.setStringPlaceName(jsonObject.getString(placeNameKey));



                                SingltonClsVisited.getInstance().arrayListVisited.add(feedItemVisited);
                            }
                            VisitedFragment.VisitedFragment();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else if(stringURL.equals(activity.getString(R.string.homePageURL))){
                    if(key.equals("AllHome")) {

                        int x = Integer.parseInt(activity.getString(R.string.contentRefreshCount));
                        int j = AllFragment.feedItemsAll.size() / x;
                        if(j==0)
                            SingltonClsAll.reset();
                        SingltonClsVisited.reset();
                        SingltonClsStory.reset();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObjectPlaces = object.getJSONObject(placeKey);
                            String stringSuccess = jsonObjectPlaces.getString(successKey);

                            //  final String stringMsg = jsonObjectPlaces.getString("message");
                            if (stringSuccess.equals(succValueKey)) {
                                JSONArray jsonArrayPlace = jsonObjectPlaces.getJSONArray(arrayListKey);
                                for (int i = 0; i < jsonArrayPlace.length(); i++) {

                                    JSONObject jsonObject = jsonArrayPlace.getJSONObject(i);
                                    FeedItemAll feedItemAll = new FeedItemAll();
                                    feedItemAll.setStringImagePath(jsonObject.getString(imgPathkey));
                                    feedItemAll.setStringCityName(jsonObject.getString(cityNameKey));
                                    feedItemAll.setStringCityID(jsonObject.getString(cityKey));
                                    feedItemAll.setStringPlaceID(jsonObject.getString(placeIdKey));
                                    feedItemAll.setStringPlaceName(jsonObject.getString(placeNameKey));
                                    feedItemAll.setStringInfo(jsonObject.getString(infoKey));
                                    feedItemAll.setStringSpotId(jsonObject.getString(spotIdKey));
                                    feedItemAll.setStringImageName(jsonObject.getString(imgNameKey));
                                    String isFav = jsonObject.getString(favBoolKey);
                                    String isVisited = jsonObject.getString(visitedBoolKey);
                                    if(isFav.isEmpty()||isFav.equals("null"))
                                        feedItemAll.setaBooleanFavorite(false);
                                    else
                                        feedItemAll.setaBooleanFavorite(true);
                                    if(isVisited.isEmpty()||isVisited.equals("null"))
                                        feedItemAll.setaBooleanVisited(false);
                                    else
                                        feedItemAll.setaBooleanVisited(true);


                                    SingltonClsAll.getInstance().arrayListAll.add(feedItemAll);
                                }
                                AllFragment.AllFragment(activity);
                                //  DismissDialog();

                            } else {
                                //  DismissDialog();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //     Toast.makeText(activity,stringMsg,Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                         //   Log.d("errormsg", e.getMessage());
                            //   DismissDialog();
                        }
                    }
                    if(key.equals("StoryHome")) {
                        int x = Integer.parseInt(activity.getString(R.string.contentRefreshCount));
                        int j = StoriesFragment.feedItemStoriesList.size() / x;
                        if(j==0)
                            SingltonClsStory.reset();
                        SingltonClsAll.reset();
                        SingltonClsVisited.reset();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObjectStory = object.getJSONObject(storyKey);
                            String stringSuccess = jsonObjectStory.getString(successKey);

                            if (stringSuccess.equals(succValueKey)) {
                                JSONArray jsonArray = jsonObjectStory.getJSONArray(arrayListKey);
                                // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    FeedItemStories feedItemStories = new FeedItemStories();
                                    feedItemStories.setStringImagePath(jsonObject.getString(imgPathkey));
                                    feedItemStories.setStringCityName(jsonObject.getString(cityNameKey));
                                    feedItemStories.setStringInfo(jsonObject.getString(commentKey));
                                    feedItemStories.setStringImageName(jsonObject.getString(nameKey));
                                    feedItemStories.setStringStatus(jsonObject.getString(activity.getString(R.string.serviceKeyStatus)));
                                    feedItemStories.setStringSpotId(jsonObject.getString(spotIdKey));
                                    feedItemStories.setStringSpotName(jsonObject.getString(spotNameKey));
//                                    feedItemStories.setStringCityID(jsonObject.getString(cityKey));
 //                                   feedItemStories.setStringPlaceID(jsonObject.getString(placeIdKey));
                                    feedItemStories.setStringPrfImgPath(jsonObject.getString(activity.getString(R.string.serviceKeyProfileImagePath)));
                                   feedItemStories.setStringPlaceName(jsonObject.getString(placeNameKey));
                                    SingltonClsStory.getInstance().arrayListStory.add(feedItemStories);
                                }
                           //     StoriesFragment.StoriesFragment();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  DismissDialog();

                        }
                    }
                    if(key.equals("VisitedHome")) {

                        int x = Integer.parseInt(activity.getString(R.string.contentRefreshCount));
                        int j = VisitedFragment.feedItemVisitedList.size() / x;
                        if(j==0)
                            SingltonClsVisited.reset();

                        SingltonClsAll.reset();
                        SingltonClsStory.reset();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObjectVisited = object.getJSONObject(visitedKey);
                            String stringSuccess = jsonObjectVisited.getString(successKey);

                            if (stringSuccess.equals(succValueKey)) {
                                JSONArray jsonArray = jsonObjectVisited.getJSONArray(arrayListKey);
                                // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    // String stringFavorite, stringVisited;

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    FeedItemVisited feedItemVisited = new FeedItemVisited();
                                    feedItemVisited.setStringImagePath(jsonObject.getString(imgPathkey));
                                    feedItemVisited.setStringCityName(jsonObject.getString(cityNameKey));
                                    feedItemVisited.setStringPlaceName(jsonObject.getString(placeNameKey));

                                    feedItemVisited.setStringSpotId(jsonObject.getString(spotIdKey));
                                    feedItemVisited.setStringSpotName(jsonObject.getString(spotNameKey));

                                    SingltonClsVisited.getInstance().arrayListVisited.add(feedItemVisited);
                                }
                                VisitedFragment.VisitedFragment();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }else if(stringURL.equals(activity.getString(R.string.favoritePageURL))&&key.equals("")){
                    int x = Integer.parseInt(activity.getString(R.string.contentRefreshCount));
                    int j = feedItemsFav.size() / x;
                    if(j==0)
                        FavoriteSingletonCls.reset();
                    try {

                        JSONObject object = new JSONObject(response);
                    //    JSONObject jsonObjectPlaces = object.getJSONObject();
                        String stringSuccess = object.getString(successKey);

                        //  final String stringMsg = jsonObjectPlaces.getString("message");
                        if (stringSuccess.equals(succValueKey)) {
                            JSONArray jsonArrayPlace = object.getJSONArray(activity.getString(R.string.serviceKeyFavourite));
                            for (int i = 0; i < jsonArrayPlace.length(); i++) {

                                JSONObject jsonObject = jsonArrayPlace.getJSONObject(i);
                                FeedItemAll feedItemAll = new FeedItemAll();
                                feedItemAll.setStringImagePath(jsonObject.getString(imgPathkey));
                                feedItemAll.setStringCityName(jsonObject.getString(cityNameKey));
                                feedItemAll.setStringCityID(jsonObject.getString(cityKey));
                                feedItemAll.setStringPlaceID(jsonObject.getString(placeIdKey));
                                feedItemAll.setStringSpotId(jsonObject.getString(spotIdKey));
                                feedItemAll.setStringPlaceName(jsonObject.getString(placeNameKey));
                                feedItemAll.setStringInfo(jsonObject.getString(infoKey));
                                feedItemAll.setStringImageName(jsonObject.getString(imgNameKey));
                                String isVisited = jsonObject.getString(visitedBoolKey);

                                if(isVisited.isEmpty()||isVisited.equals("null"))
                                    feedItemAll.setaBooleanVisited(false);
                                else
                                    feedItemAll.setaBooleanVisited(true);
                                FavoriteSingletonCls.getInstance().arrayListFav.add(feedItemAll);

                            }
                            MainActivityFavorite.Favorite();
                            //  DismissDialog();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                      //  Log.d("errormsg", e.getMessage());
                        //   DismissDialog();
                    }


                }else if(stringURL.equals(activity.getString(R.string.stateURL))&&key.equals("SignUp")){
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString(successKey);
                        String value = object.getString(activity.getString(R.string.serviceKeyStateValue));
                        if(success.equals(succValueKey)) {
                            if (value.equals("1")) {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateState));
                                MainActivitySignUp.hashMapState = new HashMap<>();
                                MainActivitySignUp.arrayListState = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainActivitySignUp.hashMapState.put(object1.getString(stateNamekey), object1.getString(stateIdkey));
                                    MainActivitySignUp.arrayListState.add(i, object1.getString(stateNamekey));

                                }
                                MainActivitySignUp.adapterStateNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainActivitySignUp.arrayListState);
                                MainActivitySignUp.autoCompleteTextViewStateSignUp.setAdapter(MainActivitySignUp.adapterStateNames);

                            }else {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateCity));
                                MainActivitySignUp.hashMapCity = new HashMap<>();
                                MainActivitySignUp.arrayListCity = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainActivitySignUp.hashMapCity.put(object1.getString(stateCityNameKey), object1.getString(cityKey));
                                    MainActivitySignUp.arrayListCity.add(i, object1.getString(stateCityNameKey));

                                }
                                MainActivitySignUp.adapterCityNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainActivitySignUp.arrayListCity);
                                MainActivitySignUp.autoCompleteTextViewCitySignUp.setAdapter(MainActivitySignUp.adapterCityNames);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(stringURL.equals(activity.getString(R.string.stateURL))&&key.equals("LogIn")){
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString(successKey);
                        String value = object.getString(activity.getString(R.string.serviceKeyStateValue));
                        if(success.equals(succValueKey)) {
                            if (value.equals("1")) {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateState));
                                MainSignInActivity.hashMapState = new HashMap<>();
                                MainSignInActivity.arrayListState = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainSignInActivity.hashMapState.put(object1.getString(stateNamekey), object1.getString(stateIdkey));
                                    MainSignInActivity.arrayListState.add(i, object1.getString(stateNamekey));

                                }
                                MainSignInActivity.adapterStateNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainSignInActivity.arrayListState);
                                MainSignInActivity.autoCompleteTextViewStateSignUp.setAdapter(MainSignInActivity.adapterStateNames);

                            }else {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateCity));
                                MainSignInActivity.hashMapCity = new HashMap<>();
                                MainSignInActivity.arrayListCity = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainSignInActivity.hashMapCity.put(object1.getString(stateCityNameKey), object1.getString(cityKey));
                                    MainSignInActivity.arrayListCity.add(i, object1.getString(stateCityNameKey));

                                }
                                MainSignInActivity.adapterCityNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainSignInActivity.arrayListCity);
                                MainSignInActivity.autoCompleteTextViewCitySignUp.setAdapter(MainSignInActivity.adapterCityNames);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else if(stringURL.equals(activity.getString(R.string.stateURL))&&key.equals("Profile")){
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString(successKey);
                        String value = object.getString(activity.getString(R.string.serviceKeyStateValue));
                        if(success.equals(succValueKey)) {
                            if (value.equals("1")) {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateState));
                                MainProfileEditActivity.hashMapState = new HashMap<>();
                                MainProfileEditActivity.arrayListState = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainProfileEditActivity.hashMapState.put(object1.getString(stateNamekey), object1.getString(stateIdkey));
                                    MainProfileEditActivity.arrayListState.add(i, object1.getString(stateNamekey));

                                }
                                MainProfileEditActivity.adapterStateNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainProfileEditActivity.arrayListState);
                                MainProfileEditActivity.autoCompleteTextViewStateNameProfile.setAdapter(MainProfileEditActivity.adapterStateNames);
                                final String s =sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfStateName));
                                if(MainProfileEditActivity.arrayListState.contains(s)){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainProfileEditActivity.autoCompleteTextViewStateNameProfile.setText(s);
                                        }
                                    });


                                    HashMap<String,String> parameters = new HashMap<>();
                                    parameters.put(activity.getString(R.string.serviceKeyStateId),MainProfileEditActivity.hashMapState.get(activity.getString(R.string.sharPrfStateName)));
                                    CallVolleyServices(parameters,activity.getString(R.string.stateURL),"Profile");

                                }
                            }else {
                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateCity));
                                MainProfileEditActivity.hashMapCity = new HashMap<>();
                                MainProfileEditActivity.arrayListCity = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainProfileEditActivity.hashMapCity.put(object1.getString(stateCityNameKey), object1.getString(cityKey));
                                    MainProfileEditActivity.arrayListCity.add(i, object1.getString(stateCityNameKey));

                                }
                                MainProfileEditActivity.adapterCityNames = new ArrayAdapter<>(activity,android.R.layout.simple_dropdown_item_1line, MainProfileEditActivity.arrayListCity);
                                MainProfileEditActivity.autoCompleteTextViewCityNameProfile.setAdapter(MainProfileEditActivity.adapterCityNames);
                                final String c =sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfCityName));
                                if(MainProfileEditActivity.arrayListCity.contains(c)){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainProfileEditActivity.autoCompleteTextViewCityNameProfile.setText(c);
                                        }
                                    });

                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(stringURL.equals(activity.getString(R.string.yourStoryURL))){
                    int x = Integer.parseInt(activity.getString(R.string.contentRefreshCount));
                    int j = feedItemsYourStory.size() / x;
                    if(j==0)
                        YourStorySingltonCls.reset();
                    try {
                        JSONObject object = new JSONObject(response);
                      //  JSONObject jsonObjectStory = object.getJSONObject();
                        String stringSuccess = object.getString(successKey);

                        if (stringSuccess.equals(succValueKey)) {
                            JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyYourStory));
                            // JSONArray jsonArrayPlace = object.getJSONArray("Places");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FeedItemStories feedItemStories = new FeedItemStories();
                                feedItemStories.setStringImagePath(jsonObject.getString(activity.getString(R.string.serviceKeyProfileImage)));
                                feedItemStories.setStringCityName(jsonObject.getString(cityNameKey));
                                feedItemStories.setStringInfo(jsonObject.getString(commentKey));
                                feedItemStories.setStringStatus(jsonObject.getString(activity.getString(R.string.serviceKeyStatus)));
//                                    feedItemStories.setStringCityID(jsonObject.getString(cityKey));
                                //                                   feedItemStories.setStringPlaceID(jsonObject.getString(placeIdKey));
                               // feedItemStories.setStringPrfImgPath(jsonObject.getString(activity.getString(R.string.serviceKeyProfileImagePath)));
                                feedItemStories.setStringCityID("");
                                feedItemStories.setStringPlaceID("");
                                feedItemStories.setStringPlaceName(jsonObject.getString(placeNameKey));
                                YourStorySingltonCls.getInstance().arrayListYourStory.add(feedItemStories);
                            }
                            MainActivityYourStoryPage.YourStories(activity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  DismissDialog();

                    }
                }else if(stringURL.equals(activity.getString(R.string.storySpot))&&key.equals("")){
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString(successKey);
                        String value = object.getString(activity.getString(R.string.serviceKeyStateValue));
                        if(success.equals(succValueKey)) {
                            switch (value) {
                                case "1": {
                                    JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateCity));
                                    MainActivityYourStories.hashMapCityId = new HashMap<>();
                                    MainActivityYourStories.arrayListCity = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        MainActivityYourStories.hashMapCityId.put(object1.getString(cityNameKey), object1.getString(cityKey));
                                        MainActivityYourStories.arrayListCity.add(i, object1.getString(cityNameKey));

                                    }
                                    MainActivityYourStories.adapterCity = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, MainActivityYourStories.arrayListCity);
                                    MainActivityYourStories.autoCompleteTextViewCity.setAdapter(MainActivityYourStories.adapterCity);


                                    break;
                                }
                                case "2": {
                                    {
                                        JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyPlace));
                                        MainActivityYourStories.hashMapPlaceId = new HashMap<>();
                                        MainActivityYourStories.arrayListPlace = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);
                                            MainActivityYourStories.hashMapPlaceId.put(object1.getString(placeNameKey), object1.getString(placeIdKey));
                                            MainActivityYourStories.arrayListPlace.add(i, object1.getString(placeNameKey));

                                        }
                                        MainActivityYourStories.adapterPlace = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, MainActivityYourStories.arrayListPlace);
                                        MainActivityYourStories.autoCompletetextViewPlace.setAdapter(MainActivityYourStories.adapterPlace);

                                    }
                                    break;
                                }
                                default: {
                                    JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyYourStorySpot));
                                    MainActivityYourStories.hashMapSpotId = new HashMap<>();
                                    MainActivityYourStories.arrayListSpot = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        MainActivityYourStories.hashMapSpotId.put(object1.getString(activity.getString(R.string.serviceKeySpotName)), object1.getString(spotIdKey));
                                        MainActivityYourStories.arrayListSpot.add(i, object1.getString(activity.getString(R.string.serviceKeySpotName)));

                                    }
                                    MainActivityYourStories.adapterSpot = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, MainActivityYourStories.arrayListSpot);
                                    MainActivityYourStories.autoCompletetextViewSpot.setAdapter(MainActivityYourStories.adapterSpot);

                                    break;
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(stringURL.equals(activity.getString(R.string.cityURL))&&key.equals("Home")){
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString(successKey);
                       // String value = object.getString(activity.getString(R.string.serviceKeyStateValue));
                        if (success.equals(succValueKey)) {

                                JSONArray jsonArray = object.getJSONArray(activity.getString(R.string.serviceKeyStateCity));
                                MainHomeScreenActivity.hashMapActiveCity = new HashMap<>();
                                MainHomeScreenActivity.arrayListActiveCity = new ArrayList<>();
                                MainHomeScreenActivity.hashMapAcivCtyLongi = new HashMap<>();
                                MainHomeScreenActivity.hashMapActvCityName = new HashMap<>();
                                MainHomeScreenActivity.hashMapAcivCtyLati = new HashMap<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    MainHomeScreenActivity.hashMapActvCityName.put(object1.getString(cityKey), object1.getString(stateCityNameKey));
                                    MainHomeScreenActivity.hashMapActiveCity.put(object1.getString(stateCityNameKey), object1.getString(cityKey));
                                    MainHomeScreenActivity.arrayListActiveCity.add(i, object1.getString(stateCityNameKey));
                                    MainHomeScreenActivity.hashMapAcivCtyLati.put(object1.getString(cityKey),object1.getString(stringAtvCityLati));
                                    MainHomeScreenActivity.hashMapAcivCtyLongi.put(object1.getString(cityKey),object1.getString(stringAtvCityLongi));


                                }


                                if(!MainHomeScreenActivity.arrayListActiveCity.isEmpty()){
                                    String city = MainHomeScreenActivity.hashMapActiveCity.get(MainHomeScreenActivity.arrayListActiveCity.get(0));
                                   MainHomeScreenActivity.CallHomeServiceCityPass(activity,city);
                                    MainHomeScreenActivity.adapter.notifyDataSetChanged();



                                }else {

                                    HashMap<String,String> para = new HashMap<>();
                                    CallVolleyServices(para,activity.getString(R.string.cityURL),"Home");
                                }

                            }

                    }catch (Exception e){
                     //   Log.d("error",e.getMessage());
                    }
                }else if(stringURL.equals(activity.getString(R.string.shareStoryURL))){
                    try {
                        JSONObject jsonObjectPassRes = new JSONObject(response);
                        String stringSuccessPassChange = jsonObjectPassRes.getString(successKey);
                        final String msg = jsonObjectPassRes.getString(msgKey);
                        if (stringSuccessPassChange.equals(succValueKey)) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                    activity.finish();
                                }
                            });


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        // DismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  DismissDialog();
                    }
                }else if(stringURL.equals("version.php")){
                    try {
                    JSONObject jsonObjectPassRes = new JSONObject(response);
                    String stringSuccessPassChange = jsonObjectPassRes.getString(successKey);
                    if (stringSuccessPassChange.equals(succValueKey)) {

                        MainActivitySplashScreen.showUpdateDialog(activity);
                    }else if(stringSuccessPassChange.equals("2")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                        ("market://details?id=com.whitebirdtechnology.treepr")));
                            }
                        });
                    }
                    }catch (Exception e){
                       // Log.d("error",e.getMessage());
                    }
                }
                DismissDialog();
            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DismissDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;


            }
        };requestQueue.add(stringRequest);
    }

    private void DismissDialog(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialogs.isShowing())
                progressDialogs.dismiss();
            }
        });
    }
}
