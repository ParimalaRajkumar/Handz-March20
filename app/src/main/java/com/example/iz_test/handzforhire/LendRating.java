package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LendRating extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    Button nxt;
    private RatingBar rb1,rb2,rb3,rb4,rb5;
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    TextView ra;
    float average;
    String job_id,employer_id,employee_id,user_id,image,profilename,profile_image;
    String category1,category2,category3,category4,category5;
    ImageView profile;
    RelativeLayout rating_lay;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String rating;
    TextView prof;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_rating);

        nxt = (Button) findViewById(R.id.next);
        rb1 = (RatingBar) findViewById(R.id.ratingBar1);
        rb2 = (RatingBar) findViewById(R.id.ratingBar2);
        rb3 = (RatingBar) findViewById(R.id.ratingBar3);
        rb4 = (RatingBar) findViewById(R.id.ratingBar4);
        rb5 = (RatingBar) findViewById(R.id.ratingBar5);
        ra = (TextView) findViewById(R.id.text3);
        prof=(TextView)findViewById(R.id.text1);
        profile = (ImageView) findViewById(R.id.profile_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        TextView rating_text = (TextView) findViewById(R.id.text2);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("user_id");
        employer_id = i.getStringExtra("employer_id");
        employee_id = i.getStringExtra("employee_id");
        profilename = i.getStringExtra("profilename");
        prof.setText(profilename);

        String fontPath1 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        rating_text.setTypeface(tf1);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        prof.setTypeface(tf2);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        ra.setTypeface(tf);

        detector = new SimpleGestureFilter(this,this);

        getProfileimage();

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendRating.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        nxt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                category1 = String.valueOf(rb1.getRating());
                System.out.println("rrrrrrrrrrrr:category1::" + category1);
                category2 = String.valueOf(rb2.getRating());
                System.out.println("rrrrrrrrrrrr::category2::" + category2);
                category3 = String.valueOf(rb3.getRating());
                System.out.println("rrrrrrrrrrrr:category3::" + category3);
                category4 = String.valueOf(rb4.getRating());
                System.out.println("rrrrrrrrrrrr::category4::" + category4);
                category5 = String.valueOf(rb5.getRating());
                System.out.println("rrrrrrrrrrrr:category5::" + category5);
                float total = 0;
                total += rb1.getRating();
                total += rb2.getRating();
                total += rb3.getRating();
                total += rb4.getRating();
                total += rb5.getRating();
                float average = total / 5;
                average = Math.round(average);
                ra.setText(String.valueOf(average));
                System.out.println("rrrrrrrrrrrr" + average);
                TextView ra = (TextView) findViewById(R.id.text3);
                rating = ra.getText().toString();

                Intent i = new Intent(LendRating.this, LendComments.class);
                i.putExtra("rating", rating);
                i.putExtra("userId", user_id);
                i.putExtra("jobId", job_id);
                i.putExtra("image",profile_image);
                i.putExtra("employerId", employer_id);
                i.putExtra("employeeId", employee_id);
                i.putExtra("category1",category1);
                i.putExtra("category2",category2);
                i.putExtra("category3",category3);
                i.putExtra("category4",category4);
                i.putExtra("category5",category5);
                i.putExtra("name", profilename);

                startActivity(i);

            }
        });

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating1,
                                        boolean fromUser) {


            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating2,
                                        boolean fromUser) {


            }
        });
        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating3,
                                        boolean fromUser) {


            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating4,
                                        boolean fromUser) {


            }
        });
        rb5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()

        {
            public void onRatingChanged(RatingBar ratingBar, float rating5,
                                        boolean fromUser) {


            }
        });
    }

    public void getProfileimage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, user_id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;

        profile_image = null;

        profilename = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success"))
            {
                profile_image = jResult.getString("profile_image");
                profilename = jResult.getString("profile_name");

                if(profile_image.equals(""))
                {
                }
                else {

                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);

                }
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
