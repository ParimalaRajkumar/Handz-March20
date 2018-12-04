package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LendActiveJobAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private static final String GET_REQUESTPAYMENT = Constant.SERVER_URL+"request_payment_notification";
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String USER_TYPE = "user_type";
    String value = "HandzForHire@~";
    private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";

    Dialog dialog;
    //Frebase details
    public static String storepath="gs://handzdev-9e758.appspot.com";
    String current_user_id = "OGO6K8nyqKVJ8WQoE02WT5qFc1S2";
    Firebase reference1;
    String child_id,jobid,sender_id,get_user;
    String jobId;
    public LendActiveJobAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Firebase.setAndroidContext(activity);
          reference1 = new Firebase("https://handz-8ac86.firebaseio.com/channels");
        //reference1 = new Firebase("https://handzdev-9e758.firebaseio.com/channels");
        sender_id = current_user_id + Profilevalues.user_id;
        get_user=Profilevalues.username;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.lend_active_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.job_name_text);
        TextView date = (TextView) vi.findViewById(R.id.when);
        TextView expected_duration = (TextView) vi.findViewById(R.id.duration);
        TextView amount = (TextView) vi.findViewById(R.id.pay_amount);
        ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
        Button payment = (Button) vi.findViewById(R.id.payment);
        Button job_details = (Button) vi.findViewById(R.id.job_detail_btn);
        final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
        final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
        final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
        final TextView image_text = (TextView) vi.findViewById(R.id.image1);
        Button chat = (Button) vi.findViewById(R.id.message_btn);
        final TextView message_count = (TextView) vi.findViewById(R.id.message_count);
        final TextView payment_count = (TextView) vi.findViewById(R.id.pay_count);
        TextView t1 = (TextView) vi.findViewById(R.id.t1);
        TextView t2 = (TextView) vi.findViewById(R.id.t2);
        TextView t3 = (TextView) vi.findViewById(R.id.t3);
        TextView t4 = (TextView) vi.findViewById(R.id.t4);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        t1.setTypeface(font);
        t2.setTypeface(font);
        t3.setTypeface(font);
        t4.setTypeface(font);
        job_name.setTypeface(font);
        amount.setTypeface(font);
        expected_duration.setTypeface(font);
        date.setTypeface(font);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("name");

        String get_image = items.get("image");
       if(items.get("image").contains("http://graph.facebook.com/"))
       {
           get_image = get_image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
       }
        final String get_user = items.get("user_name");
        final String get_job_id = items.get("jobId");
        final String user_id = items.get("userId");
        final String jobDate = items.get("jobDate");
        final String start_time = items.get("start_time");
        final String end_time = items.get("end_time");
        final String payment_amount = items.get("payment_amount");
        final String payment_type = items.get("payment_type");
        final String get_employer = items.get("employer");
        final String get_employee = items.get("employee");
        final String channel_id=items.get("channel");
        final String message_notification_count=items.get("message_count");
        final String payment_notification_count=items.get("payment_count");
        System.out.println("success:count:::" + message_notification_count+",,,"+payment_notification_count);

        System.out.println("ppppppppp:payment_type,,,,"+payment_type+",,,"+get_job_id);

        job_name.setText(get_name);
        job_name.setTypeface(font);
        job_id.setText(get_job_id);
        expected_duration.setText(payment_type);
        amount.setText(payment_amount);
        employer_id.setText(get_employer);
        employee_id.setText(get_employee);
        image_text.setText(get_image);

        if(message_notification_count.equals("0"))
        {
            message_count.setVisibility(View.INVISIBLE);
        }
        else
        {
            message_count.setVisibility(View.VISIBLE);
            message_count.setText(message_notification_count);
        }
        if(payment_notification_count.equals("0"))
        {
            payment_count.setVisibility(View.INVISIBLE);
        }
        else
        {
            payment_count.setVisibility(View.VISIBLE);
            payment_count.setText(payment_notification_count);
        }

        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(jobDate);
            date.setText(""+destDf.format(dates));
        }catch (Exception e){

        }

        payment.setTag(position);
        job_details.setTag(position);
        chat.setTag(position);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                HashMap<String, String> items =data.get(pos);
                String username="";
                System.out.println("clicked item "+items);
                requestpayment(items.get("jobId"),items.get("employee"),items.get("employer"));
            }
        });

        job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                HashMap<String, String> items =data.get(pos);
                Intent i = new Intent(activity,JobDetails.class);
                i.putExtra("jobId", items.get("jobId"));
                i.putExtra("userId",items.get("userId"));
                v.getContext().startActivity(i);
            }
        });

        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                int pos= (int) view.getTag();
                HashMap<String, String> items =data.get(pos);
                String username="";
                String  jobIds =  items.get("jobId");;
                String channel_id=items.get("channel");

                if(items.get("profile").isEmpty())
                    username=items.get("user_name");
                else
                    username= items.get("profile");

                String  userId=items.get("userId");

                jobId = items.get("jobId");

                //getmsgcount(userId);
                Utility.updateNotificationCount(activity,dialog,Utility.getApiParams(userId,jobId,"notificationCountMessage"));

                Intent i = new Intent(activity,ChatNeed.class);
                i.putExtra("jobId",jobId);
                i.putExtra("channel",channel_id);
                i.putExtra("username",username);
                i.putExtra("message_type","active_job");
                i.putExtra("user_type","employee");
                i.putExtra("userId", items.get("userId"));
                i.putExtra("receiverid",items.get("employer"));
                view.getContext().startActivity(i);

            }
        });

        if(get_image.equals(""))
        {
            image1.setVisibility(View.VISIBLE);
        }
        else {
            if(get_image!= null && get_image.contains("http://graph.facebook.com/"))
            {
                get_image = get_image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
            }
            Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

        }

        return vi;
    }


    public void requestpayment(final String jobid,final String employee_id,final String employer_id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_REQUESTPAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status=object.getString("status");
                            String channel=object.getString("channel_id");
                            String job_id=object.getString("job_id");
                            child_id = channel + job_id;
                            if(status.equals("success")){
                                final Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.custom_dialog);
                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("Request Payment Sent Successfully");
                                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("senderId", sender_id);
                                        map.put("senderName", get_user);
                                        map.put("text", "FROM HANDZ: Just a reminder that payment has not been completed on this job! Have a great day!");
                                        reference1.child(child_id).child("messages").push().setValue(map);
                                    }
                                });

                                dialog.show();
                                Window window = dialog.getWindow();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            }
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
                map.put(APP_KEY, value);
                map.put(JOB_ID, jobid);
                map.put(EMPLOYER_ID, employer_id);
                map.put(EMPLOYEE_ID, employee_id);
                map.put(USER_TYPE, "employee");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println(" Map "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    public void getmsgcount(final String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resposne "+response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(activity);
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
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");

                                // custom dialog
                                final Dialog dialog = new Dialog(activity);
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
                params.put(JOB_ID, jobId);
                params.put(TYPE,"notificationCountMessage");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
