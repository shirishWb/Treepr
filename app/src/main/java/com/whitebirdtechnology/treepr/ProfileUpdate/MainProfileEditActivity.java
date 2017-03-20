package com.whitebirdtechnology.treepr.ProfileUpdate;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;
import com.whitebirdtechnology.treepr.volley.ImageDownloaderTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MainProfileEditActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton imageButtonProfileEdit;
    ImageView imageViewProfile;
    Dialog alertDialogDismiss;
    TextView textViewCalender, textViewGender, textViewEmail, textViewFirstName, textViewLastName, textViewChangePassword;
    EditText editTextMobileNo;
    ImageLoader imageLoader;
    public static AutoCompleteTextView autoCompleteTextViewCityNameProfile,autoCompleteTextViewStateNameProfile;
    String stringMobileNoProfile, stringCityNameProfile, stringDateOfBirth, stringNewPassword, stringConfirmPassword, stringOldPassword;
    ImageButton imageButtonCalender;
    Button buttonUpdateProfile;
    public static ArrayList<String> arrayListCity,arrayListState;
    public static HashMap<String,String> hashMapState,hashMapCity;
    HashMap<String,String> parameters;
    public static ArrayAdapter<String> adapterCityNames,adapterStateNames;
    RequestQueue requestQueuePassword, requestQueueProfileData;
    private DatePicker datePicker;
    String stringImageSel;
    private Calendar calendar;
    VolleyServices volleyServices;
    private int year, month, day;
    SharePreferences sharePreferences;
    String UserID;
    String stringStateNameProfile;



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");
        setContentView(R.layout.activity_main_profile_edit);
        sharePreferences = new SharePreferences(this);
        volleyServices = new VolleyServices(this);
        imageButtonProfileEdit = (ImageButton) findViewById(R.id.imageButtonEditProfile);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfilePage);
        imageButtonProfileEdit.setOnClickListener(this);
        imageViewProfile.buildDrawingCache();
        textViewCalender = (TextView) findViewById(R.id.textViewDOB);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        textViewEmail = (TextView) findViewById(R.id.textViewEmailProfile);
        textViewFirstName = (TextView) findViewById(R.id.textViewFirstNameProfile);
        textViewLastName = (TextView) findViewById(R.id.textViewLastNameProfile);
        autoCompleteTextViewCityNameProfile = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCityProfile);
        autoCompleteTextViewStateNameProfile = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewStateProfile);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobileNumberProfile);
        imageButtonCalender = (ImageButton) findViewById(R.id.imageButtonCalender);
        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        final String stringImageURL = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfImageSel));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    new ImageDownloaderTask(imageViewProfile,stringImageURL,MainProfileEditActivity.this).execute();

                }
            });thread.start();
        buttonUpdateProfile.setOnClickListener(this);
        UserID = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfUID));
        requestQueueProfileData = Volley.newRequestQueue(getApplicationContext());
        arrayListState = new ArrayList<>();
        arrayListCity = new ArrayList<>();
        hashMapCity = new HashMap<>();
        hashMapState = new HashMap<>();


        parameters = new HashMap<>();
        parameters.put(getString(R.string.serviceKeyStateId),"0");
        volleyServices.CallVolleyServices(parameters,getString(R.string.stateURL),"Profile");
        autoCompleteTextViewStateNameProfile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextViewStateNameProfile.showDropDown();
            }
        });
        autoCompleteTextViewCityNameProfile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextViewCityNameProfile.showDropDown();
            }
        });
        autoCompleteTextViewStateNameProfile.addTextChangedListener(new TextWatcher() {
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
                    parameters.put(getString(R.string.serviceKeyStateId),hashMapState.get(s.toString()));
                    volleyServices.CallVolleyServices(parameters,getString(R.string.stateURL),"Profile");
                }else {
                    autoCompleteTextViewCityNameProfile.setText("");
                    arrayListCity = new ArrayList<>();
                    adapterCityNames = new ArrayAdapter<String>(MainProfileEditActivity.this,android.R.layout.simple_dropdown_item_1line, arrayListCity);
                    autoCompleteTextViewCityNameProfile.setAdapter(adapterCityNames);
                }
            }
        });

        //   Toast.makeText(this,stringUserIdProfile,Toast.LENGTH_SHORT).show();
        //if(false){
        String stringEmail = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfEmail));
        String stringFirstName = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfFrstName));
        String stringLastName = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfLstName));
        stringCityNameProfile = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfCity));
        String stringGender = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfGender));
        stringDateOfBirth = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfDOB));
        stringMobileNoProfile = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfMobNo));
        try {
            if (stringDateOfBirth.isEmpty() || stringDateOfBirth.equals("null") || stringDateOfBirth.equals("0000-00-00"))
                stringDateOfBirth = "";
        }catch (Exception e){
            stringDateOfBirth = "";
        }


        stringImageSel = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfImageSel));
        textViewGender.setText(stringGender);
         /*
        File imgFile = new File(ImagePath);

        if (imgFile.exists()) {
            Toast.makeText(this, ImagePath, Toast.LENGTH_SHORT).show();

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageViewProfile.setImageBitmap(myBitmap);

        }*/


        textViewEmail.setText(stringEmail);
        editTextMobileNo.setText(stringMobileNoProfile);
        textViewCalender.setText(stringDateOfBirth);
        textViewFirstName.setText(stringFirstName);
        textViewLastName.setText(stringLastName);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        buttonUpdateProfile.setOnClickListener(this);
        imageButtonCalender.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });


        textViewChangePassword = (TextView) findViewById(R.id.textViewChangePassword);
        textViewChangePassword.setOnClickListener(this);
        requestQueuePassword = Volley.newRequestQueue(getApplicationContext());


    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        Date dateCurrent = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date dateSelected = null;
        StringBuilder stringDate = new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day);
        try {
            dateSelected = sdf.parse(String.valueOf(stringDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateCurrent.equals(dateSelected) || dateCurrent.after(dateSelected)) {
            textViewCalender.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            return;
        }
        Toast.makeText(MainProfileEditActivity.this, "Invalid Date..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == imageButtonProfileEdit) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainProfileEditActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View convertView = (View) inflater.inflate(R.layout.choose_profile_pick_dialog, null);
            alertDialog.setView(convertView);
            alertDialogDismiss = alertDialog.create();

         //   GridView gridview = (GridView) convertView.findViewById(R.id.gridViewAvatar);
           // gridview.setAdapter(new ImageAdapter(this));

            alertDialogDismiss.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitleDialog);
            textViewTitle.setText("Select Profile Picture");
            TextView textViewFromGallery = (TextView) convertView.findViewById(R.id.textViewFromGallery);
            TextView textViewCamera = (TextView) convertView.findViewById(R.id.textViewCamera);
            textViewCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogDismiss.dismiss();
                    try {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }catch (Exception e){
                        Toast.makeText(MainProfileEditActivity.this,"Please Check Permission",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            textViewFromGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogDismiss.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
            });


            alertDialogDismiss.show();
        }

        if (v == textViewChangePassword) {
            final AlertDialog.Builder alertDialogPass = new AlertDialog.Builder(MainProfileEditActivity.this);
            LayoutInflater inflaterPass = getLayoutInflater();
            @SuppressLint("ViewHolder") View convertViewPass = (View) inflaterPass.inflate(R.layout.change_password_dialog, null);
            alertDialogPass.setView(convertViewPass);
          //  TextView textViewTitle = (TextView)convertViewPass.findViewById(R.id.textViewTitleDialog);
            alertDialogPass.setTitle("Change Password");
            final EditText editTextOldPassword = (EditText) convertViewPass.findViewById(R.id.editTextOldPasswordDialog);
            final EditText editTextNewPassword = (EditText) convertViewPass.findViewById(R.id.editTextNewPasswordDialog);
            final EditText editTextConfirmPassword = (EditText) convertViewPass.findViewById(R.id.editTextConfirmPasswordDialog);
            alertDialogPass.setNegativeButton("No", null);
            alertDialogPass.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stringOldPassword = editTextOldPassword.getText().toString();
                    stringNewPassword = editTextNewPassword.getText().toString();
                    stringConfirmPassword = editTextConfirmPassword.getText().toString();
                    if (isPasswordMatches()) {
                        //send Password

                        final HashMap<String, String> parameters = new HashMap<String, String>();


                        parameters.put(getString(R.string.serviceKeyPasswordOld), stringOldPassword);
                        parameters.put(getString(R.string.serviceKeyPasswordNew), stringNewPassword);
                        parameters.put(getString(R.string.serviceKeyUID), UserID);
                        volleyServices.CallVolleyServices(parameters,getString(R.string.passwordChangeURL),"");
                        //servicesRegister();

                    }
                }
            });

            alertDialogPass.create();
           // alertDialogDismiss.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            alertDialogPass.show();

        }
        if (v == buttonUpdateProfile) {
            if (!checkMobileNoFirst()) {
                return;
            }
            if(!isCheckState()){
                Toast.makeText(this,"Please Enter Correct State Name",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isCheckCity()) {
                Toast.makeText(MainProfileEditActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();

                return;
            }
            stringDateOfBirth = textViewCalender.getText().toString();
            if (stringDateOfBirth.isEmpty()) {
                Toast.makeText(MainProfileEditActivity.this, "Enter Date Of Birth", Toast.LENGTH_SHORT).show();
                return;
            }
            final HashMap<String,String> parameters= new HashMap<String, String>();
            imageViewProfile.buildDrawingCache();
            Bitmap bmap = imageViewProfile.getDrawingCache();
            String stringImageView = getStringImage(bmap);
            parameters.put(getString(R.string.serviceKeyProfileImage),stringImageView);
            parameters.put(getString(R.string.serviceKeyMoblNo), stringMobileNoProfile);
            parameters.put(getString(R.string.serviceKeyDOB),stringDateOfBirth);
            parameters.put(getString(R.string.serviceKeyCity),stringCityNameProfile);
            parameters.put(getString(R.string.serviceKeyUID),UserID);
            parameters.put(getString(R.string.serviceKeyCityName),autoCompleteTextViewCityNameProfile.getText().toString());
            parameters.put(getString(R.string.serviceKeyStateName),autoCompleteTextViewStateNameProfile.getText().toString());
            parameters.put(getString(R.string.serviceKeyStateId),stringStateNameProfile);
            try {
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                //final String stringImageURL = sharePreferences.getDataFromSharePref(getString(R.string.sharPrfURL))+sharePreferences.getDataFromSharePref(getString(R.string.sharPrfImageSel));
                //MemoryCacheUtils.removeFromCache(stringImageURL, ImageLoader.getInstance().getMemoryCache());
               // DiscCacheUtils.removeFromCache(stringImageURL, ImageLoader.getInstance().getDiscCache());

                /*
                File imageFile = imageLoader.getDiscCache().get(stringImageURL);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
*/

            }catch (Exception e){

            }

            volleyServices.CallVolleyServices(parameters,getString(R.string.profileUpdateURL),"");
        }


    }
    public boolean isCheckState() {
        String stringState =autoCompleteTextViewStateNameProfile.getText().toString();
        Boolean state = false;
        if(arrayListState.contains(stringState)){
            stringStateNameProfile = hashMapState.get(stringState);
            state = true;
        }
        return state;
    }

    public boolean isCheckCity() {
        String stringCity =autoCompleteTextViewCityNameProfile.getText().toString();
        Boolean state = false;
        if(arrayListCity.contains(stringCity)){
            stringCityNameProfile = hashMapCity.get(stringCity);
            state = true;
        }
        return state;
    }

    boolean isPasswordMatches(){
        boolean match =false;

        if(stringNewPassword.equals(stringConfirmPassword)){
            if (stringNewPassword.length()>=4){
                match =true;
            }else {
                Toast.makeText(this,"Enter Password at least 4 character",Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this,"Password not Matched",Toast.LENGTH_SHORT).show();
        if(stringNewPassword.isEmpty()){
            Toast.makeText(this,"Enter Password ",Toast.LENGTH_SHORT).show();
            match =false;
        }

        return match;
    }
    boolean checkMobileNoFirst(){
        stringMobileNoProfile= editTextMobileNo.getText().toString();
        boolean MobileMatch =false;
        if(stringMobileNoProfile.isEmpty()){
            MobileMatch=true;
        }else {
            if (stringMobileNoProfile.length()==10) {

                if(android.util.Patterns.PHONE.matcher(stringMobileNoProfile).matches()){
                    MobileMatch = true;
                }else
                    Toast.makeText(this,"Mobile Number Is Not Valid",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            }
        }

        return MobileMatch;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                     Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageViewProfile.setImageBitmap(photo);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    imageViewProfile.setImageURI(selectedImage);
                }
                break;
        }
    }
}
