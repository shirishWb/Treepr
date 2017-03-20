package com.whitebirdtechnology.treepr.SplashScreen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.whitebirdtechnology.treepr.Launcher_screen.MainActivityLogInSignUp;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;


import java.util.HashMap;

import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


public class MainActivitySplashScreen extends AppCompatActivity {

    private int currentApiVersion;
    SharePreferences sharePreferences;
    String UID;
    static Dialog dialogs;
    TextView textViewTitleTreepr;
    VolleyServices volleyServices;
    static Handler handler;
    static Runnable runnable;
    protected ImageLoader imageLoader;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_splash_screen);
        sharePreferences = new SharePreferences(this);
       // if(!isNetworkConnected())
        //    Toast.makeText(this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
        sharePreferences.saveDataInShrPref(getString(R.string.sharPrfURL),getString(R.string.serviceURL));
        UID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        volleyServices = new VolleyServices(this);
        //textViewTitleTreepr = (TextView) findViewById(R.id.textViewTreeprTitleName);
     //   Typeface fontOfTreepr = Typeface.createFromAsset(getAssets(), "fonts/apple_chancery.ttf");
       // textViewTitleTreepr.setTypeface(fontOfTreepr);

     //   PackageManager manager = this.getPackageManager();
       // PackageInfo info = null;
        try {
            //info = manager.getPackageInfo(MainActivitySplashScreen.this.getPackageName(), 0);
            String version = String.valueOf(Build.VERSION.SDK_INT);
            HashMap<String,String> params = new HashMap<>();
            params.put("version",version);
            volleyServices.CallVolleyServices(params,"version.php","Splash");
        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }*/
        final int SPLASH_DISPLAY_LENGTH = 1000;
        handler =new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(!UID.isEmpty()&&!UID.equals("0")){
                    startActivity(new Intent(MainActivitySplashScreen.this, MainHomeScreenActivity.class));
                    MainActivitySplashScreen.this.finish();
                }else {
                    Intent mainIntent = new Intent(MainActivitySplashScreen.this, MainActivityLogInSignUp.class);
                    MainActivitySplashScreen.this.startActivity(mainIntent);
                    MainActivitySplashScreen.this.finish();
                }

            }
        };
       handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);



    }
    public static void showUpdateDialog(final Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("A New Update is Available");
        dialogs = builder.create();
        handler.removeCallbacks(runnable);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                ("market://details?id=com.whitebirdtechnology.treepr")));
                    }
                });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePreferences sharePreferences = new SharePreferences(activity);
               String UID = sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfUID));
                final Intent intent;
                if(!UID.isEmpty()&&!UID.equals("0")) {
                    intent = new Intent(activity, MainHomeScreenActivity.class);
                }else {
                    intent =  new Intent(activity, MainActivityLogInSignUp.class);
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            activity.startActivity(intent);
                            activity.finish();

                    }
                });

                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });

    }
/*
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/
    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            //showNoConnectionDialog(context);
            // There are no active networks.
            //Set the visibility to "gone".
            //You can set visibility to gone here or when the function returns,
            //that is why there is a return false and true.
            return false;
        } else
            return true;
    }
}
