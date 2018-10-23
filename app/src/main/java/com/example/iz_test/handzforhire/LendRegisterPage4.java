package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LendRegisterPage4 extends Activity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lend_register_page4);

            Button profile = (Button) findViewById(R.id.profile);
            TextView text1 = (TextView) findViewById(R.id.text1);
            TextView text2 = (TextView) findViewById(R.id.text2);
            TextView text6 = (TextView) findViewById(R.id.text6);

            String fontPath1 = "fonts/LibreFranklin_Bold.ttf";
            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

            String fontPath = "fonts/LibreFranklin_Medium.ttf";
            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
            text1.setTypeface(tf1);
            text2.setTypeface(tf);
            text6.setTypeface(tf);

            Intent i = getIntent();
            final String id = i.getStringExtra("userId");
            final String user_name = i.getStringExtra("username");
            final String email = i.getStringExtra("email");
            final String address = i.getStringExtra("address");
            final String city = i.getStringExtra("city");
            final String state = i.getStringExtra("state");
            final String zipcode = i.getStringExtra("zipcode");
            System.out.println("iiiiiiiiiiiiiiiiiiiii:"+id);
            System.out.println("iiiiiiiiiiiiiiiiiiiii:username:"+user_name);

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LendRegisterPage4.this, MapActivity.class);
                    i.putExtra("userId",id);
                    i.putExtra("username",user_name);
                    i.putExtra("email",email);
                    i.putExtra("address",address);
                    i.putExtra("state",state);
                    i.putExtra("city",city);
                    i.putExtra("zipcode",zipcode);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
