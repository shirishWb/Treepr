package com.whitebirdtechnology.treepr.Home_Page;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.AboutPage.MainActivityAbout;
import com.whitebirdtechnology.treepr.Favourite.MainActivityFavorite;
import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.ProfileUpdate.MainProfileEditActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.YourStory.MainActivityYourStoryPage;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainHomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharePreferences sharePreferences;
    FragmentManager mFragmentManager;
    Dialog alertDialogDismiss;
    FragmentTransaction mFragmentTransaction;
    static Menu menus;
    VolleyServices volleyServices;
    Dialog dialogDis;
    public static Spinner spinner;
    public static ListView modeList;
    public static CityAdepter adapter;
    public static HashMap<String,String> hashMapActiveCity,hashMapAcivCtyLati,hashMapAcivCtyLongi,hashMapActvCityName;
    public static ArrayList<String> arrayListActiveCity;
    public static int itemAll,itemStory,itemVisited;
    public static TextView textViewUserName,textViewUserEmail;
    public static ImageView imageViewUserProfile;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        setTitle(WidthOfScreen()+"Home");
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_home_screen);

        AllFragment.feedItemsAll = new ArrayList<>();
        StoriesFragment.feedItemStoriesList = new ArrayList<>();
        VisitedFragment.feedItemVisitedList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayListActiveCity = new ArrayList<>();
        hashMapActiveCity = new HashMap<>();
        hashMapAcivCtyLati = new HashMap<>();
        hashMapAcivCtyLongi = new HashMap<>();
        hashMapActvCityName = new HashMap<>();
        itemAll =0;
        itemStory = 0;
        itemVisited=0;
        volleyServices = new VolleyServices(this);

       // stringURL = sharePreferences.getDataFromSharePref("URL")+stringURL;
        sharePreferences = new SharePreferences(this);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
                if(UID.isEmpty()||UID.equals("0")) {
                    Toast.makeText(MainHomeScreenActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                    return;
                }
                selectImage();
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
              //  Toast.makeText(MainHomeScreenActivity.this,"float",Toast.LENGTH_LONG).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       // new SendTask(params,this,stringURL).execute();

       // new SendTask("0",this).execute();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menus = navigationView.getMenu();
        String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        if(UID.isEmpty()||UID.equals("0")){
            MenuItem item = menus.findItem(R.id.nav_log_out);
            item.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        textViewUserName = (TextView)view.findViewById(R.id.textViewUserName);
        textViewUserEmail = (TextView)view.findViewById(R.id.textViewUserEmail);
        imageViewUserProfile = (ImageView)view.findViewById(R.id.imageViewUserProfile);
        final String stringImageURL = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfImageSel));
        if(!stringImageURL.isEmpty()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    new ImageDownloaderTask(imageViewUserProfile, stringImageURL, MainHomeScreenActivity.this).execute();

                }
            });
            thread.start();
        }
        String stringEmail = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfEmail));
        String stringFirstName = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfFrstName));
        String stringLastName = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfLstName));
        if(stringFirstName.isEmpty())
            stringFirstName = "Guest User";
        textViewUserEmail.setText(stringEmail);
        textViewUserName.setText(stringFirstName+" "+stringLastName);
        HashMap<String,String> para = new HashMap<>();
        volleyServices.CallVolleyServices(para,getString(R.string.cityURL),"Home");


        // Reading from SharedPreferences

      //  Toast.makeText(MainHomeScreenActivity.this,UID,Toast.LENGTH_SHORT).show();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
    }
    public String WidthOfScreen(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        String space = "";
        for(int i=0;i<width/(width*0.065);i++){
            space+=" ";
        }
        return space;
    }

    public static void CallHomeServiceCityPass(Activity activity,String cityId){
        MenuItem item = menus.findItem(R.id.spinner);
        if(!hashMapActvCityName.get(cityId).equals("null"))
        item.setTitle(hashMapActvCityName.get(cityId)+" "+activity.getString(R.string.down_arrow));
        else
            item.setTitle("City"+" "+activity.getString(R.string.down_arrow));
        HashMap<String,String> params = new HashMap<>();
        String TREEEPRDATA = "TreeprData";
        String stringCity;
        SharePreferences sharePreferences = new SharePreferences(activity);
        VolleyServices volleyServices = new VolleyServices(activity);

        String stringURL,UID;
        stringURL = activity.getString(R.string.homePageURL);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(TREEEPRDATA,MODE_PRIVATE);
        SingltonClsAll.reset();
        SingltonClsVisited.reset();
        SingltonClsStory.reset();
        stringCity = cityId;
        UID = sharedPreferences.getString(activity.getString(R.string.sharPrfUID),"");

        if(UID.isEmpty()) {
            params.put(activity.getString(R.string.serviceKeyCity), stringCity);
            params.put(activity.getString(R.string.serviceKeyUID),"0");
            UID = "0";
        }else {
            params.put(activity.getString(R.string.serviceKeyUID), UID);
            params.put(activity.getString(R.string.serviceKeyCity), stringCity);
        }
        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfActiveCity),stringCity);
        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfActiveCityLati),hashMapAcivCtyLati.get(stringCity));
        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfActiveCityLongi),hashMapAcivCtyLongi.get(stringCity));
        sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfUID),UID);
        params.put(activity.getString(R.string.serviceKeyItem), String.valueOf(0));
        params.put(activity.getString(R.string.serviceKeyItem1),String.valueOf(0));
        params.put(activity.getString(R.string.serviceKeyItem2),String.valueOf(0));
        itemAll = 0;
        itemStory = 0;
        itemVisited = 0;
        params.put(activity.getString(R.string.serviceKeyCityLati),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfActiveCityLati)));
        params.put(activity.getString(R.string.serviceKeyCityLongi),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfActiveCityLongi)));
        volleyServices.CallVolleyServices(params,stringURL,"");
    }


        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);



            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menus = menu;
        inflater.inflate(R.menu.search_home_page, menus);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.spinner:


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select City");
                modeList = new ListView(this);
                adapter = new CityAdepter(item);
                modeList.setAdapter(adapter);
                if(!arrayListActiveCity.isEmpty())
                builder.setView(modeList);
                /*
                modeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        item.setTitle(arrayListActiveCity.get(position));
                        String stringCity = hashMapActiveCity.get(modeList.getSelectedItem().toString());
                        CallHomeServiceCityPass(MainHomeScreenActivity.this,stringCity);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
                dialogDis = builder.create();

                dialogDis.show();
                break;
        }

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.menu_lang) {
            Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
            if(UID.isEmpty()||UID.equals("0")) {
                Toast.makeText(MainHomeScreenActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(new Intent(MainHomeScreenActivity.this,MainProfileEditActivity.class));


        } else if (id == R.id.nav_fav) {
            String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
            if(UID.isEmpty()||UID.equals("0")) {
                Toast.makeText(MainHomeScreenActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(new Intent(MainHomeScreenActivity.this, MainActivityFavorite.class));
        } else if (id == R.id.nav_home) {
            TabFragment.viewPager.setCurrentItem(0);
        }  else if (id == R.id.nav_share) {
            String boby = "Hey Checkout Treepr App for plan Trip and look for nearest places\n" +
                    "https://play.google.com/store/apps/developer?id=Whitebird+Technology" ;
            //
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = boby;
            String shareSub = "Share Treepr";
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "Share Using"));

        } else if (id == R.id.nav_log_out) {
            String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
            if(UID.isEmpty()||UID.equals("0")) {
                Toast.makeText(MainHomeScreenActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
            return true;
            }
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfUID),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfCity),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfMobNo), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfDOB), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFrstName), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfLstName), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfGender), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfEmail), null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfImageSel),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfCityName),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfStateName),null);

           // String ImagePath = sharePreferences.getDataFromSharePref("imagePathProfile");
           // File imgFile = new  File(ImagePath);
            //imgFile.delete();
            startActivity(new Intent(MainHomeScreenActivity.this, MainSignInActivity.class));
            this.finish();
        }else if (id == R.id.nav_share_stories) {
            TabFragment.viewPager.setCurrentItem(1);
        }else if (id == R.id.nav_visited_places) {
            TabFragment.viewPager.setCurrentItem(2);
        }else if(id==R.id.nav_your_stories){
            String UID= sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
            if(UID.isEmpty()||UID.equals("0")) {
                Toast.makeText(MainHomeScreenActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(new Intent(MainHomeScreenActivity.this, MainActivityYourStoryPage.class));
            //selectImage();

        }else if(id==R.id.nav_about){
            startActivity(new Intent(MainHomeScreenActivity.this, MainActivityAbout.class));
        }else if(id==R.id.nav_share){
            String boby = "https://play.google.com/store/apps/developer?id=Whitebird+Technology\n" +
                    "Hey Check Out This App Treepr and Planed For You With Your Friends ";
            //
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareBody = boby;
           // String shareSub = "Plan With Treepr";
            //sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        Intent intent = new Intent(MainHomeScreenActivity.this,MainActivityYourStories.class);
        try {
            String stringUri = uri.toString();

            intent.putExtra("STRING_URI",stringUri);
        } catch (Exception e){

        }
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final String stringImageURL = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfImageSel));
        if(!stringImageURL.isEmpty()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    new ImageDownloaderTask(imageViewUserProfile, stringImageURL, MainHomeScreenActivity.this).execute();

                }
            });
            thread.start();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainHomeScreenActivity.this);
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
                    Toast.makeText(MainHomeScreenActivity.this,"Please Check Permission",Toast.LENGTH_SHORT).show();
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

public class CityAdepter  extends BaseAdapter{
    MenuItem item;
    public CityAdepter(MenuItem item) {
        this.item =item;
    }

    @Override
    public int getCount() {
        return arrayListActiveCity.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListActiveCity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        final TextView textView = (TextView)v.findViewById(android.R.id.text1);
        textView.setText(arrayListActiveCity.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      item.setTitle(arrayListActiveCity.get(position));
                String stringCity = hashMapActiveCity.get(arrayListActiveCity.get(position));
                CallHomeServiceCityPass(MainHomeScreenActivity.this,stringCity);
                dialogDis.dismiss();
            }
        });

        return v;
    }
}





}
