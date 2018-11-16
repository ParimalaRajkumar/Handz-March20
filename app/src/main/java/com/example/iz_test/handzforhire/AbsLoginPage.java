package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.Config;
import com.facebook.AccessToken;
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

public abstract class AbsLoginPage extends Activity implements ResponseListener1{

    LinearLayout layout;
    EditText email, password;
    LoginButton login_withfacebook;
    Button login;
    String email_id, pass;
    protected static final String LOGIN_URL = Constant.SERVER_URL+"login";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DEVICETOKEN = "devicetoken";
    public static final String XAPP_KEY = "X-APP-KEY";
    public static final String KEY_TYPE = "type";
    protected static final String TAG = "";
    public static String deviceId;
    String value = "HandzForHire@~";
    String facebook_user_id ,user_id, user_name, user_email, user_password, user_address, user_city, user_state, user_zipcode,user_type;
    TextView new_employee,forgot;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String type;
    ImageView logo;
    protected static final int REQUEST_PHONE_STATE = 0;
    SessionManager session;
    String userType = "employer";

    protected CallbackManager callbackManager;
    protected AccessToken accessToken;
    Dialog dialog;
    protected String user_first_name ,user_last_name ,user_profile_pic;

    abstract void handleViewClick(int resId);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());

        SharedPreferences shared = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        deviceId = (shared.getString("regId", ""));

        System.out.println("FCm token "+deviceId);

        new_employee = (TextView) findViewById(R.id.new_employee);
        forgot = (TextView) findViewById(R.id.forgot_password);
        layout = (LinearLayout) findViewById(R.id.layout3);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_pass);

        login = (Button) findViewById(R.id.login);
        logo = (ImageView) findViewById(R.id.logo);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        login.setTypeface(tf);

        String fontPath1 = "fonts/calibri.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        email.setTypeface(tf1);
        password.setTypeface(tf1);

        String fontPath2 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        new_employee.setTypeface(tf2);
        forgot.setTypeface(tf2);

        dialog = new Dialog(AbsLoginPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initParameters();
        initFacebookViews();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        new_employee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               handleViewClick(view.getId());
            }
        });
    }
    public void initParameters() {
        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();

    }

    public void initFacebookViews() {

        login_withfacebook = (LoginButton) findViewById(R.id.activity_main_btn_login);

        login_withfacebook.setReadPermissions(Arrays.asList(new String[]{"email","public_profile","user_birthday", "user_hometown"}));

        if (accessToken != null) {
            getProfileData();
        }

// Callback registration
        login_withfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getProfileData();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "User cancel login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook login Error");
            }
        });

    }

    public void getProfileData() {
        try {
            accessToken = AccessToken.getCurrentAccessToken();
            LoginManager.getInstance().logOut();
            // rlProfileArea.setVisibility(View.VISIBLE);
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                user_name = object.getString("name");
                                facebook_user_id = object.getString("id");
                                user_email = object.getString("email");
                                user_first_name = object.getString("first_name");
                                user_last_name = object.getString("last_name");
                                JSONObject picture = object.getJSONObject("picture");
                                JSONObject data = picture.getJSONObject("data");
                                // user_profile_pic = data.getString("url");
                                user_profile_pic = "http://graph.facebook.com/"+facebook_user_id+"/picture?type=large";
                                CheckFacebookId(object);

                                //  Log.d(TAG, “Name :” + name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,gender,email,first_name,last_name,picture");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckFacebookId(JSONObject obj) {

        try {

            dialog.show();

            // String url = "https://www.handzadmin.com/service/users/facebook";
            String url = "https://www.handzadmin.com/users/facebook";

            StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            dialog.dismiss();
                            try {
                                onResponserecieveds(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                                final Dialog dialog = new Dialog(AbsLoginPage.this);
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
                                    System.out.println("json object "+jsonObject);
                                    String status = jsonObject.getString("msg");
                                    if (!status.equals("")) {
                                        // custom dialog
                                        final Dialog dialog = new Dialog(AbsLoginPage.this);
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
                    }
            ) {

                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("X-APP-KEY", value);
                    params.put("email", user_email);
                    params.put("firstname", user_first_name);
                    params.put("lastname", user_last_name);
                    params.put("name", user_name);
                    params.put("user_type", userType);
                    params.put("devicetoken", deviceId);
                    params.put("merchantID", "");
                    params.put("id", facebook_user_id);
                    params.put("profilePicture", user_profile_pic);
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(putRequest);

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
    }

    private void onResponserecieveds(String response) throws JSONException {

        JSONObject object = new JSONObject(response);
        if(object.getString("if_already_exists").equals("yes"))
        {
            JSONObject userdata = object.getJSONObject("userdata");
            user_id = userdata.getString("id");
            user_name = userdata.getString("username");
            user_email = userdata.getString("email");
            user_address = "";
            user_city ="";
            user_state = "";
            user_zipcode = "";
            user_type = userdata.getString("usertype");
            session.NeedLogin(user_email,user_password,user_name,userType,user_id,facebook_user_id,"","","","","user_type");
            Intent i = new Intent(AbsLoginPage.this,ProfilePage.class);
            startActivity(i);
            finish();
        }else
        {
            Intent i = new Intent(AbsLoginPage.this, RegisterPage3.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponserecieved(String jsonobject, int requesttype) {

    }
}
