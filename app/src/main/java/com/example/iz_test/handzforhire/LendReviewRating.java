package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LendReviewRating extends Activity implements SimpleGestureFilter.SimpleGestureListener{

   public static String  image,id,date,profile_image,profilename,average_rating,comments,avg_rat;
    private static final String URL = Constant.SERVER_URL+"review_rating";
    private static final String LINKEDIN_URL = Constant.SERVER_URL+"linked_in ";
    private static final String GET_AVERAGERAT = Constant.SERVER_URL+"get_average_rating";
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
    int timeout = 60000;
    TextView txt_rating;
    ImageView imageprofile;
    TextView txt_profilename;

    ListView list;
    LinearLayout lin_linkin,lin_linkininfo;
    Button close;
    Dialog dialog;
    private SimpleGestureFilter detector;
    Activity thisActivity;
    String firstnmae,lastnmae,lin_email,lin_id,pictureurl,profileurl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.review_rating);

        dialog = new Dialog(LendReviewRating.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        list = (ListView) findViewById(R.id.listview);
        close = (Button) findViewById(R.id.cancel_btn);
        ImageView image = (ImageView)findViewById(R.id.profile_image);
        super.onCreate(savedInstanceState);
        TextView name = (TextView) findViewById(R.id.t2);
        txt_rating=(TextView)findViewById(R.id.text2);
        lin_linkin=(LinearLayout)findViewById(R.id.lin_linkin);
        lin_linkininfo=(LinearLayout)findViewById(R.id.lin_linkininfo);
        txt_profilename=(TextView)findViewById(R.id.txt_profilename);
        imageprofile=(ImageView)findViewById(R.id.imageprofile);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        profile_image = i.getStringExtra("image");
        profilename = i.getStringExtra("name");
        String username = i.getStringExtra("username");

        detector = new SimpleGestureFilter(this,this);

        completerating();
        getAverageRatigng();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(profilename.equals(""))
        {
            name.setText(username);
        }
        else
        {
            name.setText(profilename);
        }

        Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this, 0, Glideconstants.sCorner, Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);

        lin_linkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(appInstalledOrNot()) {
                   linkedlogin();
                }else{
                    Toast.makeText(getApplicationContext(),"App not installed ",Toast.LENGTH_LONG).show();
                     LinkedInActivity.userid=id;
                    Intent in_linkedin=new Intent(LendReviewRating.this,LinkedInActivity.class);
                    startActivityForResult(in_linkedin,Constant.LINKEDIN_REQUEST);
                }

            }
        });

        thisActivity = this;
    }

    public void getAverageRatigng() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_AVERAGERAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("average rat:" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            txt_rating.setText(object.getString("average_rating"));
                        }catch (Exception e){
                            System.out.println("exception "+e.getMessage());
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, id);
                map.put(TYPE, "employer");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println(" Map "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void completerating() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:job_history::" + response);
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendReviewRating.this);
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
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                             //   if (status.equals("This User Currently Does Not Have Any Ratings")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LendReviewRating.this);
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
                            //    }

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
                params.put(KEY_USERID, id);
                params.put(TYPE, usertype);
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                System.out.println("URL "+URL);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
      //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        System.out.println("Request code "+requestCode);
        System.out.println("Data "+data);

        if(requestCode == Constant.LINKEDIN_REQUEST && resultCode == RESULT_OK)
        {
            if(data != null && data.getExtras().getString("response")!= null)
            {
                try {
                    JSONObject json = new JSONObject(data.getExtras().getString("response"));
                    if(json.getString("status").equals("success"))
                    {
                        completerating();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("review rating response"+jsonobject);

        String status = null;
        String rating_list = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_list.clear();
                rating_list = jResult.getString("rating_lists");
                JSONArray array = new JSONArray(rating_list);
                JSONObject jobj = array.getJSONObject(0);
                if (jobj.isNull("linkedin_data")) {
                    lin_linkin.setVisibility(View.VISIBLE);
                    lin_linkininfo.setVisibility(View.GONE);
                    System.out.println("Null value");
                } else {
                    lin_linkininfo.setVisibility(View.VISIBLE);
                    lin_linkin.setVisibility(View.GONE);
                    String linked_in_data = jobj.getString("linkedin_data");
                    JSONObject obj = new JSONObject(linked_in_data);
                    String id = obj.getString("id");
                    String email = obj.getString("email");
                    String first_name = obj.getString("first_name");
                    String last_name = obj.getString("last_name");
                    String profile_url = obj.getString("profile_url");
                    String picture_url = obj.getString("picture_url");
                    txt_profilename.setText("VIEW " + first_name + " " + last_name);
                    Glide.with(LendReviewRating.this).load(picture_url).apply(new RequestOptions().circleCrop().error(R.drawable.default_profile)).into(imageprofile);
                }
                for (int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    final String employer = object.getString("employer");
                    date = object.getString("job_date");
                    JSONArray emparray = new JSONArray(employer);
                    avg_rat = object.getString("average_rating");
                    for (int a = 0; a < emparray.length(); a++) {
                        JSONObject obj = emparray.getJSONObject(a);
                        image = obj.getString("profile_image");
                        average_rating = obj.getString("rating");
                    }

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("image", image);
                    map.put("average", average_rating);
                    map.put("comments", object.getString("comments"));
                    map.put("date", date);
                    job_list.add(map);

                }


                ReviewAdapter arrayAdapter = new ReviewAdapter(this, job_list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Get the current item from ListView
                        View view = super.getView(position, convertView, parent);
                        if (position % 2 == 1) {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.parseColor("#BF178487"));
                        } else {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                        }
                        return view;
                    }
                };

                // DataBind ListView with items from ArrayAdapter
                list.setAdapter(arrayAdapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);

                    }
                });

                txt_rating.setText("Rating: " + avg_rat);
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
                finish();

                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

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

    public void linkedlogin() {

        LISessionManager.getInstance(getApplicationContext()).init(getParent(), buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess () {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                getPersonelinfo();
            }

            @Override
            public void onAuthError (LIAuthError error){
                // Handle authentication errors
                System.out.println("Error "+error.toString());
            }
        },true);

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }

    public void getPersonelinfo(){
      //  String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email,picture-url,profile_url)";
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,picture-url,picture-urls::(original),positions,date-of-birth,phone-numbers,location)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                System.out.println("json res "+json);
             try{

                 lin_email=json.getString("emailAddress");
                 firstnmae=json.getString("firstName");
                 lastnmae=json.getString("lastName");
                 lin_id=json.getString("id");
                 String pictureUrl="";
                 if(json.has("pictureUrl")){
                     pictureUrl=json.getString("pictureUrl");
                 }
                 pictureurl=pictureUrl;
                 getpublicprofileurl();
              } catch (Exception e)
              {

               }
                System.out.println("Json "+json);
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });
    }

    public void getpublicprofileurl(){
        final String profilurl = "https://api.linkedin.com/v1/people/~:(public-profile-url)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, profilurl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                try{
                    String publicProfileUrl=json.getString("publicProfileUrl");
                    System.out.println("publicProfileUrl "+publicProfileUrl);
                    profileurl=publicProfileUrl;
                    UpdatelinkedingData();
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });

    }
    private boolean appInstalledOrNot() {
        Boolean appinstalled=false;
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            System.out.println("packanme "+packageInfo.packageName);
            if(packageInfo.packageName.equals("com.linkedin.android")) {
                appinstalled = true;
                System.out.println("App installed ");
                break;
            }
        }
            return appinstalled;
    }

    public void UpdatelinkedingData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LINKEDIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  onResponserecieved(response, 1);
                        System.out.println("Response "+response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendReviewRating.this);
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
                                    final Dialog dialog = new Dialog(LendReviewRating.this);
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
                params.put(KEY_USERID, id);
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
}
