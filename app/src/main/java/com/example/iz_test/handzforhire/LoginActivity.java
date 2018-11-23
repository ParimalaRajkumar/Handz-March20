package com.example.iz_test.handzforhire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.json.JSONObject;
import java.util.HashMap;


public class LoginActivity extends AbsLoginPage  {

    @Override
    void handleViewClick(int resId) {

        switch (resId)
        {
            case R.id.new_employee:
                Intent i = new Intent(LoginActivity.this, RegisterPage2.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    int getResourceLayout() {
        return R.layout.need_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParameters();
        initFacebookViews();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(i);
            }
        });
    }
    @Override
    void launchProfile() {
        session.NeedLogin(user_email,user_password,user_name,userType,user_id,facebook_user_id,"","","","","user_type");
        Intent i = new Intent(LoginActivity.this,ProfilePage.class);
        startActivity(i);
        finish();
    }

    @Override
    void launchRegister() {
        Intent i = new Intent(LoginActivity.this, RegisterPage3.class);
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
        session.savePaypalRedirect("5");
        startActivity(i);
    }
}
