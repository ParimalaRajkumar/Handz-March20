package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MakePayment extends Activity implements SimpleGestureFilter.SimpleGestureListener
{
    String job_id,user_id,job_name;
    private static final String URL = Constant.SERVER_URL+"applied_job_detailed_view";
    private static final String CANCEL_URL = Constant.SERVER_URL+"job_canceled";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String USER_TYPE = "user_type";
    public static String STATUS = "status";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TRANS_DATE = "transaction_date";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    ImageView image;
    ProgressDialog progress_dialog;
    TextView name,job_cancel,pay_employee;
    private SimpleGestureFilter detector;
    String employee,job_payment_amount,profile_image,profile_name,user_name,employerId,employeeId,job_payout,paypal_fee,fee_details,estimated_payment;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_payment);
        dialog = new Dialog(MakePayment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        image = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.text);
        pay_employee = (TextView) findViewById(R.id.pay_text);
       // payment_already = (Button) findViewById(R.id.cpa);
        job_cancel = (TextView) findViewById(R.id.jwc);
        TextView txt = (TextView) findViewById(R.id.txt);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        name.setTypeface(tf);
        pay_employee.setTypeface(tf);
        txt.setTypeface(tf);
        job_cancel.setTypeface(tf);

        Intent i = getIntent();
        job_id = i.getStringExtra("job_id");
        user_id = i.getStringExtra("userId");
        job_name = i.getStringExtra("job_name");
        profile_image = i.getStringExtra("image");
        profile_name = i.getStringExtra("profilename");
        user_name = i.getStringExtra("username");
        employerId=i.getStringExtra("employer");
        employeeId=i.getStringExtra("employee");
        job_payout=i.getStringExtra("job_payout");
        paypal_fee=i.getStringExtra("paypalfee");
        estimated_payment=i.getStringExtra("job_estimated_payment");
        job_payment_amount=i.getStringExtra("job_payment_amount");
        fee_details=i.getStringExtra("fee_details");
        System.out.println("pppppppp:profilename:::"+profile_name+"---username::::"+user_name);

        detector = new SimpleGestureFilter(this,this);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakePayment.this,ProfilePage.class);
                i.putExtra("userId",user_id);
                startActivity(i);
            }
        });

        pay_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakePayment.this,PayEmployee.class);
                i.putExtra("jobId",job_id);
                i.putExtra("employerId",user_id);
                i.putExtra("employeeId",employee);
                i.putExtra("jobname",job_name);
                i.putExtra("image",profile_image);
                i.putExtra("profilename",profile_name);
                i.putExtra("username",user_name);
                i.putExtra("job_payout",job_payout);
                i.putExtra("paypalfee",paypal_fee);
                i.putExtra("job_estimated_payment",estimated_payment);
                i.putExtra("job_payment_amount",job_payment_amount);
                i.putExtra("fee_details",fee_details);
                i.putExtra("isfrom", "makepayment");
                startActivity(i);
            }
        });
        job_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showalert();
            }
        });

        if(profile_image==null) {

        }
        else {
            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
        }
        if(profile_name.equals(""))
        {
            name.setText(user_name);
            pay_employee.setText("PAY "+ user_name);
        }
        else
            {
            name.setText(profile_name);
            pay_employee.setText("PAY "+ profile_name);
        }

    }

    public void getJobDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
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
                map.put(EMPLOYER_ID, user_id);
                map.put(JOB_ID, job_id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        System.out.println("vvvvvvv4:"+".."+value+".."+user_id+".."+job_id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void cancelJob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CANCEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:job_cancel:" + response);
                        onResponserecieved1(response, 3);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                DateFormat dateInstance = SimpleDateFormat.getDateInstance();
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat destDf = new SimpleDateFormat("MMMM dd, yyyy");
                String date="";
                Date today = Calendar.getInstance().getTime();
                try {
                    date =  destDf.format(today);

                } catch (Exception e)
                {
                    System.out.println("error " + e.getMessage());
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(JOB_ID, job_id);
                map.put(EMPLOYER_ID, employerId);
                map.put(EMPLOYEE_ID, employeeId);
                map.put(USER_TYPE, "employer");
                map.put(STATUS, "job_canceled");
                map.put(TRANS_DATE, destDf.format(today));
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        System.out.println("vvvvvvv4:"+".."+value+".."+user_id+".."+job_id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String emp_data = null;
        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            System.out.println("jjjjjjjjjjjjjjjob:::emp_data:::" + emp_data);
            if (status.equals("success")) {
                if(i==3){
                    Intent main = new Intent(MakePayment.this,ProfilePage.class);
                    startActivity(main);
                    finish();
                }else {
                    emp_data = jResult.getString("emp_data");
                    JSONArray array = new JSONArray(emp_data);
                    for (int n = 0; n < array.length(); n++) {
                        JSONObject object = (JSONObject) array.get(n);
                        final String username = object.getString("username");
                        profile_image = object.getString("profile_image");
                        employee = object.getString("employee_id");
                        final String profilename = object.getString("profile_name");

                        if (profile_image.equals("")) {

                        } else {
                            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
                        }
                        if (profilename.equals("null")) {
                            name.setText(username);
                        } else {
                            name.setText(profilename);
                        }

                    }
                }
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
    }

    public  void showalert()
    {
        final Dialog dialog = new Dialog(MakePayment.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.job_cancel);


        // set values for custom dialog components - text, image and button
        TextView txt_yes = (TextView) dialog.findViewById(R.id.txt_yes);
        TextView txt_no = (TextView) dialog.findViewById(R.id.txt_no);

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelJob();
                dialog.dismiss();
            }
        });
        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

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
