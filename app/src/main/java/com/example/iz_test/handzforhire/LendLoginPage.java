package com.example.iz_test.handzforhire;

import android.Manifest;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.Config;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_PHONE_STATE;

public class LendLoginPage extends AbsLoginPage  {


    @Override
    void handleViewClick(int resId) {
        Intent intent = null;
        switch(resId)
        {
            case R.id.new_employee:
                intent = new Intent(LendLoginPage.this, LendRegisterPage2.class);
                break;
            case R.id.forgot_password:
                intent = new Intent(LendLoginPage.this,ForgotPassword.class);
                break;
                default:
                    break;
        }

        if(intent != null)
        {
            startActivity(intent);
            finish();
        }
    }

    @Override
    int getResourceLayout() {

        return  R.layout.lend_login_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void launchProfile() {

        session.LendLogin(user_email,user_password,user_name,userType,user_id,facebook_user_id,"","","","","user_type");
        Intent i = new Intent(LendLoginPage.this,MapActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    void launchRegister() {

        Intent i = new Intent(LendLoginPage.this, RegisterPage3.class);
        HashMap<String,String> map= new HashMap<String, String>();
        i.putExtra("isfrom", "reg");
        map.put("firstname",user_first_name);
        map.put("lastname",user_last_name);
        map.put("picture",user_profile_pic);
        map.put("id",facebook_user_id);
        map.put("name",user_name);
        map.put("email",user_email);
        map.put("devicetoken",deviceId);
        map.put("user_type","employee");
        JSONObject objects = new JSONObject(map);
        session.saveregistrationdet(objects.toString());
        session.savePaypalRedirect("6");
        startActivity(i);
    }

}
