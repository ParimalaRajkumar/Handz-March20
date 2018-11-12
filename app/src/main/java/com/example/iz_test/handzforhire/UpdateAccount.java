package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UpdateAccount extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    EditText name,bank,account,re_account,driver,st;
    CheckBox check;
    Button update;
   // ProgressDialog dialog;
    RelativeLayout layout;
    String employer_id,account_id,default_account;
    String acc_name,routing_no,acc_no,re_acc_no,license_no,state;
    private static final String URL = Constant.SERVER_URL+"view_checking_account";
    private static final String URL1 = Constant.SERVER_URL+"checking_account_update";
    public static String NAME = "name";
    public static String ACCOUNT_NUMBER = "account_number";
    public static String CARD_TYPE = "card_type";
    public static String ROUTING_NUMBER = "routing_number";
    public static String LICENSE_NUMBER = "license_number";
    public static String DEFAULT_ACCOUNT = "default_account";
    public static String STATE = "state";
    public static String EMPLOYER_ID = "employer_id";
    public static String CHECKING_ACCOUNT_ID = "checking_account_id";
    public static String STATUS = "status";
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String dev = "";
    String status = "1";
    String getAcc_name,getRouting_no,getAcc_no,getLicense_no,getState,getDefault;
    ImageView logo;
    Dialog dialog;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_account);

       /* dialog = new ProgressDialog(this);
        dialog.setMessage("Loading.Please wait.....");
        dialog.show();*/
        dialog = new Dialog(UpdateAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        name = (EditText)findViewById(R.id.up_ac_name);
        bank = (EditText)findViewById(R.id.up_brn);
        account = (EditText)findViewById(R.id.up_ac_no);
        re_account = (EditText)findViewById(R.id.up_re_ac_no);
        driver = (EditText)findViewById(R.id.up_lic_no);
        st = (EditText)findViewById(R.id.up_sel_state);
        check = (CheckBox)findViewById(R.id.default_account);
        update = (Button)findViewById(R.id.update_account);
        layout = (RelativeLayout) findViewById(R.id.layout);
        logo = (ImageView)findViewById(R.id.logo);

        Intent i = getIntent();
        account_id = i.getStringExtra("id");
        System.out.println("cccccccccard:account_id::" + account_id);
        employer_id = i.getStringExtra("userId");

        detector = new SimpleGestureFilter(this,this);

        webService();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_name = name.getText().toString().trim();
                routing_no = bank.getText().toString().trim();
                acc_no = account.getText().toString().trim();
                re_acc_no = re_account.getText().toString().trim();
                license_no = driver.getText().toString().trim();
                state = st.getText().toString().trim();

                if(check.isChecked())
                {
                    default_account = "1";
                }
                else
                {
                    default_account = "0";
                }

                if(acc_no.equals(re_acc_no))
                {
                    updateCard();
                }
                else
                {
                    // custom dialog
                    final Dialog dialog = new Dialog(UpdateAccount.this);
                    dialog.setContentView(R.layout.gray_custom);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Checking Account Number and Re-Enter Checking Account Number does not match");
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
                    return;
                }
            }
        });

    }

    public void updateCard() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1,
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
                            Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(UpdateAccount.this);
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
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put(APP_KEY, value);
                params.put(NAME, acc_name);
                params.put(ACCOUNT_NUMBER, acc_no);
                params.put(ROUTING_NUMBER, routing_no);
                params.put(LICENSE_NUMBER, license_no);
                params.put(DEFAULT_ACCOUNT, default_account);
                params.put(STATE, state);
                params.put(EMPLOYER_ID, employer_id);
                params.put(STATUS, status);
                params.put(CHECKING_ACCOUNT_ID, account_id);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if(status.equals("success"))
            {
                // custom dialog
                final Dialog dialog = new Dialog(UpdateAccount.this);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Updated Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(UpdateAccount.this,ManagePaymentOptions.class);
                        i.putExtra("userId",employer_id);
                        startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;
            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void webService() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("error"+jsonObject);
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APP_KEY, value);
                map.put(CHECKING_ACCOUNT_ID, account_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        String checkingData = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            checkingData = jResult.getString("checking_data");
            System.out.println("cccccccccccccc:" + checkingData);

            if(status.equals("success"))
            {
                JSONObject object = new JSONObject(checkingData);
                getAcc_name = object.getString("name");
                getRouting_no = object.getString("routing_number");
                getAcc_no = object.getString("account_number");
                getLicense_no = object.getString("license_number");
                getState = object.getString("state");
                getDefault = object.getString("default_account");
                System.out.println("ressss:getAcc_name::"+ getAcc_name);
                System.out.println("ressss:getRouting_no::"+ getRouting_no);
                System.out.println("ressss::getAcc_no:" + getAcc_no);
                System.out.println("ressss:getLicense_no::" + getLicense_no);
                System.out.println("ressss:getState::" + getState);
                System.out.println("ressss:getDefault::" + getDefault);
                name.setText(getAcc_name);
                bank.setText(getRouting_no);
                account.setText(getAcc_no);
                driver.setText(getLicense_no);
                st.setText(getState);
                re_account.setText(getAcc_no);
                if(getDefault.equals("1"))
                {
                    check.setChecked(true);
                }
                else
                {
                    check.setChecked(false);
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

}
