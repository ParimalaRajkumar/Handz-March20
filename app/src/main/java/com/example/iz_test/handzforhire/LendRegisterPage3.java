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
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LendRegisterPage3 extends Activity implements ResponseListener1{

        Button next;
        EditText u_name, pass, re_pass;
        CheckBox check1, check2,check3,check4;
        String user_name, password, retype_password, address;
        RelativeLayout layout;
        String first, last, add1, add2, city, state, zip, email, re_email;
        private String TAG = RegisterPage3.class.getSimpleName();
        private static final String REGISTER_URL = Constant.SERVER_URL+"user_register";
        String get_email,get_address,get_city,get_state,get_zipcode,user_id;

        public static String PASS = "password";
        public static String EMAIL = "email";
        public static String FNAME = "firstname";
        public static String LNAME = "lastname";
        public static String ADDRESS = "address";
        public static String CITY = "city";
        public static String MERCHANTID = "merchantID";
        public static String STATE = "state";
        public static String ZIPCODE = "zipcode";
        public static String USERTYPE = "usertype";
        public static String DEVICETOKEN = "devicetoken";
        public static String XAPP_KEY = "X-APP-KEY";
        ProgressDialog progress_dialog;
        TextView handz_condition,feature;
        String usertype = "employee";
        String value = "HandzForHire@~";
        String deviceId;
         int timeout = 60000;
        SessionManager session;
        String merchantid;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lend_register_page3);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            session = new SessionManager(getApplicationContext());

            next = (Button) findViewById(R.id.next1);
            check1 = (CheckBox) findViewById(R.id.checkBox1);
            check2 = (CheckBox) findViewById(R.id.checkBox2);
            pass = (EditText) findViewById(R.id.password);
            re_pass = (EditText) findViewById(R.id.retype_password);
            layout = (RelativeLayout) findViewById(R.id.layout);
            handz_condition = (TextView) findViewById(R.id.handz_condition);
            feature = (TextView) findViewById(R.id.features);
            ImageView logo = (ImageView)findViewById(R.id.logo);
            check3 = (CheckBox) findViewById(R.id.checkBox3);
            check4 = (CheckBox) findViewById(R.id.checkBox4);

            handz_condition.setPaintFlags(handz_condition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            feature.setPaintFlags(feature.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            deviceId = LendLoginPage.deviceId;
            System.out.println("8888888:device:register:::"+deviceId);

            String fontPath = "fonts/calibri.ttf";
            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
            check1.setTypeface(tf);
            check2.setTypeface(tf);
            check3.setTypeface(tf);
            check4.setTypeface(tf);

            Intent i = getIntent();

           /* first = i.getStringExtra("firstname");
            System.out.println("ffffffff:" + first);
            last = i.getStringExtra("lastname");
            System.out.println("ffffffff:" + last);
            add1 = i.getStringExtra("address1");
            System.out.println("ffffffff:" + add1);
            add2 = i.getStringExtra("address2");
            System.out.println("ffffffff:" + add2);
            city = i.getStringExtra("city");
            System.out.println("ffffffff:" + city);
            state = i.getStringExtra("state");
            System.out.println("ffffffff:" + state);
            zip = i.getStringExtra("zip");
            System.out.println("ffffffff:" + zip);
            email = i.getStringExtra("email");
            System.out.println("ffffffff:" + email);
            re_email = i.getStringExtra("retype_email");
            System.out.println("ffffffff:" + re_email);*/

            String registrationdet  = session.Readreg();

            System.out.println("Registration details "+registrationdet);

            /*try {

                JSONObject obj = new JSONObject(registrationdet);

                first = obj.getString("firstname");

                last = obj.getString("lastname");

                add1 = obj.getString("address1");

                add2 = obj.getString("address2");

                city = obj.getString("city");

                state =obj.getString("state");

                zip = obj.getString("zip");

                email = obj.getString("email");

                re_email =obj.getString("retype_email");

            }catch (Exception e){
                System.out.println("Exception "+e.getMessage());
            }*/


            if(i.getStringExtra("isfrom").equals("reg")) {

            }else if(i.getStringExtra("isfrom").equals("paypal")){
                merchantid= PaypalCon.partnerReferralPrefillData(session.readReraalapilink(),session.ReadAccessToekn());
                System.out.println("Merchant id "+merchantid);
                password=session.ReadPass();
                registerUser();
            }

            address = add1 + add2;
            System.out.println("ffffffff:add:" + address);

            handz_condition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LendRegisterPage3.this, TermsAndConditions.class);
                    startActivity(i);
                }
            });

            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LendRegisterPage3.this,LendRegisterPage2.class);
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
                    final Dialog dialog = new Dialog(LendRegisterPage3.this);
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (check1.isChecked()&&check2.isChecked()&&check3.isChecked()&&check4.isChecked()) {
                        //registerUser();

                        Intent in_reg=new Intent(LendRegisterPage3.this,LinkPaypal.class);
                        startActivity(in_reg);
                    }
                    else {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage3.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Please fill all the required fields");
                        Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        Window window = dialog.getWindow();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                }
            });
        }

    private void registerUser()
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                            System.out.println("eeeee:"+response);
                            onResponserecieved(response,1);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                                final Dialog dialog = new Dialog(LendRegisterPage3.this);
                                dialog.setContentView(R.layout.custom_dialog);
                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("Error Connecting To Network");
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            }else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                            }else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    JSONObject jsonObject = new JSONObject(responseBody);
                                    System.out.println("volley error::: " + jsonObject);
                                    String status = jsonObject.getString("msg");
                                    if (!status.equals("")) {
                                        // custom dialog
                                        final Dialog dialog = new Dialog(LendRegisterPage3.this);
                                        dialog.setContentView(R.layout.custom_dialog);

                                        // set the custom dialog components - text, image and button
                                        TextView text = (TextView) dialog.findViewById(R.id.text);
                                        text.setText(status);
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
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    }
                                } catch (JSONException e) {
                                    //Handle a malformed json response
                                    System.out.println("volley error ::" + e.getMessage());
                                } catch (UnsupportedEncodingException errors) {
                                    System.out.println("volley error ::" + errors.getMessage());
                                }
                            }

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();

                    params.put(XAPP_KEY,value);
                    params.put(PASS,password);
                    params.put(EMAIL, email);
                    params.put(FNAME,first);
                    params.put(LNAME,last);
                    params.put(ADDRESS,address);
                    params.put(CITY,city);
                    params.put(STATE,state);
                    params.put(ZIPCODE,zip);
                    params.put(USERTYPE,usertype);
                    params.put(DEVICETOKEN,deviceId);
                    params.put(MERCHANTID,merchantid);
                    params.put(Constant.DEVICE, Constant.ANDROID);
                    System.out.println("Params "+params);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

        public void onResponserecieved(String jsonobject, int requesttype) {
            System.out.println("response from interface"+jsonobject);

            String status = null;
            String userdata = null;

            try {

                JSONObject jResult = new JSONObject(jsonobject);

                status = jResult.getString("status");
                userdata = jResult.getString("user_data");

                if(status.equals("success"))
                {
                    JSONObject object = new JSONObject(userdata);
                    for(int n = 0; n < object.length(); n++)
                    {
                        user_id = object.getString("id");
                        user_name = object.getString("username");
                        get_email = object.getString("email");
                        get_address = object.getString("address");
                        get_city = object.getString("city");
                        get_state = object.getString("state");
                        get_zipcode = object.getString("zipcode");

                    }

                    Intent i = new Intent(LendRegisterPage3.this,LendRegisterPage4.class);
                    i.putExtra("userId",user_id);
                    i.putExtra("username",user_name);
                    i.putExtra("email",get_email);
                    i.putExtra("address",get_address);
                    i.putExtra("state",get_city);
                    i.putExtra("city",get_state);
                    i.putExtra("zipcode",get_zipcode);
                    startActivity(i);
                    finish();
                }
                else
                {
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    private String getAccessToken() {

        String Access_Token=PaypalCon.getAccessToken();
        String[] href = PaypalCon.partnerReferralPrefillAPI(Access_Token,LendRegisterPage3.this);
        session.saveAccesstoken(Access_Token);
        session.savePaypalRedirect("2");
        session.saveReraalapilink(href[0]);
        Intent myIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(href[1]));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myIntent,1);

        return null;
    }

    }

