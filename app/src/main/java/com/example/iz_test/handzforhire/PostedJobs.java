package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostedJobs extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String URL = Constant.SERVER_URL+"job_lists";
    private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    ImageView image,profile,logo;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    String address,city,state,zipcode,id,jobId,job_id,name,date,amount,applicants,profile_image,profilename,dlist;
    TextView profile_name;
    ListView list;
    LinearLayout lin_archievedjob;

    String type = "posted";
    int timeout = 60000;
    RelativeLayout rating_lay;
    Button active_btn,history_btn;
    Dialog dialog;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_posted_jobs);

        dialog = new Dialog(PostedJobs.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        logo = (ImageView)findViewById(R.id.logo);
        list = (ListView)findViewById(R.id.listview);
        //rating_lay = (RelativeLayout) findViewById(R.id.rating);
        active_btn = (Button) findViewById(R.id.btn1);
        history_btn = (Button) findViewById(R.id.btn2);
        lin_archievedjob=(LinearLayout)findViewById(R.id.lin_archievedjob);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");

        detector = new SimpleGestureFilter(this,this);

       // getProfileimage();
        listPostedJobs();
       // getcount();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this, ProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });


        active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this,ActiveJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this,JobHistory.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        lin_archievedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostedJobs.this,ArchievedJob.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });
    }

    public void listPostedJobs() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(PostedJobs.this);
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
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");
                                if (status.equals("No Jobs Found")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(PostedJobs.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("No Jobs Found");
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
                                } else {

                                    final Dialog dialog = new Dialog(PostedJobs.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("Login Failed.Please try again");
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
                params.put(TYPE,type);
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };

        System.out.println("values::"+value+".."+id+".."+type);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

   /* public void getcount() {
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
                            final Dialog dialog = new Dialog(PostedJobs.this);
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
                        }else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Server responded with a error response",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");


                                    final Dialog dialog = new Dialog(PostedJobs.this);
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
                params.put(TYPE,"notificationCountPosted");
                return params;
            }
        };

        System.out.println("values::"+value+".."+id+".."+type);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }*/
    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String jobList = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            jobList = jResult.getString("job_lists");
            System.out.println("jjjjjjjjjjjjjjjob:::list:::"+jobList);
            if(status.equals("success"))
            {
                job_list.clear();
                JSONArray array = new JSONArray(jobList);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    name = object.getString("job_name");
                    date = object.getString("job_date");
                    type = object.getString("job_payment_type");
                    amount = object.getString("job_estimated_payment");
                    applicants = object.getString("no_of_applicants_applied");
                    job_id = object.getString("job_id");
                    dlist=object.getString("delist");
                    String start_time=object.getString("start_time");

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", type);
                    map.put("amount", amount);
                    map.put("no_of_applicants",applicants);
                    map.put("userId",id);
                    map.put("address",address);
                    map.put("city",city);
                    map.put("state",state);
                    map.put("zipcode",zipcode);
                    map.put("jobId",job_id);
                    map.put("d_list",dlist);
                    map.put("start_time",start_time);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                   /* ViewListAdapter adapter = new ViewListAdapter(this, job_list);
                    list.setAdapter(adapter);*/
                    ViewListAdapter arrayAdapter = new ViewListAdapter(this, job_list){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            if(position %2 == 1)
                            {
                                // Set a background color for ListView regular row/item
                                view.setBackgroundColor(Color.parseColor("#BF178487"));
                            }
                            else
                            {
                                // Set the background color for alternate row/item
                                view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                            }
                            return view;
                        }
                    };

                    // DataBind ListView with items from ArrayAdapter
                    list.setAdapter(arrayAdapter);

                }
            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProfileimage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
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
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, id);
                map.put(Constant.DEVICE, Constant.ANDROID);
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

            if(status.equals("success"))
            {
                profile_image = jResult.getString("profile_image");
                profilename = jResult.getString("profile_name");
                System.out.println("ggggggggget:profilename:" + profilename);
                profile_name.setText(profilename);
                System.out.println("ggggggggget:profile_image:" + profile_image);
            /*    profile.setVisibility(View.GONE);
                Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    public class ViewListAdapter extends BaseAdapter {

        private Activity activity;
        String dlist,job_id;
        String value = "HandzForHire@~";
        private ArrayList<HashMap<String, String>> data;
        private  LayoutInflater inflater = null;
        public ViewListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                vi = inflater.inflate(R.layout.view_listview, null);

            TextView job_name = (TextView) vi.findViewById(R.id.text1);
            TextView when = (TextView) vi.findViewById(R.id.text2);
            TextView pay = (TextView) vi.findViewById(R.id.text6);
            TextView date = (TextView) vi.findViewById(R.id.text3);
            TextView amount = (TextView) vi.findViewById(R.id.text7);
            TextView text = (TextView) vi.findViewById(R.id.text_value);
            TextView type = (TextView) vi.findViewById(R.id.text8);
            final TextView jobId = (TextView) vi.findViewById(R.id.job_id);
            final TextView applicants = (TextView) vi.findViewById(R.id.no_applicants);
            LinearLayout rel_viewapplicant=(LinearLayout)vi.findViewById(R.id.rel_viewapplicant);
            TextView expected = (TextView) vi.findViewById(R.id.expected);
            TextView t2 = (TextView) vi.findViewById(R.id.t2);
            TextView time = (TextView) vi.findViewById(R.id.time);

            ImageView checked=(ImageView)vi.findViewById(R.id.img);
            ImageView unchecked=(ImageView)vi.findViewById(R.id.img1);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            final String get_date = items.get("date");
            String get_amount = items.get("amount");
            String get_type = items.get("type");
            job_id = items.get("jobId");
            final String get_applicants = items.get("no_of_applicants");
            final String user_id = items.get("userId");
            final String address = items.get("address");
            final String city = items.get("city");
            final String state = items.get("state");
            final String zipcode = items.get("zipcode");
            String start_time = items.get("start_time");
            dlist= items.get("d_list");
            System.out.println("iiiiiidlist::"+dlist);

            checked.setTag(position);
            unchecked.setTag(position);
            rel_viewapplicant.setTag(position);

            if (dlist.equals("no"))
            {
                unchecked.setVisibility(View.VISIBLE);
                checked.setVisibility(View.INVISIBLE);
                text.setText("Want to relist? Check the box to make this job visible to potential employees.");
            }else
            {
                checked.setVisibility(View.VISIBLE);
                unchecked.setVisibility(View.INVISIBLE);
                text.setText("Done hiring? Uncheck the box to remove this job listing from public view.");
            }

            unchecked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos= (int) v.getTag();
                    dlist="yes";
                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get(pos);
                    job_id=items.get("jobId");
                    check();

                }
            });

            checked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos= (int) v.getTag();
                    dlist="no";
                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get(pos);
                    job_id=items.get("jobId");
                    check();

                }
            });
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


            if(get_applicants.equals("0"))
            {
                applicants.setVisibility(View.INVISIBLE);
            }
            else
            {
                applicants.setText(get_applicants);
            }

            String mStringDate = start_time;
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
            time.setTypeface(font);
            t2.setTypeface(font);

            job_name.setText(get_name);
            job_name.setTypeface(font);
            when.setTypeface(font);
            pay.setTypeface(font);
            date.setTypeface(font);
            amount.setText(get_amount);
            amount.setTypeface(font);
            type.setText(get_type);
            type.setTypeface(font);
            expected.setTypeface(font);
            jobId.setText(job_id);

            rel_viewapplicant.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    HashMap<String, String> items = data.get(pos);
                    if (items.get("no_of_applicants").equals("0")) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("No Job Applied");
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
                        return;
                    } else {
                        Intent i = new Intent(activity, ViewApplicant.class);
                        i.putExtra("jobId", items.get("jobId"));
                        i.putExtra("userId",items.get("userId"));
                        i.putExtra("address",  items.get("address"));
                        i.putExtra("city", items.get("city"));
                        i.putExtra("zipcode",  items.get("zipcode"));
                        i.putExtra("state", items.get("state"));
                        i.putExtra("jobname", items.get("name"));
                        v.getContext().startActivity(i);
                    }
                }
            });

            return vi;
        }

        private void check()
        {
            final String url = Constant.SERVER_URL+"remove_job?X-APP-KEY="+value+"&delist="+dlist+"&job_id="+job_id+"&device=android";

            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {

                            Log.d("Response", response.toString());
                            System.out.println("resssssssssssssssss:dlist::service:::" + response);
                            onResponserecieved1(response, 1);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                        }
                    }
            );


            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(getRequest);
        }

        public void onResponserecieved1(JSONObject jsonobject, int i) {
            String status = null;

            try {

                JSONObject jResult = new JSONObject(String.valueOf(jsonobject));

                status = jResult.getString("status");

                if(status.equals("success"))
                {
                  type="posted";
                  listPostedJobs();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
                finish();

                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

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
