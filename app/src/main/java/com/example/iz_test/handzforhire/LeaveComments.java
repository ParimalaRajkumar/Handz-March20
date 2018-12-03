package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LeaveComments extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    Button b1;
    String result;
    TextView t3;
    private static final String URL = Constant.SERVER_URL+"add_rating";
    private static final String GET_AVERAGERAT = Constant.SERVER_URL+"get_average_rating";
    public static String KEY_JOBID = "job_id";
    public static String USERID = "user_id";
    public static String KEY_RATING = "rating";
    public static String KEY_COMMENTS = "comments";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String KEY_TYPE = "type";
    public static String LOGIN_USERID = "login_user_id";
    public static String CATEGORY1 = "category1";
    public static String CATEGORY2 = "category2";
    public static String CATEGORY3 = "category3";
    public static String CATEGORY4 = "category4";
    public static String CATEGORY5 = "category5";
    public static String EMPLOYERID = "employer_id";
    public static String EMPLOYEEID = "employee_id";
    public static String RATING_ID = "rating_id";
    public static String USER_TYPE="user_type";
    String value = "HandzForHire@~";
    String job_id, employer_id, employee_id, rating, comments,user_id,image,profilename;
    String category1,category2,category3,category4,category5,username,average_rating,lend_status;
    EditText comment;
    String type = "employee";
    ImageView profile;
    RelativeLayout rating_lay;
    TextView pn_needcmd;
    private SimpleGestureFilter detector;
    String rating_id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_comments);

        b1 = (Button) findViewById(R.id.next1);
        t3 = (TextView) findViewById(R.id.text3);
        comment = (EditText) findViewById(R.id.edit_text);
        profile = (ImageView) findViewById(R.id.profile_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        pn_needcmd=(TextView)findViewById(R.id.text1);
        ImageView handz = (ImageView) findViewById(R.id.handz_logo);
        ImageView back = (ImageView) findViewById(R.id.back);

        Intent i = getIntent();
        rating = i.getStringExtra("rating");
        user_id = i.getStringExtra("userId");
        job_id = i.getStringExtra("jobId");
        employer_id = i.getStringExtra("employerId");
        employee_id = i.getStringExtra("employeeId");
        category1 = i.getStringExtra("category1");
        category2 = i.getStringExtra("category2");
        category3 = i.getStringExtra("category3");
        category4 = i.getStringExtra("category4");
        category5 = i.getStringExtra("category5");
        image = i.getStringExtra("image");
        profilename = i.getStringExtra("name");
        username = i.getStringExtra("username");

        System.out.println("rrrrrrrrrrrr:lend_status.."+lend_status);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        pn_needcmd.setTypeface(tf2);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        t3.setTypeface(tf);

        detector = new SimpleGestureFilter(this,this);

        if (rating.equals(""))
        {
            getAverageRating();
        }
        else
        {
            t3.setText(rating);
        }
        System.out.println("rrrrrrrrrrrr:ratingId::" + rating+"..."+image+",,,"+rating_id);
        System.out.println("rrrrrrrrrrrr:profilename::" + profilename+"..."+username);

        if(profilename.equals(""))
        {
            pn_needcmd.setText(username);
        }
        else
        {
            pn_needcmd.setText(profilename);
        }

        if(image!= null && image.contains("http://graph.facebook.com/"))
        {
            image = image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
        }
        Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments = comment.getText().toString().trim();
                    post();
            }

        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaveComments.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        handz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaveComments.this,ProfilePage.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("rrrrrrrrrrrrr:rating_response:::"+response);
                        onResponserecieved(response, 2);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LeaveComments.this);
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
                            System.out.println("error:::::"+jsonObject);
                            String status = jsonObject.getString("msg");
                            if(!status.equals(""))
                            {
                                // custom dialog
                                final Dialog dialog = new Dialog(LeaveComments.this);
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

                        } catch (UnsupportedEncodingException error1) {
                        }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_JOBID, job_id);
                map.put(LOGIN_USERID, employee_id);
                map.put(KEY_RATING, rating);
                map.put(KEY_COMMENTS, comments);
                map.put(KEY_TYPE, type);
                map.put(USERID,employer_id);
                map.put(CATEGORY1,category1);
                map.put(CATEGORY2,category2);
                map.put(CATEGORY3,category3);
                map.put(CATEGORY4,category4);
                map.put(CATEGORY5,category5);
                map.put(EMPLOYERID,employer_id);
                map.put(EMPLOYEEID,employee_id);
                map.put(RATING_ID,rating_id);
                map.put(USER_TYPE,type);
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("URL  "+URL);
                System.out.println("parameter "+map);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void onResponserecieved(String jsonobject, int requesttype)
    {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);
            System.out.println("rrrr"+jResult);
            status = jResult.getString("status");

            if (status.equals("success"))
            {
                Intent intent=new Intent(LeaveComments.this,ProfilePage.class);
                intent.putExtra("userId",employer_id);
                startActivity(intent);

            }

        } catch (Exception e) {
        }
    }

    public void getAverageRating() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_AVERAGERAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("average rat:" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            average_rating = object.getString("average_rating");
                            t3.setText(average_rating);
                        }catch (Exception e){
                            System.out.println("exception "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(USERID, user_id);
                map.put(KEY_TYPE, "employer");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println(" Map "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
