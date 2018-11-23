package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LendJobHistory extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    private static final String URL = Constant.SERVER_URL+"job_history_listing";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    ImageView logo;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    String address,city,state,zipcode,user_id,job_id;
    TextView profile_name;
    EditText ed_search;
    Button pending_job,active_job;
    ListView list;
    ProgressDialog progress_dialog;
    String usertype = "employee";
    int timeout = 60000;
    Dialog dialog;
    String rating_value,rating_id,category1,category2,category3,category4,category5,comments;
    String lend_status = "lend";
    LendHistoryAdapter arrayAdapter;
    private SimpleGestureFilter detector;

    @Override
    protected void onStart() {
        super.onStart();
        activeJobs();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_job_history);

        dialog = new Dialog(LendJobHistory.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        pending_job = (Button) findViewById(R.id.btn1);
        active_job = (Button)findViewById(R.id.btn2);
        logo = (ImageView)findViewById(R.id.logo);
        list = (ListView) findViewById(R.id.listview);
        ed_search=(EditText)findViewById(R.id.ed_search);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");

        detector = new SimpleGestureFilter(this,this);

        //activeJobs();

        ed_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_search.setHint("");
            }
        });


        ed_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
               /* String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                arrayAdapter.filter(text);*/
                String charText = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                arrayAdapter.getFilter().filter(charText);
                //JobHistory.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendJobHistory.this,LendProfilePage.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        pending_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendJobHistory.this,PendingJobs.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        active_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendJobHistory.this,LendActiveJobs.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

    }

    public void activeJobs() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:job_history::" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendJobHistory.this);
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
                               // if (status.equals("No Active Jobs Found")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LendJobHistory.this);
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
                             //   }

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
                params.put(KEY_USERID, user_id);
                params.put(TYPE, usertype);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
       // stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String jobList = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            jobList = jResult.getString("job_lists");
            System.out.println("jjjjjjjjjjjjjjjob:::list:::" + jobList);
            if(status.equals("success"))
            {
                JSONArray array = new JSONArray(jobList);
                job_list.clear();
                for(int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    final String job_name = object.getString("job_name");
                    final String image = object.getString("profile_image");
                    final String profilename = object.getString("profile_name");
                    final String username = object.getString("username");
                    final String payment = object.getString("job_payment_amount");
                    final String jobId = object.getString("job_id");
                    final String employerId = object.getString("employer_id");
                    final String employeeId = object.getString("employee_id");
                    final String channelid=object.getString("channel");
                    String rating=object.getString("rating");
                    final String tran_date=object.getString("transaction_date");
                    final String job_category=object.getString("job_category");
                    final String description=object.getString("description");
                    final String employee_id = object.getString("employee_id");
                    final String msg_notification = object.getString("employee_notificationCountMsgJobhistory");
                    final String star_notification = object.getString("employee_notificationCountStarRating");
                    final String transaction_date = object.getString("transaction_date");
                    String job_status=object.getString("job_status");
                    System.out.println("ssssssssss:job_status:::"+job_status);

                    if(rating.equals("null"))
                    {
                        rating_value = "";
                        rating_id = "";
                        category1 = "";
                        category2 = "";
                        category3 = "";
                        category4 = "";
                        category5 = "";
                    }
                    else
                    {
                        JSONObject Result = new JSONObject(rating);
                        rating_value = Result.getString("rating");
                        rating_id = Result.getString("id");
                        category1 = Result.getString("category1");
                        category2 = Result.getString("category2");
                        category3 = Result.getString("category3");
                        category4 = Result.getString("category4");
                        category5 = Result.getString("category5");
                        comments = Result.getString("comments");
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name",job_name);
                    map.put("image",image);
                    map.put("profile",profilename);
                    map.put("username",username);
                    map.put("payment",payment);
                    map.put("jobId",jobId);
                    map.put("employer",employerId);
                    map.put("employee",employeeId);
                    map.put("user_id",user_id);
                    map.put("channel",channelid);
                    map.put("ratingId",rating_id);
                    map.put("rating",rating_value);
                    map.put("category1",category1);
                    map.put("category2",category2);
                    map.put("category3",category3);
                    map.put("category4",category4);
                    map.put("category5",category5);
                    map.put("transaction_date",tran_date);
                    map.put("job_category",job_category);
                    map.put("description",description);
                    map.put("message_count",msg_notification);
                    map.put("star_count",star_notification);
                    map.put("transaction_date",transaction_date);
                    map.put("lend_status",lend_status);
                    map.put("job_status",job_status);
                    map.put("comments",comments);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    arrayAdapter = new LendHistoryAdapter(this, job_list) {
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
                            job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                            System.out.println("ssssssssssselected:item:" + job_id);
                        }
                    });
                }

            }
            else
            {

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
                Intent i = new Intent(getApplicationContext(), LendProfilePage.class);
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
