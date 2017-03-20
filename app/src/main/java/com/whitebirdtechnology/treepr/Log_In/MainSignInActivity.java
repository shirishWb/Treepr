package com.whitebirdtechnology.treepr.Log_In;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Home_Page.MainHomeScreenActivity;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainSignInActivity extends AppCompatActivity {
    TextView textViewTitleTreeprSignIn;
    Button  buttonSignIn;
    EditText editTextEmail, editTextPassword;
    LoginButton loginButton;
    VolleyServices volleyServices;
    static RadioButton radioButtonMale,radioButtonFemale;
    private CallbackManager callbackManager;
    SharePreferences sharePreferences;
    private int currentApiVersion;
    public static ArrayList<String> arrayListCity,arrayListState;
    public static HashMap<String,String> hashMapState,hashMapCity;
    public static ArrayAdapter<String> adapterCityNames,adapterStateNames;
    public static String stringEmailSignUp,stringFullNameSignUp,stringCityNameSignUp,stringMaleOrFemale;
    static HashMap<String,String> parameters;
    static EditText editTextEmailSignUp,editTextPasswordSignUp,editTextConfirmPassword,editTextFullNameSignUp,editTextMobileNoSignUp;
    public static AutoCompleteTextView autoCompleteTextViewCitySignUp,autoCompleteTextViewStateSignUp;
    public static String stringStateNameSignUp;
    TextView textViewFacebookLogIn;
    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_in);
        currentApiVersion = Build.VERSION.SDK_INT;
        sharePreferences = new SharePreferences(this);
        volleyServices = new VolleyServices(this);
        textViewFacebookLogIn = (TextView)findViewById(R.id.buttonTopOnFb);
        textViewFacebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();
            }
        });
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setReadPermissions(Arrays.asList(
                        "public_profile", "email", "user_birthday", "user_friends"));

                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile profile = Profile.getCurrentProfile();

                        // Facebook Email address
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.v("LoginActivity Response ", response.toString());
                                        new FirstTask(object).execute();

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();

                    }

                    @Override
                    public void onError(FacebookException e) {

                    }
                });
            }
        });


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }


        textViewTitleTreeprSignIn = (TextView) findViewById(R.id.textViewTreeprTitleNameSignIn);
        Typeface fontOfTreeprSignIn = Typeface.createFromAsset(getAssets(), "fonts/apple_chancery.ttf");
        textViewTitleTreeprSignIn.setTypeface(fontOfTreeprSignIn);
        buttonSignIn = (Button) findViewById(R.id.buttonSignInLogInPage);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailInput);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordInput);


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onLoginFailed();
                    return;
                }
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put(getString(R.string.serviceKeyEmail), editTextEmail.getText().toString());
                parameters.put(getString(R.string.serviceKeyPassword), editTextPassword.getText().toString());
                volleyServices.CallVolleyServices(parameters,getString(R.string.loginURL),"");
             //   new SendTask(parameters).execute();

            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(getBaseContext(), "enter email address", Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(getBaseContext(), "enter password address", Toast.LENGTH_LONG).show();
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //textInputLayout.setError("enter a valid email address");
            Toast.makeText(getBaseContext(), "enter a valid email address", Toast.LENGTH_LONG).show();

            valid = false;
        }

        return valid;
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //_loginButton.setEnabled(true);
    }


    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private class FirstTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainSignInActivity.this);
        int temp = 0;
        JSONObject object;
        HashMap<String,String> param;
        public FirstTask(JSONObject object) {
            this.object = object;
        }

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
            System.gc();
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbName),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbEmail),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbGender),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbDob),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbId),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbPrfImg),null);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbCity),null);
           // Toast.makeText(MainSignInActivity.this, "My Async  Created", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String id=null;
            try {
               id = object.getString("id");
            }catch (Exception e){
                Toast.makeText(MainSignInActivity.this,"You Are Not Registered On Facebook",Toast.LENGTH_SHORT).show();
            }
            Bitmap bitmap = getFacebookProfilePicture(id);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encodedProfileFb = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String name=null,email=null,gender=null,birthday = null;
            try {
                name = object.getString("name");
            }catch (Exception e){

            }
            try {
                email = object.getString("email");
            }catch (Exception e){

            }
            try {
                gender = object.getString("gender");
            }catch (Exception e){

            }
            try {
                birthday = object.getString("birthday");
            }catch (Exception e){

            }
            String[] split = {};
            try {
                split = birthday.split("\\/");
            }catch (Exception e){

            }
            String day = null;
            String month=null;
            String year = null;
            try{
                month = split[0];
            }catch (Exception e){

            }
            try{
                day = split[1];
            }catch (Exception e){

            }
            try{
                year = split[2];
            }catch (Exception e){

            }
            Log.v("Email = ", " " + email);
            param = new HashMap<String, String>();
          //  param.put(getString(R.string.serviceKeyName),name);
           // param.put(getString(R.string.serviceKeyEmail),email);
           // param.put(getString(R.string.serviceKeyGender),gender);
            //param.put(getString(R.string.serviceKeyDOB),year+"-"+month+"-"+day);
            param.put(getString(R.string.serviceKeyFacebookId),id);
            //param.put(getString(R.string.serviceKeyProfileImage),encodedProfileFb);
            // Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();
            //call volley
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbName),name);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbEmail),email);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbGender),gender);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbDob),year+"-"+month+"-"+day);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbId),id);
            sharePreferences.saveDataInShrPref(getString(R.string.sharPrfFbPrfImg),encodedProfileFb);
            LoginManager.getInstance().logOut();
            return null;
        }



        protected void onPostExecute(Void result) {

            volleyServices.CallVolleyServices(param,getString(R.string.facebookLogInURL),"");
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
    public static void CallFillBlankSignUpDialog(final Activity activity){
        final Dialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View convertView = (View) inflater.inflate(R.layout.activity_main_sign_up, null);
        alertDialog.setView(convertView);
        dialog = alertDialog.create();
        Button buttonCreateAccount;
        final VolleyServices volleyServices;
        volleyServices = new VolleyServices(activity);
        final SharePreferences sharePreferences = new SharePreferences(activity);
        autoCompleteTextViewCitySignUp = (AutoCompleteTextView)convertView.findViewById(R.id.autoCompleteTextViewCityNameSignUp);
        autoCompleteTextViewStateSignUp = (AutoCompleteTextView)convertView.findViewById(R.id.autoCompleteTextViewStateNameSignUp);
        editTextConfirmPassword=(EditText)convertView.findViewById(R.id.editTextConfirmPasswordInputSignUp);
        editTextEmailSignUp=(EditText)convertView.findViewById(R.id.editTextEmailInputSignUp);
        editTextFullNameSignUp =(EditText)convertView.findViewById(R.id.editTextFullNameSignUp);
        editTextMobileNoSignUp=(EditText)convertView.findViewById(R.id.editTextMobileNumberSignUp);
        editTextPasswordSignUp =(EditText)convertView.findViewById(R.id.editTextPasswordInputSignUp);
        radioButtonFemale =(RadioButton)convertView.findViewById(R.id.radioButtonFemaleSignUp);
        radioButtonMale=(RadioButton)convertView.findViewById(R.id.radioButtonMaleSignUp);
        buttonCreateAccount =(Button)convertView.findViewById(R.id.buttonCreateAccountSignUp);
        buttonCreateAccount.setVisibility(View.GONE);
        editTextConfirmPassword.setVisibility(View.GONE);
        editTextPasswordSignUp.setVisibility(View.GONE);
        editTextMobileNoSignUp.setVisibility(View.GONE);
        final String fbName= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbName));
        final String fbEmail= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbEmail));
        final String fbGender= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbGender));
        final String fbCity= sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbCity));
        if(!fbName.isEmpty()){
            editTextFullNameSignUp.setVisibility(View.GONE);
        }
        if(!fbCity.isEmpty()){
            autoCompleteTextViewCitySignUp.setVisibility(View.GONE);
            autoCompleteTextViewStateSignUp.setVisibility(View.GONE);
        }else {
            autoCompleteTextViewStateSignUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    autoCompleteTextViewStateSignUp.showDropDown();
                }
            });
            autoCompleteTextViewCitySignUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    autoCompleteTextViewCitySignUp.showDropDown();
                }
            });
            arrayListState = new ArrayList<>();
            arrayListCity = new ArrayList<>();
            hashMapCity = new HashMap<>();
            hashMapState = new HashMap<>();
            parameters = new HashMap<>();
            parameters.put(activity.getString(R.string.serviceKeyStateId),"0");
            volleyServices.CallVolleyServices(parameters,activity.getString(R.string.stateURL),"LogIn");
            autoCompleteTextViewStateSignUp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(arrayListState.contains(s.toString())){
                        parameters = new HashMap<String, String>();
                        parameters.put(activity.getString(R.string.serviceKeyStateId),hashMapState.get(s.toString()));
                        volleyServices.CallVolleyServices(parameters,activity.getString(R.string.stateURL),"LogIn");
                    }else {
                        autoCompleteTextViewCitySignUp.setText("");
                        arrayListCity = new ArrayList<>();
                        adapterCityNames = new ArrayAdapter<String>(activity,android.R.layout.simple_dropdown_item_1line, arrayListCity);
                        autoCompleteTextViewCitySignUp.setAdapter(adapterCityNames);
                    }
                }
            });

        }

        if(!fbEmail.isEmpty()){
            editTextEmailSignUp.setVisibility(View.GONE);
        }
        if(!fbGender.isEmpty()){
            radioButtonMale.setVisibility(View.GONE);
            radioButtonFemale.setVisibility(View.GONE);
        }
        radioButtonFemale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                radioButtonFemale.setChecked(true);
                radioButtonMale.setChecked(false);
            }
        });
        radioButtonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioButtonMale.setChecked(true);
                radioButtonFemale.setChecked(false);
            }
        });

        alertDialog.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.startActivity(new Intent(activity,MainHomeScreenActivity.class));

                    }
                });


            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(fbEmail.isEmpty()) {
                    if (!isEmailValid()) {
                        ToastMsg(activity,"Enter Valid Email Id");

                        return;
                    }
                }
                if(fbName.isEmpty()) {
                    if (!isCheckName()) {
                        if (stringFullNameSignUp.isEmpty()) {
                            ToastMsg(activity,"Enter Name ");
                            return;
                        } else {
                            ToastMsg(activity,"Please Enter Your Correct Name");
                            return;
                        }
                    }
                }
                if(!isCheckState()){
                    ToastMsg(activity,"Please Enter Correct State Name");
                    return;
                }
                if(fbCity.isEmpty()) {

                    if (!isCheckCity()) {
                        ToastMsg(activity,"Please Enter Correct City Name");
                        return;
                    }
                }
                if(fbGender.isEmpty()) {
                    if (radioButtonFemale.isChecked()) {
                        stringMaleOrFemale = "Female";
                    } else {
                        stringMaleOrFemale = "Male";
                    }
                }
                if(fbEmail.isEmpty()){
                    sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFbName),stringEmailSignUp);
                }
                if(fbName.isEmpty()){
                    sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFbEmail),stringFullNameSignUp);
                }
                if(fbGender.isEmpty()){
                    sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFbGender),stringMaleOrFemale);
                }
                if(fbCity.isEmpty()){
                    sharePreferences.saveDataInShrPref(activity.getString(R.string.sharPrfFbCity),stringCityNameSignUp);
                }

                HashMap<String,String> param = new HashMap<String, String>();
                param.put(activity.getString(R.string.serviceKeyName),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbName)));
                param.put(activity.getString(R.string.serviceKeyEmail),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbEmail)));
                param.put(activity.getString(R.string.serviceKeyGender),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbGender)));
                param.put(activity.getString(R.string.serviceKeyDOB),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbDob)));
                param.put(activity.getString(R.string.serviceKeyFacebookId),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbId)));
                param.put(activity.getString(R.string.serviceKeyProfileImage),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbPrfImg)));
                param.put(activity.getString(R.string.serviceKeyCity),sharePreferences.getDataFromSharePref(activity.getString(R.string.sharPrfFbCity)));
                param.put(activity.getString(R.string.serviceKeyCityName),autoCompleteTextViewCitySignUp.getText().toString());
                param.put(activity.getString(R.string.serviceKeyStateName),autoCompleteTextViewStateSignUp.getText().toString());
                param.put(activity.getString(R.string.serviceKeyStateId),stringStateNameSignUp);
                volleyServices.CallVolleyServices(param,activity.getString(R.string.facebookURL),"");
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });

    }
    public static boolean isEmailValid() {
        stringEmailSignUp = editTextEmailSignUp.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(stringEmailSignUp).matches();
    }
    public static boolean isCheckName() {
        stringFullNameSignUp =editTextFullNameSignUp.getText().toString();
        return stringFullNameSignUp.matches("[a-z A-Z]+");
    }
    public static boolean isCheckCity() {
        String stringCity =autoCompleteTextViewCitySignUp.getText().toString();
        Boolean state = false;
        if(arrayListCity.contains(stringCity)){
            stringCityNameSignUp = hashMapCity.get(stringCity);
            state = true;
        }
        return state;
    }
    public static boolean isCheckState() {
        String stringState =autoCompleteTextViewStateSignUp.getText().toString();
        Boolean state = false;
        if(arrayListState.contains(stringState)){
            stringStateNameSignUp = hashMapState.get(stringState);
            state = true;
        }
        return state;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
        public static void ToastMsg(final Activity activity, final String Msg){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, Msg, Toast.LENGTH_SHORT).show();

                }
            });
        }
}
