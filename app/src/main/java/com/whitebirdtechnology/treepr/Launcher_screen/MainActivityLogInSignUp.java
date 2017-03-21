package com.whitebirdtechnology.treepr.Launcher_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity;
import com.whitebirdtechnology.treepr.Sign_Up.MainActivitySignUp;
import com.whitebirdtechnology.treepr.Log_In.MainSignInActivity;
import com.whitebirdtechnology.treepr.R;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivityLogInSignUp extends AppCompatActivity {


    TextView textViewTitleTreepr;
    Button buttonSignInFirstPage,buttonSignUpFirstPage,buttonSkip;
    private int currentApiVersion;


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
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.LOCATION_HARDWARE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );

        }
        if(cameraPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_CAMERA,REQUEST_EXTERNAL_STORAGE);
        }
        if(locationPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_LOCATION,REQUEST_EXTERNAL_STORAGE);
        }
    }


    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_log_in_sign_up);
        // Add code to print out the key hash
        verifyStoragePermissions(this);
       // currentApiVersion = android.os.Build.VERSION.SDK_INT;
/*
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
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
        }
*/

        textViewTitleTreepr = (TextView) findViewById(R.id.textViewTreeprTitleName);
        Typeface fontOfTreepr = Typeface.createFromAsset(getAssets(), "fonts/apple_chancery.ttf");
        textViewTitleTreepr.setTypeface(fontOfTreepr);
        buttonSignInFirstPage =(Button)findViewById(R.id.button_signInFirstPage);
        buttonSignInFirstPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivityLogInSignUp.this, MainSignInActivity.class));
                    MainActivityLogInSignUp.this.finish();

            }
        });

        buttonSignUpFirstPage =(Button)findViewById(R.id.buttonSignUpFirstPage);
        buttonSignUpFirstPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityLogInSignUp.this,MainActivitySignUp.class));
            }
        });
        buttonSkip =(Button)findViewById(R.id.buttonSkipFirstScreen);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissionsCheck(MainActivityLogInSignUp.this);

            }
        });
    }


    //persmission method.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void verifyStoragePermissionsCheck(final Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.LOCATION_HARDWARE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );

        }else {
            startActivity(new Intent(MainActivityLogInSignUp.this,MainHomeScreenActivity.class));
        }
        if(cameraPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_CAMERA,REQUEST_EXTERNAL_STORAGE);
        }
        if(locationPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISION_LOCATION,REQUEST_EXTERNAL_STORAGE);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/
}