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
import android.view.ViewGroup;
import android.view.Window;
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
import com.android.volley.ServerError;
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

public class ManagePaymentOptions extends Activity implements SimpleGestureFilter.SimpleGestureListener {

    ImageView logo;
    String user_id;
    String address,city,state,zipcode;
    private static final String URL = Constant.SERVER_URL+"lists_employer_cards";
    public static String KEY_EMPLOYER_ID = "employer_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    ListView listView,listView1,listView2;
    ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> accountItems = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> paypalArray = new ArrayList<HashMap<String, String>>();

    Button add;
    TextView text,t1,t2;
    Dialog dialog;
    private SimpleGestureFilter detector;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_payment);

        dialog = new Dialog(ManagePaymentOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        add = (Button) findViewById(R.id.payment);
        logo = (ImageView) findViewById(R.id.logo);
        listView = (ListView) findViewById(R.id.list);
        listView1 = (ListView) findViewById(R.id.list1);
        listView2 = (ListView) findViewById(R.id.list2);
        text = (TextView) findViewById(R.id.text1);
        t1 = (TextView) findViewById(R.id.text2);
        t2 = (TextView) findViewById(R.id.text3);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(tf);
        add.setTypeface(tf);
        t1.setTypeface(tf);
        t2.setTypeface(tf);

        detector = new SimpleGestureFilter(this,this);
        session=new SessionManager(ManagePaymentOptions.this);

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView1.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView2.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");

        webservice();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               /* Intent i = new Intent(ManagePaymentOptions.this, EditUserProfile.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();*/
                Intent i = new Intent(ManagePaymentOptions.this, EditUserProfile.class);
                HashMap<String,String> map= new HashMap<String, String>();
                i.putExtra("isfrom", "edit");
                map.put("userId",user_id);
                map.put("address",address);
                map.put("city",city);
                map.put("state",state);
                map.put("zipcode",zipcode);
                JSONObject object = new JSONObject(map);
                session.saveregistrationdet(object.toString());
                finish();
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagePaymentOptions.this, PaymentMethod.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

    }

    private void webservice() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
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
                            final Dialog dialog = new Dialog(ManagePaymentOptions.this);
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
                                System.out.println("error" + jsonObject);

                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_EMPLOYER_ID, user_id);
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
        String card = null;
        String check = null;
        String paypal = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            card = jResult.getString("card_data");
            check = jResult.getString("checking_account_data");
            paypal = jResult.getString("paypal_info");
            System.out.println("resssssss:card_data:" + card);
            System.out.println("resssssss:check:" + check);
            System.out.println("resssssss:paypal:" + paypal);

            if(status.equals("success"))
            {
                JSONArray array = new JSONArray(card);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    final String card_id = object.getString("id");
                    String card_no = object.getString("card_number");
                    String type = object.getString("card_type");
                    String month = object.getString("exp_month");
                    String year = object.getString("exp_year");
                    System.out.println("ressss:card_id::"+card_id);
                    System.out.println("ressss:card_no::"+card_no);
                    System.out.println("ressss:type::"+type);
                    System.out.println("ressss::month:" + month);
                    System.out.println("ressss:year::" + year);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("userId", user_id);
                    map.put("id", card_id);
                    map.put("type", type);
                    map.put("card_number", card_no);
                    map.put("month",month);
                    map.put("year", year);
                    menuItems.add(map);
                    System.out.println("menuitems:::" + menuItems);
                    /*CustomAdapter adapter = new CustomAdapter(this, menuItems);
                    listView.setAdapter(adapter);*/
                    CustomAdapter arrayAdapter = new CustomAdapter(this, menuItems){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            TextView card = (TextView)view.findViewById(R.id.card_type);
                            TextView card_number = (TextView)view.findViewById(R.id.card_no);
                            TextView id = (TextView)view.findViewById(R.id.card_id);
                            TextView month = (TextView)view.findViewById(R.id.month);
                            TextView year = (TextView)view.findViewById(R.id.year);
                            Button edit_btn = (Button)view.findViewById(R.id.edit);
                            Button delete_btn = (Button)view.findViewById(R.id.delete);

                            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
                            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
                            edit_btn.setTypeface(tf);
                            delete_btn.setTypeface(tf);

                            String fontPath1 = "fonts/calibri.ttf";
                            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
                            card.setTypeface(tf1);
                            card_number.setTypeface(tf1);
                            id.setTypeface(tf1);
                            month.setTypeface(tf1);
                            year.setTypeface(tf1);

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
                    listView.setAdapter(arrayAdapter);

                }

                JSONArray array1 = new JSONArray(check);
                for(int i = 0; i < array1.length(); i++) {
                    JSONObject object1 = (JSONObject) array1.get(i);
                    final String account_id = object1.getString("id");
                    String routing_no = object1.getString("routing_number");
                    String account_no = object1.getString("account_number");
                    System.out.println("ressss:account_id::" + account_id);
                    System.out.println("ressss:routing_no::" + routing_no);
                    System.out.println("ressss:account_number::" + account_no);
                    HashMap<String,String> map1 = new HashMap<String,String>();
                    map1.put("userId",user_id);
                    map1.put("id",account_id);
                    map1.put("routing", routing_no);
                    map1.put("account", account_no);
                    accountItems.add(map1);
                    System.out.println("menuitems:::" + accountItems);

                    AccountAdapter arrayAdapter1 = new AccountAdapter(this, accountItems){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            TextView id = (TextView)view.findViewById(R.id.acc_id);
                            TextView routing = (TextView)view.findViewById(R.id.text2);
                            TextView account = (TextView)view.findViewById(R.id.text4);
                            Button edit_btn = (Button)view.findViewById(R.id.edit);
                            Button delete_btn = (Button)view.findViewById(R.id.delete);

                            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
                            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
                            edit_btn.setTypeface(tf);
                            delete_btn.setTypeface(tf);

                            String fontPath1 = "fonts/calibri.ttf";
                            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
                            routing.setTypeface(tf1);
                            id.setTypeface(tf1);
                            account.setTypeface(tf1);

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
                    listView1.setAdapter(arrayAdapter1);

                }

                JSONArray array2 = new JSONArray(paypal);
                for(int i = 0; i < array2.length(); i++) {
                    JSONObject object2 = (JSONObject) array2.get(i);
                    final String id = object2.getString("id");
                    String email = object2.getString("email");
                    String firstname = object2.getString("firstname");
                    String lastname = object2.getString("lastname");
                    String userid = object2.getString("user_id");
                    System.out.println("ressss:email::" + email);
                    System.out.println("ressss:userid::" + userid);
                    System.out.println("ressss:id::" + id);
                    System.out.println("ressss:account_number::" + firstname + "..."+lastname);
                    HashMap<String,String> map1 = new HashMap<String,String>();
                    map1.put("userId",user_id);
                    map1.put("id",id);
                    map1.put("email", email);
                    map1.put("firstname", firstname);
                    map1.put("lastname", lastname);
                    map1.put("address", address);
                    map1.put("city", city);
                    map1.put("state", state);
                    map1.put("zipcode", zipcode);
                    paypalArray.add(map1);
                    System.out.println("menuitems:::" + paypalArray);

                    PaypalAdapter arrayAdapter2 = new PaypalAdapter(this, paypalArray){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            TextView id = (TextView)view.findViewById(R.id.acc_id);
                            TextView first = (TextView)view.findViewById(R.id.text2);
                            TextView last = (TextView)view.findViewById(R.id.text5);
                            TextView mail = (TextView)view.findViewById(R.id.text4);
                            Button delete_btn = (Button)view.findViewById(R.id.delete);

                            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
                            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
                            delete_btn.setTypeface(tf);

                            String fontPath1 = "fonts/calibri.ttf";
                            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
                            first.setTypeface(tf1);
                            last.setTypeface(tf1);
                            mail.setTypeface(tf1);

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
                    listView2.setAdapter(arrayAdapter2);
                }

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
