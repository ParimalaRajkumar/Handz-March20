package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LinkedInActivity extends Activity
{
    private String TAG=LinkedInActivity.class.getSimpleName();

    public static final String LINKED_IN_PEOPLE_PROFILE = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,picture-url)?format=json"; // specific basic details
    final String LINKED_IN_PUBLIC_PROFILE = "https://api.linkedin.com/v1/people/~:(public-profile-url)?format=json";

    private static final String API_KEY = "81qt2f2mg525a4";
    //This is the private api key of our application
    private static final String SECRET_KEY = "E3VnJD4wY2l5x9U7";
    //This is any string of your choice we can use.
    private static final String STATE = "DLKDJF46ikMMZADfdfds";
    //This is the url that LinkedIn Auth 2.0 process will redirect to. We can put whatever we want that starts with http:// or https:// .
    //We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private static final String REDIRECT_URI = "https://www.handzforhire.com";
    private String accessToken="";
    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private WebView webView, webView1;
    //private ProgressDialog pd;

    private static final String LINKEDIN_URL = Constant.SERVER_URL+"linked_in ";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String EMAIL = "email";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String ID = "id";
    public static String PROF_URL = "profile_url";
    public static String PIC_URL = "picture_url";

    String value = "HandzForHire@~";
    public static String TYPE = "type";
    String usertype = "employee";

    String firstnmae,lastnmae,lin_email,lin_id,pictureurl,profileurl;
    public static String userid;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in);
        //get the webView from the layout
        webView = (WebView) findViewById(R.id.main_activity_web_view);
        //Request focus for the webview
        webView.requestFocus(View.FOCUS_DOWN);
        //Show a progress dialog to the user
      //  pd = ProgressDialog.show(this, "", "Loading...", true);

        dialog = new Dialog(LinkedInActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        //Set a custom web view client
        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String authorizationUrl, Bitmap favicon) {
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl){
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                   // Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        //Log.e("Authorize", "State token doesn't match");
                        return true;
                    }
                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                      //  Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    //Log.i("Authorize", "Auth token received: " + authorizationToken);
                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);
                } else {
                    //Default behaviour
                    //Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });
        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        //Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }
    /**
     * Method that generates the url for get the access token from the Service
     * @return Url
     */
    private static String getAccessTokenUrl(String authorizationToken){
        return ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
    }
    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND+STATE_PARAM+EQUALS+STATE
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }
    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute(){
            dialog.show();
          //  pd = ProgressDialog.show(LinkedInActivity.this, "", "Loading...",true);
        }
        @Override
        protected Boolean doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                URL sourceUrl = null;
                try {
                    sourceUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try{
                    HttpURLConnection conn = (HttpURLConnection)sourceUrl.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int responsecode = conn.getResponseCode();
                   // Log.d(TAG, "Response Code is :"+responsecode);
                    if(responsecode == 200){
                        String result = "", line = "";
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        while((line=br.readLine())!=null)
                        {
                            result = result+line;
                        }
                        //Convert the string result to a JSON Object
                        JSONObject resultJson = new JSONObject(result);
                        //Extract data from JSON Response
                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                        accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                        try {
                            GetProfile(accessToken);
                            GetPublicURL(accessToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error "+e.getMessage());
                        }
                      //  Log.e("Tokenm", ""+accessToken);
                        if(expiresIn>0 && accessToken!=null){
                         //   Log.i("Authorize", "This is the access Token: "+accessToken+". It will expires in "+expiresIn+" secs");
                            //Calculate date of expiration
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            return true;
                        }
                    }
                }catch(IOException e){
                  //  Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
                catch (ParseException e) {
                  //  Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                  //  Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean status){
            if(status)
            {
                UpdatelinkedingData();

         //      webView.clearCache(true);
               webView.setVisibility(View.GONE);
             //  finish();
            }

            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }
              //  Log.e(TAG, "Access Token : "+ accessToken);
            }
        }


    private void GetProfile(String access_token) throws Exception {

        URL obj = new URL(LINKED_IN_PEOPLE_PROFILE);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("Host", "api.linkedin.com");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Authorization", "Bearer "+	access_token);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + LINKED_IN_PEOPLE_PROFILE);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println(response.toString());

        JSONObject json = new JSONObject(response.toString());
        lin_email=json.getString("emailAddress");
        firstnmae=json.getString("firstName");
        lastnmae=json.getString("lastName");
        lin_id=json.getString("id");
        String pictureUrl="";
        if(json.has("pictureUrl")){
            pictureUrl=json.getString("pictureUrl");
        }
        pictureurl=pictureUrl;
        System.out.println("profile "+json);

    }

    private void GetPublicURL(String access_token) throws Exception {

        URL obj = new URL(LINKED_IN_PUBLIC_PROFILE);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Host", "api.linkedin.com");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Authorization", "Bearer "+	access_token);


        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + LINKED_IN_PUBLIC_PROFILE);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        JSONObject jsonObj = new JSONObject(response.toString());
        String publicProfileUrl=jsonObj.getString("publicProfileUrl");
        System.out.println("publicProfileUrl "+publicProfileUrl);
        profileurl=publicProfileUrl;
        System.out.println("profile "+jsonObj);

    }
    public void UpdatelinkedingData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LINKEDIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    onResponserecieved(response, 1);
                        setResult(RESULT_OK , new Intent().putExtra("response",response));
                        finish();
                        System.out.println("Response "+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LinkedInActivity.this);
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
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure while performing the request", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network error while performing the request", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                                if (status.equals("This User Currently Does Not Have Any Ratings")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LinkedInActivity.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("This User Currently Does Not Have Any Ratings");
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, userid);
                params.put(FIRST_NAME, firstnmae);
                params.put(LAST_NAME, lastnmae);
                params.put(ID, lin_id);
                params.put(PROF_URL, profileurl);
                params.put(PIC_URL, pictureurl);
                params.put(EMAIL, lin_email);
                params.put(Constant.DEVICE, Constant.ANDROID);

                System.out.println("Params " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

};

