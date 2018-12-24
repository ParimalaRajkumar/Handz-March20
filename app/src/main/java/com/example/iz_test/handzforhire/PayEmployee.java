package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayEmployee extends Activity  implements SimpleGestureFilter.SimpleGestureListener{

    ImageView logo,image,info1,info2;
    Button payment_button;
    EditText tip;
    String job_id,employer_id,employee_id,job_name,profile_image,paypal_value,estimated_value;
    ProgressDialog progress_dialog;
    TextView name,date,total,payout,service_fee,processing_fee;
    String profile_name,user_name,job_payout,paypal_fee,estimated_payment,fee_details,job_payment_amount,merchant_id,new_payout_value;
    Integer total_value;
    Dialog dialog;

    private SimpleGestureFilter detector;
    private String current = "";
    SessionManager session;
    Firebase reference1;
    String child_id,sender_id,get_user,transaction_date;
    private static final String PAYMENT_URL = Constant.SERVER_URL+"payment_service";
    private static final String GET_REQUESTPAYMENT = Constant.SERVER_URL+"request_payment_notification";
    public static String JOB_NAME="job_name";
    public static String ORDER_ID="order_id";
    public static String TIP="tip";
    public static String TRANS_DATE="transaction_date";
    public static String PAYER_ID="payer_id";
    public static String JOB_ID="job_id";
    public static String PAYMENT_ID="payment_id";
    public static String STATUS="status";
    public static String ORDER_STATUS="order_status";
    public static String PAYPAL_FEE="paypal_fee";
    public static String EMPLOYER_ID="employer_id";
    public static String PAYMENT_METHOD="payment_method";
    public static String PAYMENT_AMOUNT="payment_amount";
    public static String APP_KEY="X-APP-KEY";
    public static String PAYEE_EMAIL="payee_email";
    public static String USER_TYPE="user_type";
    public static String PAYER_EMAIL="payer_email";
    public static String TOTAL_AMOUNT="total_payment";
    public static String EMPLOYEE_ID="employee_id";
    public static String REFERENCE_ID="reference_id";

    public static String appkey_value="HandzForHire@~";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_employee);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(PayEmployee.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        session = new SessionManager(getApplicationContext());

        logo = (ImageView)findViewById(R.id.logo);
        image = (ImageView)findViewById(R.id.imageView);
        info1 = (ImageView)findViewById(R.id.info1);
        info2 = (ImageView)findViewById(R.id.info2);
        payout = (TextView) findViewById(R.id.job_payout);
        service_fee = (TextView) findViewById(R.id.service_fee);
        processing_fee = (TextView) findViewById(R.id.processing_fee);
        tip = (EditText) findViewById(R.id.tip);
        payment_button = (Button) findViewById(R.id.payment_btn);
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.transaction_date);
        total = (TextView) findViewById(R.id.total);


       // TextView text1 = (TextView) findViewById(R.id.text1);
        TextView text2 = (TextView) findViewById(R.id.text2);
        TextView text3 = (TextView) findViewById(R.id.text3);
        TextView text4 = (TextView) findViewById(R.id.text4);
        TextView text5 = (TextView) findViewById(R.id.text5);
        TextView text6 = (TextView) findViewById(R.id.text6);
        TextView text7 = (TextView) findViewById(R.id.text7);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        name.setTypeface(tf);
        //text1.setTypeface(tf);
        text2.setTypeface(tf);
        date.setTypeface(tf);

        String fontPath2 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        text3.setTypeface(tf2);
        payout.setTypeface(tf2);
        text4.setTypeface(tf2);
        service_fee.setTypeface(tf2);
        text5.setTypeface(tf2);
        tip.setTypeface(tf2);
        text6.setTypeface(tf2);
        text7.setTypeface(tf2);
        total.setTypeface(tf2);
        processing_fee.setTypeface(tf2);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://handz-8ac86.firebaseio.com/channels");

        String paymentdetails  = session.Readpaymentdetails();
        System.out.println("Payment "+paymentdetails);
        try {

            JSONObject obj = new JSONObject(paymentdetails);
            job_id = obj.getString("jobId");
            employer_id =obj.getString("userId");
            employee_id = obj.getString("employee");
            job_name =  obj.getString("name");
            profile_image = obj.getString("image");
            profile_name =obj.getString("profile");
            user_name = obj.getString("user");
            job_payout =obj.getString("job_payout");
            paypal_fee =obj.getString("paypalfee");
            estimated_payment =obj.getString("job_estimated_payment");
            fee_details = obj.getString("fee_details");
            job_payment_amount=obj.getString("job_payment_amount");
            merchant_id=obj.getString("merchant_id");
        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
        System.out.println("Payment:::merchant_id::: "+merchant_id);

        System.out.println("job_payment_amount details  "+job_payment_amount+"-job_payout::"+job_payout+"-paypal_fee::"+paypal_fee+"-estimated_payment:::"+estimated_payment);
        Intent i = getIntent();

        if(i.getStringExtra("isfrom").equals("makepayment")) {

        }else if(i.getStringExtra("isfrom").equals("paypal")){
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String formattedDate = df.format(c.getTime());
            PaypalCon paycon=new PaypalCon(PayEmployee.this);
            String payorderapi = PaypalCon.payOrderAPI(session.ReadAccessToekn(),formattedDate,session.ReadorderID());
            try {
                JSONObject obj = new JSONObject(payorderapi);
                if(obj.has("debug_id")){
                    final Dialog dialog = new Dialog(PayEmployee.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText(obj.getString("message"));
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
                }else{

                    JSONArray array=obj.getJSONArray("links");
                    JSONObject objre=array.getJSONObject(0);
                    String orderstatusdapi=objre.getString("href");
                    String orderstatusapi = PaypalCon.orderstatusapi(session.ReadAccessToekn(),orderstatusdapi);
                    paymentservice(orderstatusapi);
                }
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }
        }

        detector = new SimpleGestureFilter(this,this);

        tip.addTextChangedListener(tw);

        new_payout_value = job_payout;
        new_payout_value = new_payout_value.substring(1);
        System.out.println("new_payout_value "+new_payout_value);

        if(fee_details.equals("add"))
        {
            payout.setText(new_payout_value);
            String get_service_fee = service_fee.getText().toString().trim();
            String get_tip = tip.getText().toString().trim();
            service_fee.setText(get_service_fee);
            paypal_value= paypal_fee;
            paypal_value = paypal_value.substring(1);
            processing_fee.setText(paypal_value);
            String get_total = String.valueOf(Float.valueOf(new_payout_value)+Float.valueOf(get_service_fee)+Float.valueOf(get_tip)+ Float.valueOf(paypal_value));
            String tot= String.format("%.2f", Float.valueOf(get_total));
            total.setText(tot);
        }
        else
        {
            estimated_value = estimated_payment;
            estimated_value = estimated_value.substring(1);
            payout.setText(estimated_value);
            String get_service_fee = service_fee.getText().toString().trim();
            String get_tip = tip.getText().toString().trim();
            service_fee.setText(get_service_fee);
            paypal_value= paypal_fee;
            paypal_value = paypal_value.substring(1);
            processing_fee.setText(paypal_value);
            String get_total = String.valueOf(Float.valueOf(estimated_value)+Float.valueOf(get_service_fee)+Float.valueOf(get_tip)+ Float.valueOf(paypal_value));
            String tot= String.format("%.2f", Float.valueOf(get_total));
            total.setText(tot);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MMMM dd, yyyy");
        transaction_date = mdformat.format(calendar.getTime());
        date.setText(transaction_date);
        System.out.println("dddddddddddddddtransaction_date:payemployee:: " + transaction_date);

        if(profile_image==null) {

        }
        else {
            if(profile_image!= null && profile_image.contains("http://graph.facebook.com/"))
            {
                profile_image = profile_image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
            }
            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
        }
        if(profile_name.equals(""))
        {
            name.setText("pay " + user_name);
        }
        else {
            name.setText("pay " +profile_name);
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayEmployee.this,ProfilePage.class);
                i.putExtra("userId",employer_id);
                startActivity(i);
            }
        });

        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("paymentbutton::: ");
                String total_value = total.getText().toString().trim();
                System.out.println("payEmployee::total_value:: "+total_value);
                session.savePaypalRedirect("1");

                PaypalCon pay=new PaypalCon(PayEmployee.this);

                String accesstoken=PaypalCon.getAccessToken();
                System.out.println("accesstoken:::"+accesstoken);

                session.saveAccesstoken(accesstoken);

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String formattedDate = df.format(c.getTime());

                String orderapibody=PaypalCon.OrderReqjson(merchant_id,total_value,formattedDate);
                System.out.println("orderapibody:::"+orderapibody);

                String returnurl = PaypalCon.OrderAPI(accesstoken,formattedDate,orderapibody);
                System.out.println("returnurl:::"+returnurl);

                try {
                    JSONObject obj = new JSONObject(returnurl);
                    String orderid=obj.getString("id");
                    session.saveorderId(orderid);

                    JSONArray array=obj.getJSONArray("links");
                    JSONObject objre=array.getJSONObject(1);
                    Intent myIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(objre.getString("href")));
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(myIntent,1);
                }catch (Exception e)
                {
                    System.out.println("e "+e.getMessage());
                }

            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(PayEmployee.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("You may add a tip for a job well done, or if the job took longer than expected.");
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
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(PayEmployee.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("This fee may minimally increase if you choose to add a tip.");
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);
                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                tip.removeTextChangedListener(this);
                tip.setText(cashAmountBuilder.toString());

                tip.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(tip.getText(), cashAmountBuilder.toString().length());

                tip.addTextChangedListener(this);
            }

            if(fee_details.equals("add"))
            {
                String new_payout = payout.getText().toString().trim();

                String new_payout_value = new_payout;
                new_payout_value = new_payout_value.substring(1);

                String new_tip = tip.getText().toString().trim();
                String add_job_payout = String.valueOf(Float.valueOf(new_payout)+Float.valueOf(new_tip));

                String s1 = "100";
                String multi = String.valueOf(Float.valueOf(s1)*Float.valueOf(add_job_payout));

                String s2 = "130";
                String add1 = String.valueOf(Float.valueOf(multi)+Float.valueOf(s2));

                String s3 = "97.1";
                String div_total = String.valueOf(Float.valueOf(add1)/Float.valueOf(s3));

                String roundup_value = String.format("%.2f", Float.valueOf(div_total));

                String handz_fee = "1.00";
                String pay_fee = String.valueOf(Float.valueOf(roundup_value)-Float.valueOf(new_payout)-Float.valueOf(new_tip)-Float.valueOf(handz_fee));
                String total_value = String.format("%.2f", Float.valueOf(pay_fee));

                processing_fee.setText(total_value);
                total.setText(roundup_value);
            }
            else
            {
                String new_payout = payout.getText().toString().trim();
                String estimated_value = estimated_payment;
                estimated_value = estimated_value.substring(1);
                String new_tip = tip.getText().toString().trim();

                String s1 = "100";
                String multi = String.valueOf(Float.valueOf(s1)*Float.valueOf(new_tip));
                String s3 = "97.1";
                String div_total = String.valueOf(Float.valueOf(multi)/Float.valueOf(s3));
                String roundup_value = String.format("%.2f", Float.valueOf(div_total));
                String total_value = String.valueOf(Float.valueOf(job_payment_amount)+Float.valueOf(roundup_value));
                String handz_fee = "1.00";
                String pay_fee = String.valueOf(Float.valueOf(total_value)-Float.valueOf(estimated_value)-Float.valueOf(new_tip)-Float.valueOf(handz_fee));
                String pay_roundup_value = String.format("%.2f", Float.valueOf(pay_fee));
                processing_fee.setText(pay_roundup_value);
                total.setText(total_value);
            }
        }
    };
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
           /* case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
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

    public void paymentservice(final String orderresposne)
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PAYMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  onResponserecieved1(response, 2);
                        System.out.println("response from izaap "+response);
                        dialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            String status=obj.getString("status");
                            if(status.equals("success"))
                            {
                                transactionstatus();
                            }
                        }catch (Exception e){
                            System.out.println("Exception "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(PayEmployee.this);
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
                                // if (status.equals("You are not allowed to apply for the job")) {
                                // custom dialog
                                final Dialog dialog = new Dialog(PayEmployee.this);
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

                try {

                    JSONObject obj = new JSONObject(orderresposne);
                    JSONObject payer_info = obj.getJSONObject("payer_info");
                    JSONObject payment_det=obj.getJSONObject("payment_details");
                    JSONArray purchase_units= obj.getJSONArray("purchase_units");
                    JSONObject objec = purchase_units.getJSONObject(0);
                    String reference_id= objec.getString("reference_id");
                    JSONObject payee = objec.getJSONObject("payee");

                    params.put(APP_KEY, appkey_value);
                    params.put(JOB_NAME, job_name);
                    params.put(ORDER_ID, obj.getString("id"));
                    params.put(TIP, tip.getText().toString());
                    params.put(TRANS_DATE, transaction_date);
                    params.put(JOB_ID, job_id);
                    params.put(PAYPAL_FEE, paypal_fee);
                    params.put(EMPLOYER_ID, employer_id);
                    params.put(PAYMENT_AMOUNT, job_payout);
                    params.put(USER_TYPE, "employer");
                    params.put(TOTAL_AMOUNT, total.getText().toString());
                    params.put(EMPLOYEE_ID, employee_id);
                    params.put(REFERENCE_ID, reference_id);
                    params.put(PAYER_ID, payer_info.getString("payer_id"));
                    params.put(PAYMENT_ID, payment_det.getString("payment_id"));
                    params.put(STATUS, "payment");
                    params.put(ORDER_STATUS, obj.getString("status"));
                    params.put(PAYMENT_METHOD, "PayPal");
                    params.put(PAYEE_EMAIL, payee.getString("email"));
                    params.put(PAYER_EMAIL, payer_info.getString("email"));

                    System.out.println("Params "+params);

                }catch (Exception e){
                    System.out.println("Exception "+e.getMessage());
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void transactionstatus() {
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
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("senderId", sender_id);
                                map.put("senderName", get_user);
                                map.put("text", "FROM HANDZ: Transaction completed on "+transaction_date+" for the amount of $"+job_payout);
                                reference1.child(child_id).child("messages").push().setValue(map);
                                Intent i =new Intent(PayEmployee.this,ProfilePage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
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
                map.put(APP_KEY, appkey_value);
                map.put(JOB_ID, job_id);
                map.put(EMPLOYER_ID, employer_id);
                map.put(EMPLOYEE_ID, employee_id);
                map.put(USER_TYPE, "employee");
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println(" Map "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PayEmployee.this);
        requestQueue.add(stringRequest);
    }

}
