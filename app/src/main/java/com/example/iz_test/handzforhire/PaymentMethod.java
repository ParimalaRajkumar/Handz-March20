package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PaymentMethod extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    Button credit,account;
    LinearLayout paypal;
    ImageView logo;
    public static final int PAYPAL_REQUEST_CODE = 123;

    String email;
    String add,ema,ev,ph,uv;
    String name,userid,address1,email1,phone;
    String user_id;
    String address,city,state,zipcode;
    TextView text,text1;
    private SimpleGestureFilter detector;
    private static final String PAYPALUSER_INFO = Constant.SERVER_URL +"paypal_user_info_add";
    public static final String XAPP_KEY = "X-APP-KEY";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERTYPE = "usertype";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_EMAILVERIFIED = "email_verified";
    public static final String KEY_USERVERIFIED = "user_verified";
     String value = "HandzForHire@~";
     String usertype="employer";
     String verified;
    Dialog dialog;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
             .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            //.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            //.clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            .merchantName("HandzForHire")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.homeadvisor.com/rfs/aboutus/privacyPolicy.jsp"))
            .merchantUserAgreementUri(Uri.parse("https://www.homeadvisor.com/servlet/TermsServlet"));


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);

        credit = (Button) findViewById(R.id.add_credit);
        account = (Button) findViewById(R.id.add_account);
        paypal = (LinearLayout) findViewById(R.id.paypal_layout);
        logo = (ImageView) findViewById(R.id.logo);
        text = (TextView) findViewById(R.id.text1);
        text1 = (TextView) findViewById(R.id.text2);


        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");

        detector = new SimpleGestureFilter(this,this);
        dialog = new Dialog(PaymentMethod.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        credit.setTypeface(tf);
        account.setTypeface(tf);
        text1.setTypeface(tf);
        text.setTypeface(tf);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


            paypal.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentMethod.this, PayPalProfileSharingActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);

            }

        });
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                //PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                System.out.println("authorization "+auth.toJSONObject());
                if (auth != null) {
                    String authorization_code = auth.getAuthorizationCode();
                    getAccessToken(authorization_code);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private PayPalOAuthScopes getOauthScopes() {

        HashSet<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_PHONE);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_PROFILE);
        return new PayPalOAuthScopes(scopes);
    }


    private String getAccessToken(String authorizationCode)
    {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(PayPalConfig.PAYPAL_TOKEN_URL);
        //HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/oauth2/token");

        try {
            //String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
             String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
            byte[] data = text.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.addHeader("Authorization", "Basic " + base64);
            StringEntity se=new StringEntity("grant_type=authorization_code&response_type=token&redirect_uri=urn:ietf:wg:oauth:2.0:oob&code="+authorizationCode);
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("authorizatio code "+authorizationCode);
            System.out.println("authorizatio code "+authorizationCode);
            Log.d("Response", responseContent );
            try {
                JSONObject obj = new JSONObject(responseContent);
                System.out.println(obj.getString("access_token"));
                getCustomerinformation(obj.getString("access_token"));
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }
            System.out.println("Response "+responseContent);

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());

        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());

        }
        return null;
    }

    private String getCustomerinformation(String accesstoken)
    {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(PayPalConfig.PAYPAL_CUSTOMER_INFO_URL);
        //HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/identity/openidconnect/userinfo/?schema=openid");
        try {

            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            Log.d("Response", responseContent );
            try {
                JSONObject obj = new JSONObject(responseContent);
                add=obj.getString("address");
                ev=obj.getString("email_verified");
                uv=obj.getString("verified");
                ph=obj.getString("phone_number");
                ema=obj.getString("email");



                JSONObject address = new JSONObject(obj.getString("address"));




            }catch (Exception e)

            {
                System.out.println("e "+e.getMessage());
            }
              addpay();
              System.out.println("Response "+responseContent);

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());

        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());

        }
        return null;
    }

    private void addpay()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PAYPALUSER_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ppppp:Pay::" +response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(PaymentMethod.this);
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
                                    final Dialog dialog = new Dialog(PaymentMethod.this);
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
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                }
                            } catch (JSONException e) {

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY,value);
                map.put(KEY_USERID,user_id);
                map.put(KEY_USERTYPE,usertype);
                map.put(KEY_ADDRESS,add);
                map.put(KEY_EMAIL,ema);
                map.put(KEY_PHONENUMBER,ph);
                map.put(KEY_EMAILVERIFIED,ev);
                map.put(KEY_USERVERIFIED,uv);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onResponserecieved(String jsonObject, int i)
    {
        System.out.println("response from interface" + jsonObject);
        String status = null;

        try {
            JSONObject jResult = new JSONObject(jsonObject);
            status = jResult.getString("status");
            System.out.println("final"+jResult);
            if (status.equals("success"))
            {
                Intent intent = new Intent(PaymentMethod.this,ProfilePage.class);
                intent.putExtra("userId",user_id);
                intent.putExtra("address",address);
                intent.putExtra("city",city);
                intent.putExtra("state",state);
                intent.putExtra("zipcode",zipcode);
                startActivity(intent);

            } else
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
