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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangeCurrentUsernamePassword extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    EditText change_username,pass1,retype_pass;
    Button update;
    ImageView h_icon;
    RelativeLayout layout;
    String user,pass,re_pass;
    private static final String URL = Constant.SERVER_URL+"get_username";
    private static final String AUTH_URL = Constant.SERVER_URL+"user_authentication_update";
    String usertype = "employer";
    String value = "HandzForHire@~";
    String dev = "";
    String uid,user_name;
    public static String KEY_USERNAME = "username";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String KEY_PASSWORD = "password";
    TextView username,text,text1;

    String address,city,state,zipcode;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_current_username_password);

        update = (Button) findViewById(R.id.update);

        change_username = (EditText) findViewById(R.id.change_username);
        pass1 = (EditText) findViewById(R.id.pass1);
        retype_pass = (EditText) findViewById(R.id.retype_pass);
        username = (TextView) findViewById(R.id.user);
        h_icon = (ImageView)findViewById(R.id.logo);
        text = (TextView) findViewById(R.id.text1);
        text1 = (TextView) findViewById(R.id.text2);

        Intent i = getIntent();
        uid = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("hhhhhhhhhhhhh:" + uid);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(tf);
        update.setTypeface(tf);
        text1.setTypeface(tf);
        username.setTypeface(tf);

        String fontPath1 = "fonts/calibri.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        change_username.setTypeface(tf1);
        pass1.setTypeface(tf1);
        retype_pass.setTypeface(tf1);

        detector = new SimpleGestureFilter(this,this);

        webservice();

        layout = (RelativeLayout) findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

       /* change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_username.setHint("");
            }
        });
        pass1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pass1.setHint("");
            }
        });
        retype_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                retype_pass.setHint("");
            }
        });*/

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = change_username.getText().toString().trim();
                pass = pass1.getText().toString().trim();
                re_pass = retype_pass.getText().toString().trim();
                String old_username = username.getText().toString();

                if(!pass.equals(""))
                {
                    if (pass.length() < 8) {
                        // custom dialog
                        final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Password is too short, Please input with 8-32 characters");
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
                    if (pass.length() > 32) {
                        // custom dialog
                        final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Password is too long, Please input with 8-32 characters");
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
                    if (TextUtils.isEmpty(re_pass)) {
                        // custom dialog
                        final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Retype Password\" Box");
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

                }

                if(!pass.equals(re_pass))
                {
                    final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Password and Re-type Password does not match");
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
                }
                else
                {
                    if(user.equals(old_username))
                    {
                        final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Username already exists");
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
                    }
                    else
                    {
                        updateAuthentication();
                    }
                }
            }
        });

        h_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent i = new Intent(ChangeCurrentUsernamePassword.this, EditUserProfile.class);
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

    public void webservice() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, uid);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int requesttype) {
        String status = null;

        String user_name = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                user_name = jResult.getString("username");
                System.out.println("resssssss:email:" + user_name);
                username.setText(user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void updateAuthentication()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AUTH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resp:"+response);
                        onResponserecieved(response,5);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
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
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("eeeeeeeeeeeeeeeror:"+jsonObject);

                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {
                        }
                        }
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(XAPP_KEY,value);
                map.put(KEY_USERNAME,user);
                map.put(KEY_USERID,uid);
                map.put(KEY_PASSWORD,pass);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

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
                final Dialog dialog = new Dialog(ChangeCurrentUsernamePassword.this);
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
                        Intent i = new Intent(ChangeCurrentUsernamePassword.this, ProfilePage.class);
                        i.putExtra("userId", uid);
                        startActivity(i);
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
            /*case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
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
