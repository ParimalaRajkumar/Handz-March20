package com.example.iz_test.handzforhire;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterPage3 extends AppCompatActivity implements ResponseListener1{

    Button next;
    EditText u_name, pass, re_pass;
    CheckBox check1, check2,check3,check4;
    String user_name, password, retype_password, address;
    RelativeLayout layout;
    String first, last, add1, add2, city, state, zip, email, re_email;
    private String TAG = RegisterPage3.class.getSimpleName();
    private static final String REGISTER_URL = Constant.SERVER_URL+"user_register";
    String get_email,get_address,get_city,get_state,get_zipcode,user_id,user_type,get_password;

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
    TextView handz_condition,feature;
    private static final int REQUEST_PHONE_STATE = 0;
    String usertype = "employer";
    String value = "HandzForHire@~";
    int timeout = 60000;
    String deviceId;
    SessionManager session;
    Dialog dialog;
    static String[] hreflink;
    String merchantid;
    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;

   static Context mcontext;

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
        pass = (EditText) findViewById(R.id.password);
        re_pass = (EditText) findViewById(R.id.retype_password);
        layout = (RelativeLayout) findViewById(R.id.layout);
        handz_condition = (TextView) findViewById(R.id.handz_condition);
        feature = (TextView) findViewById(R.id.features);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        check3 = (CheckBox) findViewById(R.id.checkBox3);
        check4 = (CheckBox) findViewById(R.id.checkBox4);

       // handz_condition.setPaintFlags(handz_condition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
       // feature.setPaintFlags(feature.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        deviceId = LoginActivity.deviceId;
        String fontPath = "fonts/calibri.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        check1.setTypeface(tf);
        check2.setTypeface(tf);
        check3.setTypeface(tf);

        dialog = new Dialog(RegisterPage3.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        check4.setTypeface(tf);

        String fontPath1 = "fonts/calibriItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        feature.setTypeface(tf1);
        handz_condition.setTypeface(tf1);

        String fontPath2 = "fonts/calibri.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        pass.setTypeface(tf2);
        re_pass.setTypeface(tf2);

        String fontPath3 = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
        next.setTypeface(tf3);

        Intent i = getIntent();

        String registrationdet  = session.Readreg();

        System.out.println("Registration details "+registrationdet);

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

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }



        if(i.getStringExtra("isfrom").equals("reg")) {

        }else if(i.getStringExtra("isfrom").equals("paypal")){
            merchantid= PaypalCon.partnerReferralPrefillData(session.readReraalapilink(),session.ReadAccessToekn());
            System.out.println("Merchant id "+merchantid);
            password=session.ReadPass();
            registerUser();
        }

        address = add1 + add2;
        System.out.println("ffffffff:add:" + address);

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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

       next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  password = pass.getText().toString().trim();
                  retype_password = re_pass.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Must Fill In \"Password\" Box");
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
                    return;
                }
                if (password.length() < 8) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    return;
                }
                if (password.length() > 32) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    return;
                }
                if (TextUtils.isEmpty(retype_password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    return;
                }
                if (!password.equals(retype_password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Password and Retype Password does not match");
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
                    return;
                }
                if (check1.isChecked()&&check2.isChecked()&&check3.isChecked()&&check4.isChecked()) {
                   //
                    session.savepass(password);
                    getAccessToken();
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            }
        });

        // ATTENTION: This was auto-generated to handle app links.


    }

    private void registerUser()
    {
        dialog.show();
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
                            final Dialog dialog = new Dialog(RegisterPage3.this);
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
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(RegisterPage3.this);
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

        System.out.println("values::"+value+".."+password+".."+email+".."+first+".."+last+".."+address+".."+city+".."+state+".."+zip+".."+usertype+".."+deviceId);

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

                Intent i = new Intent(RegisterPage3.this,RegisterPage4.class);

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
        String[] href = PaypalCon.partnerReferralPrefillAPI(Access_Token,RegisterPage3.this);
        session.saveAccesstoken(Access_Token);
        session.savePaypalRedirect("0");
        session.saveReraalapilink(href[0]);
        Intent myIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(href[1]));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myIntent,1);

        return null;
    }

   /* private String[] partnerReferralPrefillAPI(String  accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("https://api.paypal.com/v1/oauth2/token");
        HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/customer/partner-referrals/");

        try {
            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);

            StringEntity se=  new StringEntity(readFromFile(mcontext));

           // StringEntity se=new StringEntity();
            httppost.setEntity(se);

           // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response partner-referrals "+responseContent);

            try {
                JSONObject obj = new JSONObject(responseContent);
                JSONArray array= obj.getJSONArray("links");
                  hreflink=new String[array.length()];
                for(int i=0;i<array.length();i++){
                    JSONObject obj1=array.getJSONObject(i);
                    hreflink[i]=obj1.getString("href");
                }

                session.savePaypalRedirect("0");
                session.saveReraalapilink("0");

              Intent myIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(hreflink[1]));
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(myIntent,1);

                partnerReferralPrefillData(hreflink[0],accesstoken);
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return hreflink;
    }*/

/*

    private  String partnerReferralPrefillData(String  hreflink,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(hreflink);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response partner-referrals Data"+responseContent);
            try {
                JSONObject obj = new JSONObject(responseContent);
                JSONObject obj1=obj.getJSONObject("referral_data");
                JSONObject obj2=obj1.getJSONObject("customer_data");
                JSONArray array=obj2.getJSONArray("partner_specific_identifiers");
                for(int i=0;i<array.length();i++){
                    JSONObject obj3=array.getJSONObject(i);
                    String trackiungvalue=obj3.getString("value");
                    System.out.println("tracking Value "+trackiungvalue);
                    getMerchantIdOfSeller(trackiungvalue,accesstoken);
                }


            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private String getMerchantIdOfSeller(String  trackvalue,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations?tracking_id="+trackvalue);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response MerchantIdOfSeller"+responseContent);
            try {
                JSONObject obj = new JSONObject(responseContent);
                String merchant_id=obj.getString("merchant_id");
                System.out.println("merchant id "+merchant_id);
                getMerchantStatus(merchant_id,accesstoken);
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private String getMerchantStatus(String  merchant_id,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations/"+merchant_id);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response getMerchantStatus"+responseContent);



        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private  String readFromFile(Context context) {

        String ret = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("PartnerReferralPrefilled.json")));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                stringBuilder.append(mLine);
            }

            ret = stringBuilder.toString();
            System.out.println("file "+ret);
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return ret;
    }
*/

}

