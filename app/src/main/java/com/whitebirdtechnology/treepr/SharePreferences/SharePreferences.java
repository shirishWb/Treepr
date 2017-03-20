package com.whitebirdtechnology.treepr.SharePreferences;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dell on 20/2/17.
 */

public class SharePreferences {
    static String TREEEPRDATA = "TreeprData";
    SharedPreferences sharedPreferences;
    Activity activity;

    public SharePreferences(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(TREEEPRDATA,MODE_PRIVATE);
    }

    public void saveDataInShrPref(String key,String value){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public String getDataFromSharePref(String key){
        String stringValue;
        stringValue =sharedPreferences.getString(key, "");
        return stringValue;

    }
}
