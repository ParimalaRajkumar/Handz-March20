package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PendingJobs extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    ListView list;
    private static final String SEARCH_URL = Constant.SERVER_URL+"job_lists";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String KEY_USER = "user_id";
    public static String KEY_TYPE = "type";
    Calendar calendar;
    String user_id,address,city,state,zipcode,cat_type,cat_id,job_cat_name,name,date,amount,jobId;
    String emplrid,empleid;
    String jobname,jobdate,pay,esti,jobstatus;
    ImageView logo;
    ProgressDialog progress_dialog;
    String type = "applied";
    Button active_jobs,job_history;
    Dialog dialog;
    int visible_pos,visible_lay;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_jobs);


        dialog = new Dialog(PendingJobs.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        detector = new SimpleGestureFilter(this,this);

        list = (ListView) findViewById(R.id.listview);
        logo = (ImageView) findViewById(R.id.logo);
        active_jobs = (Button) findViewById(R.id.btn1);
        job_history = (Button) findViewById(R.id.btn2);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        cat_type = i.getStringExtra("type");
        cat_id = i.getStringExtra("categoryId");
        job_cat_name = i.getStringExtra("category");

        searchJobList();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        active_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PendingJobs.this,LendActiveJobs.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        job_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PendingJobs.this,LendJobHistory.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

    }

    public void searchJobList() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:pendingjobs:::" +response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(PendingJobs.this);
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
                             //   if (status.equals("No Jobs Found")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(PendingJobs.this);
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

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USER,user_id);
                params.put(KEY_TYPE,type);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onResponserecieved(String response, int i)
    {
        String status = null;

        try
        {
            JSONObject result = new JSONObject(response);
            status = result.getString("status");
            if(status.equals("success"))
            {
                job_list.clear();
                String job = result.getString("job_lists");
                System.out.println("jjjjjjjjjjjjjjjob:"+job);
                JSONArray array = new JSONArray(job);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    String category = object.getString("job_category");
                    System.out.println("ressss::category:" + category);
                    jobname = object.getString("job_name");
                    jobdate = object.getString("job_date");
                    esti = object.getString("job_payment_type");
                    pay = object.getString("job_estimated_payment");
                    jobId = object.getString("id");
                    jobstatus=object.getString("job_status");
                    emplrid=object.getString("employer_id");

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", jobname);
                    map.put("date", jobdate);
                    map.put("type", esti);
                    map.put("amount", pay);
                    map.put("jobId",jobId);
                    map.put("status",jobstatus);
                    map.put("emrid",emplrid);
                    map.put("employeeid",user_id);
                    job_list.add(map);
                }
                System.out.println("job_list:::" + job_list);
                PendingAdapter arrayAdapter = new PendingAdapter(this, job_list)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
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
                               /* view.setSelected(true);
                                String job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                                System.out.println("ssssssssssselected:job_id:" + job_id);
                                Intent i = new Intent(PendingJobs.this,JobDescription.class);
                                i.putExtra("userId",user_id);
                                i.putExtra("jobId",job_id);
                                startActivity(i);*/
                    }
                });
            }
        }catch (JSONException e){
            e.printStackTrace();
        }  {
    }
        {

        }
    }


    public class PendingAdapter extends BaseAdapter {
        private  final String emp_reject = Constant.SERVER_URL + "employee_reject";
        private  final String job_list = Constant.SERVER_URL + "job_lists";

        public  String XAPP_KEY = "X-APP-KEY";
        String value = "HandzForHire@~";
        String type = "employee";
        String user_id;
        String get_jobid, get_emplrid, get_employeeid, get_status;
        public  String KEY_JOBID = "job_id";
        public  String KEY_EMPLOYERID = "employer_id";
        public  String KEY_EMPLOYEEID = "employee_id";
        public  String KEY_USERTYPE = "user_type";
        public  String KEY_USER = "user_id";
        ProgressDialog progress_dialog;
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private  LayoutInflater inflater = null;
        private LayoutInflater layoutInflater;


        public PendingAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
                vi = inflater.inflate(R.layout.pending_job_list, null);

            TextView job_name = (TextView) vi.findViewById(R.id.job);
            TextView job_date = (TextView) vi.findViewById(R.id.when);
            TextView pay = (TextView) vi.findViewById(R.id.pay);
            TextView job_type = (TextView) vi.findViewById(R.id.expected);
            TextView jobId = (TextView) vi.findViewById(R.id.job_id);
            final ImageView gray = (ImageView) vi.findViewById(R.id.gray);
            final ImageView red = (ImageView) vi.findViewById(R.id.red);
            final ImageView green = (ImageView) vi.findViewById(R.id.green);
            TextView gry=(TextView)vi.findViewById(R.id.gra);
            TextView re=(TextView)vi.findViewById(R.id.redd);
            TextView gre=(TextView)vi.findViewById(R.id.gr);
            TextView t1=(TextView)vi.findViewById(R.id.t1);
            TextView t2=(TextView)vi.findViewById(R.id.t2);
            LinearLayout rel_viewapplicant=(LinearLayout)vi.findViewById(R.id.rel_viewapplicant);
            RelativeLayout gray_layout=(RelativeLayout)vi.findViewById(R.id.gray_layout);
            RelativeLayout red_layout=(RelativeLayout)vi.findViewById(R.id.red_layout);
            RelativeLayout green_layout=(RelativeLayout)vi.findViewById(R.id.green_layout);


            TextView t3=(TextView)vi.findViewById(R.id.t3);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            job_date.setTypeface(font);
            pay.setTypeface(font);
            job_type.setTypeface(font);
            job_name.setTypeface(font);
            t1.setTypeface(font);
            t2.setTypeface(font);
            t3.setTypeface(font);

            final LinearLayout lin_hold=(LinearLayout)vi.findViewById(R.id.lin_hold);
            final LinearLayout lin_hire=(LinearLayout)vi.findViewById(R.id.lin_hire);
            final LinearLayout lin_refuse=(LinearLayout)vi.findViewById(R.id.lin_refuse);

            final LinearLayout layout_refuse=(LinearLayout)vi.findViewById(R.id.layout_refuse);
            final LinearLayout layout_hold=(LinearLayout)vi.findViewById(R.id.layout_hold);
            final LinearLayout layout_hire=(LinearLayout)vi.findViewById(R.id.layout_hire);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_jobname = items.get("name");
            final String get_jobdate = items.get("date");
            final String get_pay = items.get("amount");
            final String get_esti = items.get("type");
            get_status = items.get("status");
            user_id = items.get("employeeid");

            if (get_status.equals("Hired")) {
                green.setVisibility(View.VISIBLE);
                gray.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);
                gre.setVisibility(View.VISIBLE);
                re.setVisibility(View.INVISIBLE);
                gry.setVisibility(View.INVISIBLE);

            } else if (get_status.equals("Hold")) {
                gray.setVisibility(View.VISIBLE);
                green.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);
                gre.setVisibility(View.INVISIBLE);
                re.setVisibility(View.INVISIBLE);
                gry.setVisibility(View.VISIBLE);
            } else {
                red.setVisibility(View.VISIBLE);
                green.setVisibility(View.INVISIBLE);
                gray.setVisibility(View.INVISIBLE);
                gre.setVisibility(View.INVISIBLE);
                re.setVisibility(View.VISIBLE);
                gry.setVisibility(View.INVISIBLE);
            }

            System.out.println("pos "+visible_pos);

            if(position==visible_pos){
                if(visible_lay==1){
                    lin_hold.setVisibility(View.VISIBLE);
                    lin_hire.setVisibility(View.GONE);
                    lin_refuse.setVisibility(View.GONE);
                }else if(visible_lay==2){
                    lin_hold.setVisibility(View.GONE);
                    lin_hire.setVisibility(View.GONE);
                    lin_refuse.setVisibility(View.VISIBLE);
                }else if(visible_lay==3){
                    lin_hold.setVisibility(View.GONE);
                    lin_hire.setVisibility(View.VISIBLE);
                    lin_refuse.setVisibility(View.GONE);
                }
            }else{
                lin_hold.setVisibility(View.GONE);
                lin_hire.setVisibility(View.GONE);
                lin_refuse.setVisibility(View.GONE);
            }

            layout_refuse.setTag(position);
            layout_hold.setTag(position);
            layout_hire.setTag(position);
            rel_viewapplicant.setTag(position);

            layout_hold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lin_hold.setVisibility(View.GONE);
                    visible_pos=-1;
                    visible_lay=0;
                }
            });

            layout_hire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lin_hire.setVisibility(View.GONE);
                    visible_pos=-1;
                    visible_lay=0;
                }
            });

            layout_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    visible_pos=-1;
                    visible_lay=0;
                    lin_refuse.setVisibility(View.GONE);
                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get((Integer) view.getTag());
                    get_jobid = items.get("jobId");
                    get_emplrid = items.get("emrid");
                    get_employeeid = items.get("employeeid");
                    type="employee";
                    refusee();
                }
            });

                    gray_layout.setTag(position);
                    red_layout.setTag(position);
                    green_layout.setTag(position);

            gray_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    visible_pos=pos;
                    visible_lay=1;
                    notifyDataSetChanged();
                    return;
                }
            });
            red_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    visible_pos=pos;
                    visible_lay=2;;
                    notifyDataSetChanged();
                    return;
                }

            });
            green_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    visible_lay=3;
                    visible_pos=pos;
                    notifyDataSetChanged();
                    return;

                }
            });

            get_jobid = items.get("jobId");
            get_emplrid = items.get("emrid");
            get_employeeid = items.get("employeeid");


            DateFormat dateInstance = SimpleDateFormat.getDateInstance();
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            try {
                java.util.Date dates = srcDf.parse(get_jobdate);
                System.out.println("date " + get_jobdate);
                System.out.println("converted " + destDf.format(dates));
                job_name.setText(get_jobname);
                job_date.setText(destDf.format(dates));
                pay.setText(get_pay);
                job_type.setText(get_esti);
                //jobId.setText(get_id);
                //job_name.setText("PAY"+get_jobname);

            } catch (Exception e) {
                System.out.println("error " + e.getMessage());
            }
            System.out.println("today date " + dateInstance.format(Calendar.getInstance().getTime()));
            return vi;
        }

        private void refusee() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, emp_reject,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("resss::emp_reject" + response);
                            String status = null;

                            try {
                                JSONObject result = new JSONObject(response);
                                status = result.getString("status");
                                if (status.equals("success"))
                                {
                                    searchJobList();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(PendingJobs.this);
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
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                }
                            } catch (JSONException e) {

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(XAPP_KEY, value);
                    params.put(KEY_JOBID, get_jobid);
                    params.put(KEY_EMPLOYERID, get_emplrid);
                    params.put(KEY_EMPLOYEEID, get_employeeid);
                    params.put(KEY_USERTYPE, type);
                    params.put(Constant.DEVICE, Constant.ANDROID);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);

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
