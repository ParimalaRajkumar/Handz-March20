package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.Config;
import com.listeners.ApiResponseListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterPage4 extends Activity  implements ResponseListener1 ,ApiResponseListener<String ,String> {

    TextView text1,text2,text3,text4,text5,text6;
    public static String PASS = "password";
    public static String EMAIL = "email";
    public static String FNAME = "firstname";
    public static String LNAME = "lastname";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String ZIPCODE = "zipcode";
    public static String USERTYPE = "usertype";
    public static String DEVICETOKEN = "devicetoken";
    public static String MERCHANTID = "merchantID";
    public static String XAPP_KEY = "X-APP-KEY";
    ProgressDialog progress_dialog;

    private static final int REQUEST_PHONE_STATE = 0;
    String usertype = "employer";
    String value = "HandzForHire@~";
    int timeout = 60000;
    String deviceId;
    SessionManager session;
    String user_name, password, retype_password, address;
    String first, last, add1, add2, city, state, zip, email, re_email;
    private static final String REGISTER_URL = Constant.SERVER_URL+"user_register";
    String get_email,get_address,get_city,get_state,get_zipcode,user_id,facebook_id,user_type,get_password;
    String merchantid;
    Dialog dialog;
    String registrationdet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page4);

        Button profile = (Button) findViewById(R.id.profile);
        TextView text1 = (TextView) findViewById(R.id.text1);
        TextView text2 = (TextView) findViewById(R.id.text2);
        TextView text6 = (TextView) findViewById(R.id.text6);

        String fontPath3 = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
        profile.setTypeface(tf3);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        text1.setTypeface(tf);
        text2.setTypeface(tf);
        text6.setTypeface(tf);
        session = new SessionManager(getApplicationContext());
        registrationdet  = session.Readreg();

        dialog = new Dialog(RegisterPage4.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        /*Intent i = getIntent();
        final String id = i.getStringExtra("userId");
        final String user_name = i.getStringExtra("username");
        final String email = i.getStringExtra("email");
        final String address = i.getStringExtra("address");
        final String city = i.getStringExtra("city");
        final String state = i.getStringExtra("state");
        final String zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:"+id);
        System.out.println("iiiiiiiiiiiiiiiiiiiii:username:"+user_name);*/

        SharedPreferences shared = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        deviceId = (shared.getString("regId", ""));

        Intent i = getIntent();
        if(i.getStringExtra("isfrom").equals("reg"))
        {
            merchantid="";
        }else if(i.getStringExtra("isfrom").equals("paypal")){
            PaypalCon.partnerReferralPrefillData(session.readReraalapilink(),session.ReadAccessToekn(),this);
        }


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Intent i = new Intent(RegisterPage4.this, ProfilePage.class);
                i.putExtra("userId",id);
                i.putExtra("username",user_name);
                i.putExtra("email",email);
                i.putExtra("address",address);
                i.putExtra("state",state);
                i.putExtra("city",city);
                i.putExtra("zipcode",zipcode);
                startActivity(i);
                finish();*/

                String paypalredirect=session.ReadPaypalRedirect();

                if(paypalredirect.equals("0"))
                {
                    password=session.ReadPass();
                    registerUser();

                }else if(paypalredirect.equals("5")){

                    loginwithfacebook();
                }
            }
        });
    }

    private void registerUser()
    {
        dialog.show();
        try {

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


            address = add1 + add2;

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();

                        System.out.println("eeeee:"+response);
                        onResponserecieved(response,1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(RegisterPage4.this);
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
                            dialog.getWindow().setDimAmount(0);
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
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(RegisterPage4.this);
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
                                    dialog.getWindow().setDimAmount(0);
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
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                    user_type = object.getString("usertype");

                }

                session.NeedLogin(get_email,get_password,user_name,usertype,user_id,get_address,get_city,get_state,get_zipcode,user_type);

                Intent i = new Intent(RegisterPage4.this,ProfilePage.class);
                i.putExtra("userId",user_id);
                i.putExtra("username",user_name);
                i.putExtra("email",get_email);
                i.putExtra("address",get_address);
                i.putExtra("state",get_state);
                i.putExtra("city",get_city);
                i.putExtra("zipcode",get_zipcode);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public void loginwithfacebook() {
        try {

            dialog.show();

                 JSONObject obj = new JSONObject(registrationdet);

                 final String firstname = obj.getString("firstname");

                 final  String lastname = obj.getString("lastname");

                 final  String picture = obj.getString("picture");

                 facebook_id = obj.getString("id");

                 final String name = obj.getString("name");

                 final String email = obj.getString("email");

                 final String devicetoken = obj.getString("devicetoken");

                 final String user_type = obj.getString("user_type");

               // String url = "https://www.handzadmin.com/service/users/facebook";

            String url = "https://www.handzadmin.com/users/facebook";

            StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            dialog.dismiss();
                            onResponserecieveds(response);

                            System.out.println("facebook login response "+response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            //Log.d("Error.Response", response);
                            dialog.dismiss();

                            if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                                final Dialog dialog = new Dialog(RegisterPage4.this);
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
                                dialog.getWindow().setDimAmount(0);
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
                                    System.out.println("json object "+jsonObject);
                                    String status = jsonObject.getString("msg");
                                    if (!status.equals("")) {
                                        // custom dialog
                                        final Dialog dialog = new Dialog(RegisterPage4.this);
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
                                        dialog.getWindow().setDimAmount(0);
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
                    }
            ) {

                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String> ();
                    params.put("X-APP-KEY", value);
                    params.put("email", email);
                    params.put("firstname", firstname);
                    params.put("lastname", lastname);
                    params.put("name", name);
                    params.put("user_type", user_type);
                    params.put("devicetoken", devicetoken);
                    params.put("merchantID", merchantid);
                    params.put("id", facebook_id);
                    params.put("profilePicture", picture);
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(putRequest);

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
    }


    public void onResponserecieveds(String jsonobject) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String userdata = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            userdata = jResult.getString("userdata");

            if(status.equals("success"))
            {
                JSONObject object = new JSONObject(userdata);

                    user_id = object.getString("id");
                    user_name = object.getString("username");
                    get_email = object.getString("email");
                    get_address = "";
                    get_city ="";
                    get_state = "";
                    get_zipcode = "";
                    user_type = object.getString("usertype");



                session.NeedLogin(get_email,get_password,user_name,usertype,user_id,facebook_id,get_address,get_city,get_state,get_zipcode,user_type);

                Intent i = new Intent(RegisterPage4.this,ProfilePage.class);
                i.putExtra("userId",user_id);
                i.putExtra("username",user_name);
                i.putExtra("email",get_email);
                i.putExtra("address",get_address);
                i.putExtra("state",get_state);
                i.putExtra("city",get_city);
                i.putExtra("zipcode",get_zipcode);
                startActivity(i);
                finish();
            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Exception e"+e.getMessage());
        }
    }


    @Override
    public void OnResponseReceived(String s, String s2) {
        merchantid = s;
        //UpdatePaypal();
    }
}
