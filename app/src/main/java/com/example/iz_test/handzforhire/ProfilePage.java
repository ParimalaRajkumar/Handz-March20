package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.app.AppConstant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AbsSwipeActivity implements ResponseListener {

    TextView profile_name,rating,rating_value;
    TextView txt_postedjobcnt,txt_activejobscnt,job_historycnt;
    private static final String USERNAME_URL = Constant.SERVER_URL+"get_username";
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String PAYMENT_URL = Constant.SERVER_URL+"check_if_payment_mode";
    private static final String GET_AVERAGERAT = Constant.SERVER_URL+"get_average_rating";
    String id,user_name,email,employer_rating,posted_notification,pending_notification,active_notification,jobhistory_notification;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    Button need_help;
    String address,city,state,zipcode,profile_image,profilename,type;
    ImageView profile,logo,menu,share_need,tutorial;
    ProgressDialog progress_dialog;
    ProgressBar progress;
    View rating_lay,rating_img;
    SessionManager session;
    FrameLayout posted,history,active;
    ProgressBar pb;
    Dialog dialog;

    Activity activity;
    String description="https://www.handzforhire.com";
    String title="Whether you need a hand or would like to lend a hand, Handz for Hire is built to connect you and your neighbors looking to get jobs done. Visit HandzForHire.com or download the app in the App Store or Google Play.\"\n" +
            "along with that website url and logo";

    float x1,x2;
    float y1, y2;
    RequestMethods req;
    HashMap<String, String> user;
    FrameLayout create,edit_profile,edit;

    @Override
    protected void onStart() {
        super.onStart();
        getProfileimage(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page_new);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(ProfilePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edit_profile =  findViewById(R.id.edit_user_profile);
        rating_lay =  findViewById(R.id.text2);
        rating_img =findViewById(R.id.imageView6);
        profile_name = (TextView) findViewById(R.id.text1);
        rating_value = (TextView) findViewById(R.id.text3);
        need_help = (Button) findViewById(R.id.need_help);
        create =  findViewById(R.id.create_job);
        edit =  findViewById(R.id.edit_posted_job);
        posted = (FrameLayout) findViewById(R.id.posted_job);
        active = (FrameLayout) findViewById(R.id.active_job);
        history = (FrameLayout) findViewById(R.id.job_history);
        logo = (ImageView)findViewById(R.id.logo);
        profile = (ImageView)findViewById(R.id.profile_image);
        tutorial=(ImageView)findViewById(R.id.tutorial);
        menu = (ImageView)findViewById(R.id.menu);
        rating = (TextView) findViewById(R.id.text2);
        txt_postedjobcnt = (TextView) findViewById(R.id.txt_postedjobcnt);
        txt_activejobscnt = (TextView) findViewById(R.id.txt_activejobscnt);
        job_historycnt = (TextView) findViewById(R.id.job_historycnt);
        TextView txt = (TextView) findViewById(R.id.textView5);

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        // get name
        user_name = user.get(SessionManager.KEY_USERNAME);
        id = user.get(SessionManager.KEY_ID);
        email = user.get(SessionManager.KEY_EMAIL);
        address = user.get(SessionManager.KEY_ADDRESS);
        city = user.get(SessionManager.KEY_CITY);
        state = user.get(SessionManager.KEY_STATE);
        zipcode = user.get(SessionManager.KEY_ZIPCODE);
        type = user.get(SessionManager.TYPE);
        share_need=(ImageView)findViewById(R.id.sha_need);
        share_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                share();
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(ProfilePage.this,PlayTutorialVideo.class);
                in.putExtra("uri","http://162.144.41.156/~izaapinn/handzforhire/Hand.mp4");
                startActivity(in);
            }
        });


        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        need_help.setTypeface(tf);
        txt.setTypeface(tf);
        rating_value.setTypeface(tf);

        String fontPath1 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        rating.setTypeface(tf1);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        profile_name.setTypeface(tf2);

        //paymentCheck();

       // getProfileimage(getIntent());
        getUsername();
        getAverageRatigng();
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, EditUserProfile.class);
                HashMap<String,String> map= new HashMap<String, String>();
                i.putExtra("isfrom", "edit");
                map.put("userId",id);
                map.put("address",address);
                map.put("city",city);
                map.put("state",state);
                map.put("zipcode",zipcode);
                JSONObject object = new JSONObject(map);
                session.saveregistrationdet(object.toString());
                startActivity(i);
            }
        });

       logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,EditPostedJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,SwitchingSide.class);
                startActivity(i);
            }
        });

        posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    req = new RequestMethods(ProfilePage.this, 1, id, "notificationCountCreateJob");
                    req.execute(RequestMethods.RequestMethod.POST, ProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i = new Intent(ProfilePage.this,PostedJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, CreateJob.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    req = new RequestMethods(ProfilePage.this, 1, id, "notificationCountJobHistory");
                    req.execute(RequestMethods.RequestMethod.POST, ProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i = new Intent(ProfilePage.this,JobHistory.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        rating_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,ReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                i.putExtra("username", user_name);
                startActivity(i);
            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,ReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                i.putExtra("username", user_name);
                startActivity(i);
            }
        });

        need_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,NeedHelp.class);
                i.putExtra("userId", id);
                i.putExtra("email",email);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    req = new RequestMethods(ProfilePage.this, 1, id, "notificationCountActive");
                    req.execute(RequestMethods.RequestMethod.POST, ProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i = new Intent(ProfilePage.this,ActiveJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

    }

    @Override
    AppConstant.SWIPETYPE SwipeType() {
        return AppConstant.SWIPETYPE.LEFTTORIGHT;
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
                            rating_value.setText(object.getString("average_rating"));
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
    public void paymentCheck()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PAYMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("cccccccccccccheck:payment:" + response);
                        onResponserecieved3(response, 3);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );

                            String status = jsonObject.getString("status");
                            if(status.equals("error"))
                            {
                                create.setEnabled(false);
                                create.setAlpha(0.65F);
                                edit.setEnabled(false);
                                edit.setAlpha(0.65F);
                                posted.setEnabled(false);
                                posted.setAlpha(0.65F);
                                active.setEnabled(false);
                                active.setAlpha(0.65F);
                                history.setEnabled(false);
                                history.setAlpha(0.65F);
                            }
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, id);
                System.out.println("Params "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void onResponserecieved3(String jsonobject, int requesttype) {
        String status = null;

        String user_name = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                create.setEnabled(true);
                edit.setEnabled(true);
                posted.setEnabled(true);
                active.setEnabled(true);
                history.setEnabled(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getProfileimage(final Intent intent)
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2 ,intent);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ProfilePage.this);
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
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(ProfilePage.this);
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
                                    return;
                                } else {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(ProfilePage.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("Registration Failed");
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
                                    return;
                                }
                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, id);
                map.put("type", "employer");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved2(String jsonobject, int requesttype ,Intent intent) {
        String status = null;

         profile_image = null;

         profilename = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                if(user.get("facebook_id")!=null )
                {
                    profile_image = "http://graph.facebook.com/"+user.get("facebook_id")+"/picture?type=large";
                }
                else
                {
                    profile_image = jResult.getString("profile_image");
                }

                profilename = jResult.getString("profile_name");
                user_name = jResult.getString("username");
                employer_rating = jResult.getString("employer_rating");
                posted_notification = jResult.getString("notificationCountPosted");
                pending_notification = jResult.getString("notificationCountPending");
                active_notification = jResult.getString("notificationCountActive");
                jobhistory_notification = jResult.getString("notificationCountJobHistory");
                if(!posted_notification.equals("0"))
                {
                    txt_postedjobcnt.setText(posted_notification);
                    txt_postedjobcnt.setVisibility(View.VISIBLE);
                }else{
                    txt_postedjobcnt.setVisibility(View.INVISIBLE);
                }
                if(!active_notification.equals("0"))
                {
                    txt_activejobscnt.setText(active_notification);
                    txt_activejobscnt.setVisibility(View.VISIBLE);
                 }else{
                    txt_activejobscnt.setVisibility(View.INVISIBLE);
                }
                if(!jobhistory_notification.equals("0"))
                {
                    job_historycnt.setText(jobhistory_notification);
                    job_historycnt.setVisibility(View.VISIBLE);
                }else{
                    job_historycnt.setVisibility(View.INVISIBLE);
                }
                if(!profile_image.equals("")&&!profilename.equals(""))
                {
                    profile_name.setText(profilename);
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
                }
                else if(!profile_image.equals("")&&profilename.equals(""))
                {
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
                }
                else if(!profilename.equals("")&&profile_image.equals(""))
                {
                    profile_name.setText(profilename);
                }
                else if(profilename.equals("")&&profile_image.equals(""))
                {
                }
                if(profilename.equals(""))
                {
                    profile_name.setText(user_name);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
            }*/
         catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Timber.d("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    public void getUsername() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERNAME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ProfilePage.this);
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
                                System.out.println("eeeeeeeeeeeeeeeror:" + jsonObject);

                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int requesttype) {
        String status = null;

        String employer_rating = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                user_name = jResult.getString("username");
                System.out.println("resssssss:user_name:" + user_name);
                session.SaveUsername(user_name);
                profile_name.setText(user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void share()
    {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,"HandzForHire");
        share.putExtra(Intent.EXTRA_TEXT,title);
        startActivity(Intent.createChooser(share, "Share link!"));

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
    public void onResponseReceived(JSONObject responseObj, int requestType) {
        System.out.println("json "+responseObj);
    }
}
