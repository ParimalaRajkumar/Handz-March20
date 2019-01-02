package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IZ-Test on 09-10-2018.
 */

public class JobCancelEditComments extends Activity implements SimpleGestureFilter.SimpleGestureListener{

        Button b1;
        String result;
        TextView t3;
        private static final String URL = Constant.SERVER_URL+"add_rating";
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
        String category1,category2,category3,category4,category5,rating_id,username,average_rating;
        EditText comment;
        String type = "employee";
        ImageView profile;
        RelativeLayout rating_lay;
        TextView pn_needcmd;
        private SimpleGestureFilter detector;
        private static final String GET_AVERAGERAT = Constant.SERVER_URL+"get_average_rating";

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
            TextView rating_text = (TextView) findViewById(R.id.text2);
            TextView comment_text = (TextView) findViewById(R.id.tv1);
            TextView t1 = (TextView) findViewById(R.id.t1);
            TextView t2 = (TextView) findViewById(R.id.t2);

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
            rating_id = i.getStringExtra("ratingId");
            comments = i.getStringExtra("comments");
            username = i.getStringExtra("username");
            System.out.println("rrrrrrrrrrrr:rating_id::" + rating_id);

            String fontPath1 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
            rating_text.setTypeface(tf1);

            String fontPath2 = "fonts/cambriab.ttf";
            Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
            pn_needcmd.setTypeface(tf2);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
            t3.setTypeface(tf);
            t1.setTypeface(tf);
            t2.setTypeface(tf);
            comment_text.setTypeface(tf);

            System.out.println("rrrrrrrrrrrr:comments::" + comments);
            comment.setText(comments);

            if (rating.equals(""))
            {
                getAverageRating();
            }
            else
            {
                t3.setText(rating);
            }
            System.out.println("rrrrrrrrrrrr:ratingId::" + rating+"..."+image+",,,"+rating_id);

            if(profilename.equals(""))
            {
                pn_needcmd.setText(username);
            }
            else
            {
                pn_needcmd.setText(profilename);
            }

            detector = new SimpleGestureFilter(this,this);

            System.out.println("rrrrrrrrrrrr:rating::" + rating+"..."+image);
            if(image.equals(null)||image.equals(""))
            {
            }
            else {
                //  profile.setVisibility(View.INVISIBLE);
                Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
            }

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
                    Intent i = new Intent(JobCancelEditComments.this,ReviewRating.class);
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
                    Intent i = new Intent(JobCancelEditComments.this,ProfilePage.class);
                    startActivity(i);
                    finish();
                }
            });
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
                            try {
                                String responseBody = new String( error.networkResponse.data, "utf-8" );
                                JSONObject jsonObject = new JSONObject( responseBody );
                                System.out.println("error:::"+jsonObject);
                                String status = jsonObject.getString("msg");
                                //  if(!status.equals(""))
                                // {
                                // custom dialog
                                final Dialog dialog = new Dialog(JobCancelEditComments.this);
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
                                dialog.getWindow().setDimAmount(0);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                // }

                            } catch (JSONException e) {

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(XAPP_KEY, value);
                    map.put(KEY_JOBID, job_id);
                    map.put(USERID, employee_id);
                    map.put(KEY_RATING, rating);
                    map.put(KEY_COMMENTS, comments);
                    map.put(KEY_TYPE, type);
                    map.put(LOGIN_USERID,employer_id);
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
                    System.out.println("Parameter "+map);
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
                    Intent intent=new Intent(JobCancelEditComments.this,ProfilePage.class);
                    intent.putExtra("userId",employer_id);
                    startActivity(intent);

                }

            } catch (Exception e) {
            }
        }

        protected Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
            // Initialize a new Bitmap to make it bordered bitmap
            Bitmap dstBitmap = Bitmap.createBitmap(
                    srcBitmap.getWidth() + borderWidth*2, // Width
                    srcBitmap.getHeight() + borderWidth*2, // Height
                    Bitmap.Config.ARGB_8888 // Config
            );
            Canvas canvas = new Canvas(dstBitmap);

            Paint paint = new Paint();
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            paint.setAntiAlias(true);
            Rect rect = new Rect(
                    borderWidth / 2,
                    borderWidth / 2,
                    canvas.getWidth() - borderWidth / 2,
                    canvas.getHeight() - borderWidth / 2
            );
            canvas.drawRect(rect,paint);
            canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
            srcBitmap.recycle();

            // Return the bordered circular bitmap
            return dstBitmap;
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

