package com.example.iz_test.handzforhire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    static SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "User_Details";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String USER_TYPE = "user_type";

    // Email address (make variable public to access from outside)
    public static final String KEY_ID = "id";
    public static final String KEY_FACEBOOK_ID = "facebook_id";
    public static final String KEY_USERNAME = "user_name";
    public static final String KEY_PASSWORD = "pass_word";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_ZIPCODE = "zipcode";

    public static final String ID = "id";
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "pass_word";
    public static final String EMAIL = "email";
    public static final String TYPE = "type";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIPCODE = "zipcode";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    public static final String CHECKBOX_STATUS = "checkbox_status";

     // Email address (make variable public to access from outside)
    public static final String KEY_PASS = "password";
    public static final String LOGIN_STATUS = "login_status";
    public static final String PAYPAL_REDIRECT = "paypalredirect";
    public static final String PAYPAL_ORDERID = "paypalorderid";
    public static final String PAYPAL_ACCESSTOEKN= "paypalaccesstoken";
    public static final String PAYMENT_DETAILS= "makepaymentvalaue";
    public static final String PAYMENT_REFERALAPI= "parterreferralapi";
    public static final String PAYMENT_REGISTRATION= "registrationdet";
    public static final String PAYMENT_PASS = "regpass";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void driverLoginSession(String Driver_Name, String Driver_Id){
        // Storing email in preferences
        editor.putString(KEY_ID, Driver_Id);

        // commit changes
        editor.commit();
    }

    public void SaveUsername(String username)
    {
        editor.putString(USERNAME,username);
        editor.commit();
    }

    public void LendLogin(String email,String password,String name,String type,String id,String address,String city,String state,String zipcode,String lendusertype){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(LOGIN_STATUS, false);

        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(USERNAME, name);
        editor.putString(TYPE, type);
        editor.putString(ID, id);
        editor.putString(ADDRESS, address);
        editor.putString(CITY, city);
        editor.putString(STATE, state);
        editor.putString(ZIPCODE, zipcode);
        editor.putString(USER_TYPE, lendusertype);

        // commit changes
        editor.commit();
    }

    public void LendLogin(String email,String password,String name,String type,String id,String facebook_id,String address,String city,String state,String zipcode,String lendusertype){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(LOGIN_STATUS, false);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(USERNAME, name);
        editor.putString(TYPE, type);
        editor.putString(ID, id);
        editor.putString(KEY_FACEBOOK_ID ,facebook_id);
        editor.putString(ADDRESS, address);
        editor.putString(CITY, city);
        editor.putString(STATE, state);
        editor.putString(ZIPCODE, zipcode);
        editor.putString(USER_TYPE, lendusertype);

        // commit changes
        editor.commit();
    }

    public void saveCheckboxStatus(String checkboxStatus){
        // Storing login value as TRUE
        editor.putString(CHECKBOX_STATUS, checkboxStatus);
        // commit changes
        editor.commit();
    }

    public void savePaypalRedirect(String paypalredirect){
        // Storing paypalredirect value
        editor.putString(PAYPAL_REDIRECT, paypalredirect);
        // commit changes
        editor.commit();
    }

    public String ReadPaypalRedirect(){

        return pref.getString(PAYPAL_REDIRECT,"0");
    }

    public void saveReraalapilink(String link){
        // Storing paypalredirect value
        editor.putString(PAYMENT_REFERALAPI, link);
        // commit changes
        editor.commit();
    }

    public String readReraalapilink(){

        return pref.getString(PAYMENT_REFERALAPI,"");
    }

    public void saveorderId(String orderid){
        // Storing paypalredirect value
        editor.putString(PAYPAL_ORDERID, orderid);
        // commit changes
        editor.commit();
    }

    public String ReadorderID(){

        return pref.getString(PAYPAL_ORDERID,"");
    }

    public void saveAccesstoken(String accesstoken){
        // Storing paypalredirect value
        editor.putString(PAYPAL_ACCESSTOEKN, accesstoken);
        // commit changes
        editor.commit();
    }

    public String ReadAccessToekn(){

        return pref.getString(PAYPAL_ACCESSTOEKN,"");
    }

    public void savepaymentdetails(String paymentdet){
        // Storing paypalredirect value
        editor.putString(PAYMENT_DETAILS, paymentdet);
        // commit changes
        editor.commit();
    }

    public String Readpaymentdetails(){
        return pref.getString(PAYMENT_DETAILS,"");
    }

    public void saveregistrationdet(String registraiondet){
        // Storing paypalredirect value
        editor.putString(PAYMENT_REGISTRATION, registraiondet);
        // commit changes
        editor.commit();
    }

    public String Readreg(){
        return pref.getString(PAYMENT_REGISTRATION,"");
    }

    public void savepass(String registraiondet){
        // Storing paypalredirect value
        editor.putString(PAYMENT_PASS, registraiondet);
        // commit changes
        editor.commit();
    }

    public String ReadPass(){
        return pref.getString(PAYMENT_PASS,"");
    }




    public  HashMap<String, String> getCheckboxStatus(){
        HashMap<String, String> check = new HashMap<String, String>();
        // Storing name in preferences
        check.put(CHECKBOX_STATUS, pref.getString(CHECKBOX_STATUS, ""));

        return check;
    }

    public void NeedLogin(String email,String password,String name,String type,String id,String address,String city,String state,String zipcode,String usertype){
        // Storing login value as TRUE
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putBoolean(IS_LOGIN, false);

        // Storing name in preferences
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_ZIPCODE, zipcode);
        editor.putString(USER_TYPE, usertype);

        editor.commit();
    }

    public void NeedLogin(String email,String password,String name,String type,String id,String facebook_id ,String address,String city,String state,String zipcode,String usertype){
        // Storing login value as TRUE
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putBoolean(IS_LOGIN, false);

        // Storing name in preferences
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_FACEBOOK_ID,facebook_id);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_ZIPCODE, zipcode);
        editor.putString(USER_TYPE, usertype);

        editor.commit();
    }

    public void savelocation(String lat,String lon){

        // Storing name in preferences
        editor.putString(LATITUDE, lat);
        editor.putString(LONGITUDE, lon);

        editor.commit();
    }

    public  HashMap<String, String> getlocation(){
        HashMap<String, String> loc = new HashMap<String, String>();
        // Storing name in preferences
        loc.put(LATITUDE, pref.getString(LATITUDE, ""));
        loc.put(LONGITUDE, pref.getString(LONGITUDE, ""));

       return loc;
    }


    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_TYPE, pref.getString(KEY_TYPE, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_ZIPCODE, pref.getString(KEY_ZIPCODE, null));
        user.put(KEY_STATE, pref.getString(KEY_STATE, null));
        user.put(USER_TYPE, pref.getString(USER_TYPE, null));
        user.put(KEY_FACEBOOK_ID , pref.getString(KEY_FACEBOOK_ID, null));

        user.put(ID, pref.getString(ID, null));
        user.put(USERNAME, pref.getString(USERNAME, null));
        user.put(PASSWORD, pref.getString(PASSWORD, null));
        user.put(EMAIL, pref.getString(EMAIL, null));
        user.put(CITY, pref.getString(CITY, null));
        user.put(TYPE, pref.getString(TYPE, null));
        user.put(ADDRESS, pref.getString(ADDRESS, null));
        user.put(ZIPCODE, pref.getString(ZIPCODE, null));
        user.put(STATE, pref.getString(STATE, null));

        // return user
        return user;
    }

    public static Boolean getLoginStatus()
    {
        return pref.getBoolean(LOGIN_STATUS,false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public static Boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
