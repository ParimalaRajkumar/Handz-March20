package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Map;

public class ViewSearchJob extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    ListView list;
    private static final String SEARCH_URL = Constant.SERVER_URL+"job_search";
    private static final String URL = Constant.SERVER_URL+"job_lists";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String KEY_SEARCHTYPE = "search_type";
    public static String RADIUS = "location";
    public static String CATEGORY = "category";
    public static String ZIPCODE = "zipcode";
    public static String USER_ID = "user_id";
    public static String LAT = "lat";
    public static String LON = "lon";
    public static String EMPLOYEE_ID = "employee_id";
    public static String MILES = "miles";
    public static String TYPE = "type";
    String user_id,address,city,state,zipcode,radius,category,name,date,pay_type,amount,jobId,image,zip,alljobs;
    ImageView logo;
   // ProgressDialog progress_dialog;
    String cat = "category";
    String zip_type = "zipcode";
    String display_all = "display_all";
    String cat_zip = "category,zipcode";
    String cat_loc = "category,location";
    String zip_loc = "zipcode,location";
    String type = "";
     int timeout = 60000;
    LinearLayout new_search,map_view;
    Dialog dialog;
    SessionManager session;
    HashMap<String, String> location;
    Map<String, String> params = new HashMap<String, String>();
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_job);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        session = new SessionManager(ViewSearchJob.this);
        location = session.getlocation();

        dialog = new Dialog(ViewSearchJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        list = (ListView) findViewById(R.id.listview);
        logo = (ImageView) findViewById(R.id.logo);
        new_search = (LinearLayout) findViewById(R.id.new_search);
        map_view = (LinearLayout) findViewById(R.id.map_view);

        Intent i = getIntent();
        String setype = i.getStringExtra("type");
        if(setype.equals("search")) {
            user_id = i.getStringExtra("userId");
            address = i.getStringExtra("address");
            city = i.getStringExtra("city");
            state = i.getStringExtra("state");
            zipcode = i.getStringExtra("zipcode");
            radius = i.getStringExtra("radius");
            category = i.getStringExtra("categoryId");
            zip = i.getStringExtra("zip");
            alljobs = i.getStringExtra("alljobs");


            if(alljobs.equals("all_jobs")){
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"location");
                params.put(CATEGORY,category);
                params.put(ZIPCODE,zip);
                params.put(LAT,location.get(SessionManager.LATITUDE));
                params.put(LON,location.get(SessionManager.LONGITUDE));
                params.put(MILES,"5");
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();

            }else if (!category.equals("") && !zip.equals("")){
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"category,zipcode");
                params.put(CATEGORY,category);
                params.put(ZIPCODE,zip);
                params.put(MILES,"5");
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();
            } else if (!zip.equals("") && !radius.equals("")) {
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"zipcode,location");
                params.put(ZIPCODE,zip);
                params.put(LAT,location.get(SessionManager.LATITUDE));
                params.put(LON,location.get(SessionManager.LONGITUDE));
                params.put(MILES,radius);
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();
            }else if (!category.equals("") && !radius.equals("")) {
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"category,location");
                params.put(CATEGORY,category);
                params.put(LAT,location.get(SessionManager.LATITUDE));
                params.put(LON,location.get(SessionManager.LONGITUDE));
                params.put(MILES,radius);
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();
            } else if (!category.equals("")&&zip.equals("")&&radius.equals("")) {
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"category");
                params.put(CATEGORY,category);
                params.put(MILES,"5");
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();
            }else if (!zip.equals("")&&category.equals("")&&radius.equals("")) {
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"zipcode");
                params.put(ZIPCODE,zip);
                params.put(MILES,"5");
                params.put(EMPLOYEE_ID,user_id);
                searchJobList();
            }else if (!radius.equals("")) {
                params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,"location");
                params.put(LAT,location.get(SessionManager.LATITUDE));
                params.put(LON,location.get(SessionManager.LONGITUDE));
                params.put(MILES,radius);
                params.put(EMPLOYEE_ID,user_id);
            }

            System.out.println("params "+params);

        }else{
            showundisclosedjob(FindJobMap.undisclosedjobsarray);
        }

        detector = new SimpleGestureFilter(this,this);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewSearchJob.this,SearchJob.class);
                i.putExtra("userId",Profilevalues.user_id);
                i.putExtra("address",Profilevalues.address);
                i.putExtra("city",Profilevalues.city);
                i.putExtra("state",Profilevalues.state);
                i.putExtra("zipcode",Profilevalues.zipcode);
                startActivity(i);
            }
        });

        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewSearchJob.this,MapActivity.class);
                i.putExtra("userId",Profilevalues.user_id);
                i.putExtra("address",Profilevalues.address);
                i.putExtra("city",Profilevalues.city);
                i.putExtra("state",Profilevalues.state);
                i.putExtra("zipcode",Profilevalues.zipcode);
                startActivity(i);
            }
        });


    }

    private void joblist() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:search_job:joblist::" +response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ViewSearchJob.this);
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
                params.put(USER_ID,user_id);
                params.put(TYPE,type);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        System.out.println("vvvvvvv:job list:::::"+value+".."+user_id+".."+type);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void searchJobList() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:search_job:::" +response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("volley error::: " + jsonObject);
                            String status = jsonObject.getString("msg");
                            // custom dialog
                            final Dialog dialog = new Dialog(ViewSearchJob.this);
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
                            return;

                        } catch (JSONException e) {
                            //Handle a malformed json response
                            System.out.println("volley error ::" + e.getMessage());
                        } catch (UnsupportedEncodingException errors) {
                            System.out.println("volley error ::" + errors.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void onResponserecieved(String response, int i) {
        String status = null;
        System.out.println("Resposne "+response);
        try
        {
            JSONObject result = new JSONObject(response);
            status = result.getString("status");
            if(status.equals("success"))
            {
                String job = result.getString("job_lists");

                JSONArray array = new JSONArray(job);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    String category = object.getString("job_category");
                    System.out.println("ressss::category:" + category);
                        name = object.getString("job_name");
                        date = object.getString("job_date");
                        pay_type = object.getString("job_payment_type");
                        amount = object.getString("job_payment_amount");
                        jobId = object.getString("id");
                        image = object.getString("profile_image");
                     String  average_rating=object.getString("average_rating");

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", pay_type);
                    map.put("amount", amount);
                    map.put("jobId",jobId);
                    map.put("image",image);
                    map.put("average_rating",average_rating);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    CustomList arrayAdapter = new CustomList(this, job_list){
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

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            HashMap<String, String> map=job_list.get(position);
                            Intent i = new Intent(ViewSearchJob.this,JobDescription.class);
                            i.putExtra("userId",user_id);
                            i.putExtra("average_rating",map.get("average_rating"));
                            i.putExtra("jobId",map.get("jobId"));
                            startActivity(i);
                        }
                    });
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;

        try
        {
            JSONObject result = new JSONObject(jsonobject);
            status = result.getString("status");
            if(status.equals("success"))
            {
                String job = result.getString("job_lists");
                JSONArray array = new JSONArray(job);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    String category = object.getString("job_category");
                    System.out.println("ressss::category:" + category);
                    name = object.getString("job_name");
                    date = object.getString("job_date");
                    pay_type = object.getString("job_payment_type");
                    amount = object.getString("job_payment_amount");
                    jobId = object.getString("id");
                    image = object.getString("profile_image");
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", pay_type);
                    map.put("amount", amount);
                    map.put("jobId",jobId);
                    map.put("image",image);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    CustomList arrayAdapter = new CustomList(this, job_list){
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

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            HashMap<String, String> map=job_list.get(position);
                            Intent i = new Intent(ViewSearchJob.this,JobDescription.class);
                            i.putExtra("userId",user_id);
                            i.putExtra("average_rating",map.get("average_rating"));
                            i.putExtra("jobId",map.get("jobId"));
                            startActivity(i);
                        }
                    });
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void showundisclosedjob(JSONArray array){
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                String category = object.getString("job_category");

                name = object.getString("job_name");
                date = object.getString("job_date");
                pay_type = object.getString("job_payment_type");
                amount = object.getString("job_payment_amount");
                jobId = object.getString("id");
                image = object.getString("profile_image");
                String  average_rating=object.getString("average_rating");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("date", date);
                map.put("type", pay_type);
                map.put("amount", amount);
                map.put("jobId", jobId);
                map.put("image", image);
                map.put("average_rating",average_rating);
                job_list.add(map);
                System.out.println("job_list:::" + job_list);
                CustomList arrayAdapter = new CustomList(this, job_list) {
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
                        HashMap<String, String> map=job_list.get(position);
                        Intent i = new Intent(ViewSearchJob.this, JobDescription.class);
                        i.putExtra("userId", Profilevalues.user_id);
                        i.putExtra("average_rating",map.get("average_rating"));
                        i.putExtra("jobId", map.get("jobId"));
                        startActivity(i);
                    }
                });

            }

        }catch (Exception e){
            System.out.println("exception "+e.getMessage());
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
