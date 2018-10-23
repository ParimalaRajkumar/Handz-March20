package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;


public class TermsAndConditions extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        WebView wv;
        wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl("file:///android_asset/terms_conditions.html");

        ImageView close = (ImageView) findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
