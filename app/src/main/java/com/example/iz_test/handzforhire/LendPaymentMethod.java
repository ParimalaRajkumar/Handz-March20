package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

public class LendPaymentMethod extends Activity implements SimpleGestureFilter.SimpleGestureListener
{
    private static final String PAYPAL_LEND = Constant.SERVER_URL +"paypal_user_info_add";
    public static final int PAYPAL_REQUEST_CODE = 123;
    public static final String XAPP_KEY = "X-APP-KEY";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERTYPE = "usertype";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_EMAILVERIFIED = "email_verified";
    public static final String KEY_USERVERIFIED = "user_verified";

    Button credit, account;
    LinearLayout paypal_lend;
    String ladd,lema,lev,lph,luv;
    ImageView logo;

    String user_id;
    String usertype="employee";
    String value = "HandzForHire@~";
    String address, city, state, zipcode;
    private SimpleGestureFilter detector;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfig.PAYPAL_ENVIRONMENT)
            //.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            //.clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            .merchantName("HandzForHire")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.homeadvisor.com/rfs/aboutus/privacyPolicy.jsp"))
            .merchantUserAgreementUri(Uri.parse("https://www.homeadvisor.com/servlet/TermsServlet"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);


        credit = (Button) findViewById(R.id.add_credit);
        account = (Button) findViewById(R.id.add_account);
        paypal_lend = (LinearLayout) findViewById(R.id.paypal_layout);
        logo = (ImageView) findViewById(R.id.logo);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");

        detector = new SimpleGestureFilter(this,this);

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendPaymentMethod.this, LendCreditDebit.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendPaymentMethod.this, LendCheckingAccount.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        paypal_lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LendPaymentMethod.this, PayPalProfileSharingActivity.class);
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
                ladd=obj.getString("address");
                lev=obj.getString("email_verified");
                luv=obj.getString("verified");
                lph=obj.getString("phone_number");
                lema=obj.getString("email");


                JSONObject address = new JSONObject(obj.getString("address"));


            }catch (Exception e)

            {
                System.out.println("e "+e.getMessage());
            }
            laddpay();
            System.out.println("Response "+responseContent);

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());

        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());

        }
        return null;
    }

    private void laddpay()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PAYPAL_LEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Pay" +response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendPaymentMethod.this);
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
                map.put(KEY_ADDRESS,ladd);
                map.put(KEY_EMAIL,lema);
                map.put(KEY_PHONENUMBER,lph);
                map.put(KEY_EMAILVERIFIED,lev);
                map.put(KEY_USERVERIFIED,luv);
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
            System.out.println("lendfinal"+jResult);
            if (status.equals("success"))
            {
                Intent intent = new Intent(LendPaymentMethod.this,LendPaymentMethod.class);
                startActivity(intent);
            } else
            {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
            logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

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
                Intent i = new Intent(getApplicationContext(), LendProfilePage.class);
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