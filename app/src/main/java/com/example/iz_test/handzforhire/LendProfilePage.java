package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LendProfilePage extends Activity implements SimpleGestureFilter.SimpleGestureListener,ResponseListener{

    Button job_list,edit,find_map,need_help;
    String id,address,city,state,zipcode,profile_image,profilename,email,username;
    String employee_rating,pending_notification,posted_notification,active_notification,jobhistory_notification;
    TextView user_name;
    private static final String USERNAME_URL = Constant.SERVER_URL+"get_username";
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String GET_AVERAGERAT = Constant.SERVER_URL+"get_average_rating";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    ProgressDialog progress_dialog;
    ImageView profile,handz,menu,share_lend,tutorial;
    TextView profile_name,rating_value;
    RelativeLayout rating_lay;
    SessionManager session;
    FrameLayout pending,active_job,job_history;
    Dialog dialog;
    TextView txt_postedjobcnt,txt_activejobscnt,job_historycnt;
    String tittle="Whether you need a hand or would like to lend a hand, Handz for Hire is built to connect you and your neighbors looking to get jobs done. Visit HandzForHire.com or download the app in the App Store or Google Play.\"\n" +
            "along with that website url and logo";
    private SimpleGestureFilter detector;
    RequestMethods req;
    HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_profile_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       /* progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/


        dialog = new Dialog(LendProfilePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        handz = (ImageView) findViewById(R.id.handz);
        need_help = (Button) findViewById(R.id.need_help);
        job_list = (Button) findViewById(R.id.list_view);
        edit = (Button) findViewById(R.id.edit_user_profile);
        user_name = (TextView) findViewById(R.id.text1);
        rating_value = (TextView) findViewById(R.id.text3);
        profile = (ImageView)findViewById(R.id.profile_image);
        tutorial=(ImageView)findViewById(R.id.tutorial);
        profile_name = (TextView) findViewById(R.id.text1);
        find_map = (Button) findViewById(R.id.find_job);
        pending = (FrameLayout) findViewById(R.id.pending_job);
        active_job = (FrameLayout) findViewById(R.id.active_job);
        job_history = (FrameLayout) findViewById(R.id.job_history);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        menu = (ImageView)findViewById(R.id.menu);
        txt_postedjobcnt = (TextView) findViewById(R.id.txt_postedjobcnt);
        txt_activejobscnt = (TextView) findViewById(R.id.txt_activejobscnt);
        job_historycnt = (TextView) findViewById(R.id.job_historycnt);
        TextView txt = (TextView) findViewById(R.id.txt);

        session = new SessionManager(getApplicationContext());
         user = session.getUserDetails();
        // get name
        //user_name = user.get(SessionManager.USERNAME);
        id = user.get(SessionManager.ID);
        email = user.get(SessionManager.EMAIL);
        address = user.get(SessionManager.ADDRESS);
        city = user.get(SessionManager.CITY);
        state = user.get(SessionManager.STATE);
        zipcode = user.get(SessionManager.ZIPCODE);


        Profilevalues.user_id=id;
        Profilevalues.email=email;
        Profilevalues.address=address;
        Profilevalues.city=city;
        Profilevalues.state=state;
        Profilevalues.zipcode=zipcode;

        detector = new SimpleGestureFilter(this,this);

        share_lend=(ImageView)findViewById(R.id.sha_lend);
        share_lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharelend();
            }
        });

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);
        edit.setTypeface(tf);
        need_help.setTypeface(tf);
        rating_value.setTypeface(tf);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        profile_name.setTypeface(tf2);

        getProfileimage();
        getUsername();
        getAverageRatigng();

       /* handz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendProfilePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendProfilePage.this,LendReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        need_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendProfilePage.this,LendNeedHelp.class);
                i.putExtra("userId", id);
                i.putExtra("email",email);
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
                Intent i = new Intent(LendProfilePage.this,SwitchingSide.class);
                startActivity(i);
            }
        });

        job_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(LendProfilePage.this,SearchJob.class);
                i.putExtra("userId",id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        active_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    req = new RequestMethods(LendProfilePage.this, 1, id, "notificationCountActive");
                    req.execute(RequestMethods.RequestMethod.POST, LendProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i =  new Intent(LendProfilePage.this,LendActiveJobs.class);
                i.putExtra("userId",id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        job_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    req = new RequestMethods(LendProfilePage.this, 1, id, "notificationCountJobHistory");
                    req.execute(RequestMethods.RequestMethod.POST, LendProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i =  new Intent(LendProfilePage.this,LendJobHistory.class);
                i.putExtra("userId",id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(LendProfilePage.this, LendEditUserProfile.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);*/
                Intent i = new Intent(LendProfilePage.this, LendEditUserProfile.class);
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

        find_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LendProfilePage.this, MapActivity.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    req = new RequestMethods(LendProfilePage.this, 1, id, "notificationCountPending");
                    req.execute(RequestMethods.RequestMethod.POST, LendProfilePage.this);
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
                Intent i = new Intent(LendProfilePage.this, PendingJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(LendProfilePage.this,PlayTutorialVideo.class);
                in.putExtra("uri","http://162.144.41.156/~izaapinn/handzforhire/Lend.mp4");
                startActivity(in);
            }
        });

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

        String user_name = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                user_name = jResult.getString("username");
                System.out.println("resssssss:user_name:" + user_name);
                profile_name.setText(user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProfileimage()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
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
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved2(String jsonobject, int requesttype) {
        String status = null;

         profile_image = null;

         profilename = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success")) {
                if(user.get("facebook_id")!=null)
                {
                    profile_image = "http://graph.facebook.com/"+user.get("facebook_id")+"/picture?type=large";
                }
                else
                {
                    profile_image = jResult.getString("profile_image");
                }
                profilename = jResult.getString("profile_name");
                username = jResult.getString("username");
                employee_rating = jResult.getString("employee_rating");
                posted_notification = jResult.getString("notificationCountPosted");
                pending_notification = jResult.getString("notificationCountPending");
                active_notification = jResult.getString("notificationCountActive");
                jobhistory_notification = jResult.getString("notificationCountJobHistory");
                if(!pending_notification.equals("0"))
                {
                    txt_postedjobcnt.setText(pending_notification);
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
                if (!profile_image.equals("") && !profilename.equals("")) {
                    profile_name.setText(profilename);
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
                } else if (!profile_image.equals("") && profilename.equals("")) {
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
                } else if (!profilename.equals("") && profile_image.equals("")) {
                    profile_name.setText(profilename);
                } else {
                    profile_name.setText(username);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
                map.put(TYPE, "employee");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println(" Map "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sharelend()
    {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,"HandzForHire");
        share.putExtra(Intent.EXTRA_TEXT,tittle);
        startActivity(Intent.createChooser(share, "Share link!"));

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
                Intent i = new Intent(getApplicationContext(), LendProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
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

    @Override
    public void onResponseReceived(JSONObject responseObj, int requestType) {
        System.out.println("json "+responseObj);
    }
}
