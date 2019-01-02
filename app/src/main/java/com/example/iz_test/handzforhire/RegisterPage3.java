package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RegisterPage3 extends AppCompatActivity{

    Button next;
    EditText u_name, pass, re_pass;
    CheckBox check1, check2,check3,check4;

    RelativeLayout layout;
    String first, last, add1, add2, city, state, zip, email, re_email;
    private String TAG = RegisterPage3.class.getSimpleName();


    static String[] hreflink;

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    SessionManager session;
   static Context mcontext;
    TextView handz_condition,feature;
    String  password, retype_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page3);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());
        mcontext = getApplicationContext();

        next = (Button) findViewById(R.id.next1);
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);
       // pass = (EditText) findViewById(R.id.password);
        //re_pass = (EditText) findViewById(R.id.retype_password);
        layout = (RelativeLayout) findViewById(R.id.layout);
        handz_condition = (TextView) findViewById(R.id.handz_condition);
        feature = (TextView) findViewById(R.id.features);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        check3 = (CheckBox) findViewById(R.id.checkBox3);
        check4 = (CheckBox) findViewById(R.id.checkBox4);

        handz_condition.setPaintFlags(handz_condition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        feature.setPaintFlags(feature.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String fontPath = "fonts/calibri.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        check1.setTypeface(tf);
        check2.setTypeface(tf);
        check3.setTypeface(tf);


        check4.setTypeface(tf);

        String fontPath1 = "fonts/calibriItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        feature.setTypeface(tf1);
        handz_condition.setTypeface(tf1);

        String fontPath2 = "fonts/calibri.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
       // pass.setTypeface(tf2);
        //re_pass.setTypeface(tf2);

        String fontPath3 = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
        next.setTypeface(tf3);

        Intent i = getIntent();

       /* if(i.getStringExtra("isfrom").equals("reg")) {

        }else if(i.getStringExtra("isfrom").equals("paypal")){

            System.out.println("Merchant id "+merchantid);

            registerUser();
        }*/


        // permission();

        handz_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPage3.this, TermsAndConditions.class);
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPage3.this, RegisterPage2.class);
                startActivity(i);
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });


        feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(RegisterPage3.this);
                dialog.setContentView(R.layout.popup);

                // set the custom dialog components - text, image and button
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                // if button is clicked, close the custom dialog
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setDimAmount(0);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

       next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (check1.isChecked()&&check2.isChecked()&&check3.isChecked()&&check4.isChecked()) {
                   //
                  Intent in_reg=new Intent(RegisterPage3.this,LinkPaypal.class);
                  startActivity(in_reg);
                }
                else {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Please fill all the required fields");
                    Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    dialog.getWindow().setDimAmount(0);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            }
        });

        // ATTENTION: This was auto-generated to handle app links.


    }



}

