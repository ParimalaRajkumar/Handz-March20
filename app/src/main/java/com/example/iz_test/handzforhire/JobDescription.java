package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobDescription extends Activity implements SimpleGestureFilter.SimpleGestureListener {

    ImageView profile_image,close;
    TextView profile_name, description, date, time, amount, type,name,apply,rat_val;
    private static final String URL = Constant.SERVER_URL+"job_detail_view";
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    String value = "HandzForHire@~";
    String job_id,user_id,employerId,get_name,get_start_time,get_date,get_amount,get_end_time,get_type,get_profile_name,image,profileimage,profilename,usertype;
    ProgressDialog progress_dialog;
    RelativeLayout rating_lay;
    Dialog dialog;
    String get_user_name,get_first_name;
    LinearLayout flexible_layout;

    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_description);

        dialog = new Dialog(JobDescription.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_name = (TextView) findViewById(R.id.text1);
        description = (TextView) findViewById(R.id.description_text);
        name = (TextView) findViewById(R.id.job_name_text);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        amount = (TextView) findViewById(R.id.amount);
        type = (TextView) findViewById(R.id.duration);
        apply = (TextView) findViewById(R.id.apply_btn);
        close = (ImageView) findViewById(R.id.close_btn);
        rat_val=(TextView)findViewById(R.id.rating_value);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        flexible_layout = (LinearLayout)findViewById(R.id.flexible_lay);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);
        TextView t3 = (TextView) findViewById(R.id.t3);
        TextView t4 = (TextView) findViewById(R.id.t4);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        date.setTypeface(tf);
        time.setTypeface(tf);
        amount.setTypeface(tf);
        type.setTypeface(tf);
        name.setTypeface(tf);
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);
        t4.setTypeface(tf);
        apply.setTypeface(tf);
        rat_val.setTypeface(tf);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        profile_name.setTypeface(tf2);

        detector = new SimpleGestureFilter(this,this);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("userId");
        rat_val.setText(i.getStringExtra("average_rating"));

        getJobDetails();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JobDescription.this,ApplyJob.class);
                i.putExtra("userId",user_id);
                i.putExtra("jobId",job_id);
                i.putExtra("employerId",employerId);
                i.putExtra("job_name",get_name);
                i.putExtra("date",get_date);
                i.putExtra("start_time",get_start_time);
                i.putExtra("end_time",get_end_time);
                i.putExtra("profile_name",get_profile_name);
                i.putExtra("amount",get_amount);
                i.putExtra("type",get_type);
                i.putExtra("image",image);
                i.putExtra("usertype",usertype);
                i.putExtra("username",get_user_name);
                i.putExtra("firstname",get_first_name);
                startActivity(i);
            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JobDescription.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", get_profile_name);
                startActivity(i);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getJobDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:job_description:::" + response);
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(JobDescription.this);
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
                                // custom dialog
                                final Dialog dialog = new Dialog(JobDescription.this);
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
                            } catch (JSONException e) {

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(APP_KEY, value);
                params.put(JOB_ID, job_id);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                JSONObject object = new JSONObject(job_data);
                get_name = object.getString("job_name");
                String get_description = object.getString("description");
                get_date = object.getString("job_date");
                get_start_time = object.getString("start_time");
                get_end_time = object.getString("end_time");
                get_amount = object.getString("job_estimated_payment");
                get_type = object.getString("job_payment_type");
                get_profile_name = object.getString("profile_name");
                image = object.getString("profile_image");
                employerId = object.getString("employer_id");
                usertype = object.getString("usertype");
                String flexible_status = object.getString("job_date_time_flexible");
                get_user_name = object.getString("username");
                get_first_name = object.getString("firstname");
                System.out.println("success:image::::"+image+",,"+get_user_name+",,"+get_first_name);

                if(flexible_status.equals("yes"))
                {
                    flexible_layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    flexible_layout.setVisibility(View.GONE);
                }

                if(get_profile_name.equals(""))
                {
                    profile_name.setText(get_user_name);
                }
                if(get_profile_name.equals("")&&get_user_name.equals(""))
                {
                    profile_name.setText(get_first_name);
                }
                else
                {
                    profile_name.setText(get_profile_name);
                }

                description.setText(get_description);

                type.setText(get_type);
                amount.setText(get_amount);
                name.setText(get_name);

                DateFormat dateInstance = SimpleDateFormat.getDateInstance();
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                try {
                    Date dates = srcDf.parse(get_date);
                    date.setText("" + destDf.format(dates));

                } catch (Exception e)
                {
                    System.out.println("error " + e.getMessage());
                }

                String mStringDate = get_start_time;
                String oldFormat= "HH:mm:ss";
                String newFormat= "hh:mm aaa";

                String formatedDate = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
                Date myDate = null;
                try {
                    myDate = dateFormat.parse(mStringDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
                formatedDate = timeFormat.format(myDate).toUpperCase().replace(".","");
                System.out.println("hhhhhhhhhhhhh:newFormat:::"+formatedDate);

                time.setText(formatedDate);



                if(image.equals(""))
                {
                }
                else
                {
                    if(image!= null && image.contains("http://graph.facebook.com/"))
                    {
                        image = image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
                    }
                    Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile_image);

                }

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
