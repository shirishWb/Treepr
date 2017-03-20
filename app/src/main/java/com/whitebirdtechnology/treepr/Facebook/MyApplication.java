package com.whitebirdtechnology.treepr.Facebook;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.whitebirdtechnology.treepr.FontOfText.FontOfText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shirish on 3/1/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,


            // Add code to print out the key hash
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.whitebirdtechnology.treepr",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    //Toast.makeText(this,"succ",Toast.LENGTH_LONG).show();
                }
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this,"package",Toast.LENGTH_LONG).show();

            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(this,"Algo",Toast.LENGTH_LONG).show();
            }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FontOfText.setDefaultFont(this, "MONOSPACE", "fonts/avenir.ttf");
        FontOfText.setDefaultFont(this, "DEFAULT", "fonts/avenir.ttf");
        FontOfText.setDefaultFont(this, "SERIF", "fonts/avenir.ttf");
        FontOfText.setDefaultFont(this, "SANS_SERIF", "fonts/avenir.ttf");
    }
}