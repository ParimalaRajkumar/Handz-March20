package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangeCurrentAddress extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    EditText address1,address2,city1,state1,zipcode1;
    String add1,add2,cit,stat,zip;
    Button update;
    RelativeLayout layout;
    private static final String REGISTER_URL = Constant.SERVER_URL+"update_user_address";
    private static final String GET_ADDRESS = Constant.SERVER_URL+"get_user_address";
    public static String ADDRESS1 = "address1";
    public static String ADDRESS2 = "address2";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String ZIPCODE = "zipcode";
    public static String PASSWORD = "password";
    public static String USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String uid,city,state,zipcode;
    ImageView h_icon;
    TextView o_address,o_city,o_state,o_zip,text;
    String address;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_current_address);

        Intent i = getIntent();
        uid = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("hhhhhhhhhhhhh:" + uid);

        detector = new SimpleGestureFilter(this,this);

        update = (Button) findViewById(R.id.update);
        address1 = (EditText) findViewById(R.id.addr1);
        address2 = (EditText) findViewById(R.id.addr2);
        city1 = (EditText) findViewById(R.id.city);
        h_icon = (ImageView)findViewById(R.id.logo);
        state1 = (EditText) findViewById(R.id.state);
        zipcode1 = (EditText) findViewById(R.id.zipcode);
        layout = (RelativeLayout) findViewById(R.id.layout);
        o_address = (TextView) findViewById(R.id.old_address);
        o_city = (TextView) findViewById(R.id.old_city);
        o_state = (TextView) findViewById(R.id.old_state);
        o_zip = (TextView) findViewById(R.id.old_zip);
        text = (TextView) findViewById(R.id.text1);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        o_address.setTypeface(tf);
        o_city.setTypeface(tf);
        o_state.setTypeface(tf);
        o_zip.setTypeface(tf);
        update.setTypeface(tf);
        text.setTypeface(tf);

        String fontPath1 = "fonts/calibri.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        address1.setTypeface(tf1);
        address2.setTypeface(tf1);
        city1.setTypeface(tf1);
        state1.setTypeface(tf1);
        zipcode1.setTypeface(tf1);

        getAddress();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });

        h_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent i = new Intent(ChangeCurrentAddress.this, EditUserProfile.class);
                i.putExtra("userId",uid);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode",zipcode);
                startActivity(i);
                finish();*/
            }
        });

    }

    public void Update()
    {
        add1 = address1.getText().toString().trim();
        add2 = address2.getText().toString().trim();
        cit = city1.getText().toString().trim();
        stat = state1.getText().toString().trim();
        zip = zipcode1.getText().toString().trim();

        if(TextUtils.isEmpty(add1))
        {

            // custom dialog
            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Street Address 1\" Box");
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(TextUtils.isEmpty(cit))
        {
            // custom dialog
            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"City\" Box");
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(TextUtils.isEmpty(stat))
        {
            // custom dialog
            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"State\" Box");
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(TextUtils.isEmpty(zip))
        {
            // custom dialog
            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Zipcode\" Box");
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }

        addressUpdate();

    }

    public void addressUpdate()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                        System.out.println("eeeee:"+response);
                        onResponserecieved(response,3);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
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
                                System.out.println("eeeeeeeeeeeeeeeror:" + jsonObject);

                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map1 = new HashMap<String, String>();
                map1.put(XAPP_KEY,value);
                map1.put(ADDRESS1,add1);
                map1.put(ADDRESS2,add2);
                map1.put(CITY,cit);
                map1.put(STATE,stat);
                map1.put(ZIPCODE,zip);
                map1.put(USERID,uid);
                map1.put(Constant.DEVICE, Constant.ANDROID);
                return map1;
            }

        };

        System.out.println("vvvvvvv5:"+".."+value+".."+add1+".."+add2+".."+cit+".."+stat+".."+zip+".."+uid+"..");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success"))
            {
                final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Updated Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(ChangeCurrentAddress.this, ProfilePage.class);
                        i.putExtra("userId", uid);
                        i.putExtra("address", address);
                        i.putExtra("city", city);
                        i.putExtra("state", state);
                        i.putExtra("zipcode", zipcode);
                        startActivity(i);
                        finish();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setDimAmount(0);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            else
            {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAddress()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                        System.out.println("eeeee:getaddress:::::"+response);
                        onResponserecieved1(response,3);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ChangeCurrentAddress.this);
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
                                System.out.println("eeeeeeeeeeeeeeeror:" + jsonObject);
                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map1 = new HashMap<String, String>();
                map1.put(XAPP_KEY,value);
                map1.put(USERID,uid);
                map1.put(Constant.DEVICE, Constant.ANDROID);
                return map1;
            }

        };

        System.out.println("vvvvvvv5:"+".."+value+".."+uid+"..");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int requesttype) {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success"))
            {
                String address1 = jResult.getString("address1");
                String address2 = jResult.getString("address2");
                String city = jResult.getString("city");
                String state = jResult.getString("state");
                String zipcode = jResult.getString("zipcode");
                System.out.println("eeeee:address::::"+address1+address2+city+state+zipcode);
                o_address.setText(address1 +","+ address2 +",");
                o_city.setText(city);
                o_state.setText(state + ",");
                o_zip.setText(zipcode);

            }
            else
            {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                Intent j = new Intent(getApplicationContext(), SwitchingSide.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                Intent i;
                if(Profilevalues.usertype.equals("1")) {
                    i = new Intent(getApplicationContext(), ProfilePage.class);
                }else{
                    i = new Intent(getApplicationContext(), LendProfilePage.class);
                }
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

                break;
           /* case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;*/

        }
        //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
