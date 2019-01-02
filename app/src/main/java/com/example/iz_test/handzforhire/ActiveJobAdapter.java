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
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveJobAdapter extends BaseAdapter {
        private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
        public static String KEY_USERID = "user_id";
        public static String XAPP_KEY = "X-APP-KEY";
        public static String TYPE = "type";
        String value = "HandzForHire@~";
        public static String JOB_ID = "job_id";
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater = null;
        String jobId;
        SessionManager sessoin;
        Dialog dialog;

        public ActiveJobAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sessoin=new SessionManager(a);
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
                vi = inflater.inflate(R.layout.activejobs_list, null);

            TextView job_name = (TextView) vi.findViewById(R.id.text1);
            TextView text = (TextView) vi.findViewById(R.id.text2);
            ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
            Button make_payment = (Button) vi.findViewById(R.id.payment);
            TextView profile_name = (TextView) vi.findViewById(R.id.text3);
            Button job_details = (Button) vi.findViewById(R.id.job_detail_btn);
            Button chat = (Button) vi.findViewById(R.id.message_btn);
            final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
            final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
            final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
            final TextView image_text = (TextView) vi.findViewById(R.id.image1);
            final TextView message_count = (TextView) vi.findViewById(R.id.message_count);
            final TextView payment_count = (TextView) vi.findViewById(R.id.pay_count);
            TextView symbol = (TextView) vi.findViewById(R.id.symbol);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            text.setTypeface(font);
            profile_name.setTypeface(font);
            symbol.setTypeface(font);

            String fontPath1 = "fonts/calibri.ttf";
            Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            String get_image = items.get("image");
            final String get_profile = items.get("profile");
            final String get_user = items.get("user");
            final String user_id = items.get("userId");
            final String get_jobid = items.get("jobId");
            final String get_employer = items.get("employer");
            final String get_employee = items.get("employee");
            final String channel_id=items.get("channel");
            final String message_notification_count=items.get("message_count");
            final String payment_notification_count=items.get("payment_count");
            final String job_payout=items.get("job_payout");
            final String paypal_fee=items.get("paypalfee");
            final String estimated_payment=items.get("job_estimated_payment");
            final String fee_details=items.get("fee_details");
            final String job_payment_amount=items.get("job_payment_amount");
            System.out.println("success:count:::" + message_notification_count+",,,"+payment_notification_count);

            job_name.setText(get_name);
            job_name.setTypeface(font);
            job_id.setText(get_jobid);
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

            if(get_profile.equals(""))
            {
                profile_name.setText(get_user);
            }
            else
            {
                profile_name.setText(get_profile);
            }

            chat.setTag(position);
            make_payment.setTag(position);
            job_details.setTag(position);
            make_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(activity,MakePayment.class);
                    int pos= (int) v.getTag();

                    HashMap<String, String> items =data.get(pos);

                    JSONObject object = new JSONObject(items);
                    System.out.println("activejobadapter:::object::: "+object);
                    sessoin.savepaymentdetails(object.toString());
                    String  userId=items.get("userId");
                    String  jobId =  items.get("jobId");
                    //getpaymentcount(userId);
                    Utility.updateNotificationCount(activity,dialog,Utility.getApiParams(userId,jobId,"notificationCountMakePayment"));

                    String username="";
                   // String  jobId =  items.get("jobId");;


                    String name=items.get("name");
                    String image=items.get("image");
                    String user=items.get("user");
                    String profile=items.get("profile");
                    String employer=items.get("employer");
                    String employee=items.get("employee");

                    i.putExtra("job_id",jobId);
                    i.putExtra("userId",userId);
                    i.putExtra("job_name",name);
                    i.putExtra("image",image);
                    i.putExtra("profilename",profile);
                    i.putExtra("merchant_id",items.get("merchant_id"));
                    i.putExtra("username",user);
                    i.putExtra("employer",employer);
                    i.putExtra("employee",employee);
                    i.putExtra("job_payout",items.get("job_payout"));
                    i.putExtra("paypalfee",items.get("paypalfee"));
                    i.putExtra("job_estimated_payment",items.get("job_estimated_payment"));
                    i.putExtra("job_payment_amount",items.get("job_payment_amount"));
                    i.putExtra("fee_details",items.get("fee_details"));

                    v.getContext().startActivity(i);
                }
            });

            chat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int pos= (int) view.getTag();
                    HashMap<String, String> items =data.get(pos);
                    String  userId=items.get("userId");
                    jobId = items.get("jobId");
                   // getmsgcount(userId);
                    Utility.updateNotificationCount(activity,dialog,Utility.getApiParams(userId,jobId,"notificationCountMessage"));
                    String username="";
                    String  jobId =  items.get("jobId");;
                    String channel_id=items.get("channel");

                    if(items.get("profile").isEmpty())
                        username=items.get("user");
                    else
                        username= items.get("profile");;

                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",jobId);
                    i.putExtra("channel",channel_id);
                    i.putExtra("username",username);
                    i.putExtra("userId",userId);
                    i.putExtra("message_type","active_job");
                    i.putExtra("user_type","employer");
                    i.putExtra("receiverid",items.get("employee"));
                    view.getContext().startActivity(i);
                }
            });

            job_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    HashMap<String, String> items =data.get(pos);
                    Intent i = new Intent(activity,JobDetails.class);
                    i.putExtra("jobId",items.get("jobId"));
                    i.putExtra("userId",items.get("userId"));
                    v.getContext().startActivity(i);
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
     return  vi;
        }


    public void getmsgcount(final String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("success::::resposne::: "+response);
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

    public void getpaymentcount(final String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("success:::resposne:::"+response);
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
                                dialog.getWindow().setDimAmount(0);
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
                params.put(TYPE,"notificationCountMakePayment");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("params "+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}