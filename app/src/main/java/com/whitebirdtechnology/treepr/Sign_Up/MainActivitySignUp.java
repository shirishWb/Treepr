package com.whitebirdtechnology.treepr.Sign_Up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.whitebirdtechnology.treepr.R;
import com.whitebirdtechnology.treepr.SharePreferences.SharePreferences;
import com.whitebirdtechnology.treepr.Home_Page.VolleyServices;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivitySignUp extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmailSignUp,editTextPasswordSignUp,editTextConfirmPassword,editTextFullNameSignUp,editTextMobileNoSignUp;
    public static AutoCompleteTextView autoCompleteTextViewCitySignUp,autoCompleteTextViewStateSignUp;
    Button buttonCreateAccount;
    RadioButton radioButtonMale,radioButtonFemale;
    SharePreferences sharePreferences;
    VolleyServices volleyServices;
    String stringEmailSignUp,stringPasswordSignUp,stringConfirmPasswordSignUp,stringFullNameSignUp,stringMobileNoSignUp,stringCityNameSignUp,stringMaleOrFemale;
    public static ArrayList<String> arrayListCity,arrayListState;
    public static HashMap<String,String> hashMapState,hashMapCity;
    HashMap<String,String> parameters;
    public static ArrayAdapter<String> adapterCityNames,adapterStateNames;
    String stringStateNameSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setTitle("Register");
        setContentView(R.layout.activity_main_sign_up);
        sharePreferences = new SharePreferences(this);
        autoCompleteTextViewCitySignUp = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewCityNameSignUp);
        autoCompleteTextViewStateSignUp = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewStateNameSignUp);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPasswordInputSignUp);
        editTextEmailSignUp=(EditText)findViewById(R.id.editTextEmailInputSignUp);
        editTextFullNameSignUp =(EditText)findViewById(R.id.editTextFullNameSignUp);
        editTextMobileNoSignUp=(EditText)findViewById(R.id.editTextMobileNumberSignUp);
        editTextPasswordSignUp =(EditText)findViewById(R.id.editTextPasswordInputSignUp);
        buttonCreateAccount =(Button)findViewById(R.id.buttonCreateAccountSignUp);
        radioButtonFemale =(RadioButton)findViewById(R.id.radioButtonFemaleSignUp);
        radioButtonMale=(RadioButton)findViewById(R.id.radioButtonMaleSignUp);
        buttonCreateAccount.setOnClickListener(this);
        radioButtonFemale.setOnClickListener(this);
        radioButtonMale.setOnClickListener(this);
        arrayListState = new ArrayList<>();
        arrayListCity = new ArrayList<>();
        hashMapCity = new HashMap<>();
        hashMapState = new HashMap<>();
       // adapterStateNames = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, arrayListState);


       // autoCompleteTextViewStateSignUp.setAdapter(adapterStateNames);
        volleyServices = new VolleyServices(this);
        parameters = new HashMap<>();
        parameters.put(getString(R.string.serviceKeyStateId),"0");
        volleyServices.CallVolleyServices(parameters,getString(R.string.stateURL),"SignUp");

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
                    parameters.put(getString(R.string.serviceKeyStateId),hashMapState.get(s.toString()));
                    volleyServices.CallVolleyServices(parameters,getString(R.string.stateURL),"SignUp");
                }else {
                    autoCompleteTextViewCitySignUp.setText("");
                    arrayListCity = new ArrayList<>();
                    adapterCityNames = new ArrayAdapter<String>(MainActivitySignUp.this,android.R.layout.simple_dropdown_item_1line, arrayListCity);
                    autoCompleteTextViewCitySignUp.setAdapter(adapterCityNames);
                }
            }
        });
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
    }

    boolean isEmailValid() {
        stringEmailSignUp = editTextEmailSignUp.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(stringEmailSignUp).matches();
    }
    boolean isPasswordMatches(){
        boolean match =false;
        stringPasswordSignUp =editTextPasswordSignUp.getText().toString();
        stringConfirmPasswordSignUp =editTextConfirmPassword.getText().toString();

        if(stringPasswordSignUp.equals(stringConfirmPasswordSignUp)){
            if (stringPasswordSignUp.length()>=4){
                match =true;
            }else {
                Toast.makeText(this,"Enter Password at least 4 character",Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this,"Password not Matched",Toast.LENGTH_SHORT).show();
        if(stringPasswordSignUp.isEmpty()){
            Toast.makeText(this,"Enter Password ",Toast.LENGTH_SHORT).show();
            match =false;
        }

        return match;
    }
    boolean checkMobileNo(){
        boolean MobileMatch =false;
        stringMobileNoSignUp =editTextMobileNoSignUp.getText().toString();
        if(stringMobileNoSignUp.isEmpty()){
            MobileMatch=true;
        }else {
            if (stringMobileNoSignUp.length()==10) {

               if(android.util.Patterns.PHONE.matcher(stringMobileNoSignUp).matches()){
                   MobileMatch = true;
               }else
                   Toast.makeText(this,"Mobile Number Is Not Valid",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            }
        }

        return MobileMatch;
    }

    public boolean isCheckName() {
        stringFullNameSignUp =editTextFullNameSignUp.getText().toString();
        return stringFullNameSignUp.matches("[a-z A-Z]+");
    }
    public boolean isCheckCity() {
        String stringCity =autoCompleteTextViewCitySignUp.getText().toString();
        Boolean state = false;
        if(arrayListCity.contains(stringCity)){
            stringCityNameSignUp = hashMapCity.get(stringCity);
            state = true;
        }
        return state;
    }
    public boolean isCheckState() {
        String stringState =autoCompleteTextViewStateSignUp.getText().toString();
        Boolean state = false;
        if(arrayListState.contains(stringState)){
            stringStateNameSignUp = hashMapState.get(stringState);
            state = true;
        }
        return state;
    }
    @Override
    public void onClick(View v) {

        if(v==radioButtonFemale){
            radioButtonFemale.setChecked(true);
            radioButtonMale.setChecked(false);
        }
        if(v==radioButtonMale){
            radioButtonMale.setChecked(true);
            radioButtonFemale.setChecked(false);
        }
        if(v==buttonCreateAccount){
            if(!isEmailValid()){
                Toast.makeText(this,"Enter Valid Email Id",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPasswordMatches()){


                return;
            }
            if(!isCheckName()){
                if(stringFullNameSignUp.isEmpty()) {
                    Toast.makeText(this, "Enter Name ", Toast.LENGTH_SHORT).show();
                return;
                }
                else {
                    Toast.makeText(this, "Please Enter Your Correct Name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(!checkMobileNo()){
                return;
            }

            if(!isCheckState()){
                Toast.makeText(this,"Please Enter Correct State Name",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isCheckCity()){
                Toast.makeText(this,"Please Enter Correct City Name",Toast.LENGTH_SHORT).show();
                return;
            }
            if(radioButtonFemale.isChecked()){
                stringMaleOrFemale = "Female";
            }else {
                stringMaleOrFemale = "Male";
            }
            HashMap<String,String> parameters= new HashMap<String, String>();
            parameters.put(getString(R.string.serviceKeyEmail),stringEmailSignUp);
            parameters.put(getString(R.string.serviceKeyPassword),stringPasswordSignUp);
            parameters.put(getString(R.string.serviceKeyName),stringFullNameSignUp);
            parameters.put(getString(R.string.serviceKeyCity),stringCityNameSignUp);
            parameters.put(getString(R.string.serviceKeyMoblNo),stringMobileNoSignUp);
            parameters.put(getString(R.string.serviceKeyGender),stringMaleOrFemale);
            parameters.put(getString(R.string.serviceKeyCityName),autoCompleteTextViewCitySignUp.getText().toString());
            parameters.put(getString(R.string.serviceKeyStateName),autoCompleteTextViewStateSignUp.getText().toString());
            parameters.put(getString(R.string.serviceKeyStateId),stringStateNameSignUp);
            volleyServices.CallVolleyServices(parameters,getString(R.string.signUpURL),"");
        }
    }


}
