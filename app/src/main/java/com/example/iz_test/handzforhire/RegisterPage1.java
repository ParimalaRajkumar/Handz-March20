package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterPage1 extends Activity{

    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page1);

        Button next = (Button) findViewById(R.id.next3);
        TextView txt1 = (TextView) findViewById(R.id.text1);
        TextView txt2 = (TextView) findViewById(R.id.text2);
        TextView txt3 = (TextView) findViewById(R.id.text3);
        TextView txt4 = (TextView) findViewById(R.id.text4);
        TextView txt5 = (TextView) findViewById(R.id.text5);
        TextView txt6 = (TextView) findViewById(R.id.text6);
        TextView txt7 = (TextView) findViewById(R.id.text7);
        TextView txt8 = (TextView) findViewById(R.id.text8);

        String fontPath1 = "fonts/LibreFranklin_Bold.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

        String fontPath = "fonts/LibreFranklin_Medium.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf1);
        txt2.setTypeface(tf1);
        txt3.setTypeface(tf);
        txt4.setTypeface(tf);
        txt5.setTypeface(tf1);
        txt6.setTypeface(tf);
        txt7.setTypeface(tf);
        txt8.setTypeface(tf);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterPage1.this, RegisterPage2.class);
                startActivity(i);
                finish();
            }
        });
    }
}
