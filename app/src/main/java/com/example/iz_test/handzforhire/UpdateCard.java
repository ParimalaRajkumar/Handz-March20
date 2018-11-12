package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class UpdateCard extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    String card_id,employer_id;
    private static final String URL = Constant.SERVER_URL+"view_credit_card";
    private static final String URL1 = Constant.SERVER_URL+"credit_card_update";
    public static String KEY_CARD_ID = "card_id";
    EditText card_name,card_number,mm,yy,cv,add,c,s,z;
    String name,card_no,month,year,cvv,address,city,state,zipcode,de_card;
    CheckBox check;
    Button update;
    public static String NAME = "name";
    public static String CARD_NUMBER = "card_number";
    public static String CARD_TYPE = "card_type";
    public static String EXP_MONTH = "exp_month";
    public static String EXP_YEAR = "exp_year";
    public static String CVV = "cvv";
    public static String EMPLOYER_ID = "employer_id";
    public static String ADDRESS_CARD = "address_card";
    public static String STATE = "state";
    public static String CITY = "city";
    public static String ZIPCODE = "zipcode";
    public static String DEFAULT_CARD = "default_card";
    public static String CARD_ID = "card_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String DEVICETOKEN = "dev";
    String value = "HandzForHire@~";
    String dev = "";
    //ProgressDialog dialog;
    Dialog dialog;
    RelativeLayout layout;
    String cardtype,getDefaultCard;
    String update_name,update_card_number,update_month,update_year,update_cvv,update_address,update_city,update_state,update_zipcode;
    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_credit);
        dialog = new Dialog(UpdateCard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        card_name = (EditText) findViewById(R.id.up_card);
        card_number = (EditText) findViewById(R.id.up_card_no);
        mm = (EditText) findViewById(R.id.up_month);
        yy = (EditText) findViewById(R.id.up_year);
        cv = (EditText) findViewById(R.id.up_cvv);
        add = (EditText) findViewById(R.id.up_card_acc);
        c = (EditText) findViewById(R.id.up_city);
        s = (EditText) findViewById(R.id.up_state);
        z = (EditText) findViewById(R.id.up_zipcode);
        check = (CheckBox) findViewById(R.id.up_checkbox);
        update = (Button) findViewById(R.id.update_card);
        layout = (RelativeLayout)findViewById(R.id.layout);

        Intent i = getIntent();
        card_id = i.getStringExtra("id");
        System.out.println("cccccccccard:id::" + card_id);
        employer_id = i.getStringExtra("userId");
        System.out.println("employer_id::" + employer_id);

        detector = new SimpleGestureFilter(this,this);
        webService();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_name = card_name.getText().toString().trim();
                update_card_number = card_number.getText().toString().trim();
                update_month = mm.getText().toString().trim();
                update_year = yy.getText().toString().trim();
                update_cvv = cv.getText().toString().trim();
                update_address = add.getText().toString().trim();
                update_city = c.getText().toString().trim();
                update_state = s.getText().toString().trim();
                update_zipcode = z.getText().toString().trim();

                if(check.isChecked())
                {
                    de_card = "1";
                }
                else
                {
                    de_card = "0";
                }

                CardType type = CardType.fromCardNumber(update_card_number);
                cardtype = type.toString();
                System.out.println("tttttttttttype:card:" + cardtype);
                if(cardtype.equals("Unknown"))
                {
                    // custom dialog
                    final Dialog dialog = new Dialog(UpdateCard.this);
                    dialog.setContentView(R.layout.gray_custom);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Please input \"Valid Card Number\"");
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
                else
                {
                    updateCard();
                }

            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
                            final Dialog dialog = new Dialog(UpdateCard.this);
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
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(UpdateCard.this);
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

                params.put(XAPP_KEY, value);
                params.put(NAME, update_name);
                params.put(CARD_NUMBER, update_card_number);
                params.put(CARD_TYPE, cardtype);
                params.put(EXP_MONTH, update_month);
                params.put(EXP_YEAR, update_year);
                params.put(CVV, update_cvv);
                params.put(EMPLOYER_ID, employer_id);
                params.put(ADDRESS_CARD, update_address);
                params.put(STATE, update_state);
                params.put(CITY, update_city);
                params.put(ZIPCODE, update_zipcode);
                params.put(DEFAULT_CARD, de_card);
                params.put(CARD_ID, card_id);
                params.put(DEVICETOKEN, dev);
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
                final Dialog dialog = new Dialog(UpdateCard.this);
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
                        Intent i = new Intent(UpdateCard.this,ManagePaymentOptions.class);
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
                map.put(KEY_CARD_ID, card_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        String cardData = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            cardData = jResult.getString("card_data");

            if(status.equals("success"))
            {
                    JSONObject object = new JSONObject(cardData);
                    name = object.getString("name");
                    card_no = object.getString("card_number");
                     month = object.getString("exp_month");
                     year = object.getString("exp_year");
                     cvv = object.getString("cvv");
                     address = object.getString("address_card");
                     city = object.getString("city");
                     state = object.getString("state");
                     zipcode = object.getString("zipcode");
                getDefaultCard = object.getString("default_card");
                    System.out.println("ressss:name::"+ name);
                    System.out.println("ressss:card_no::"+ card_no);
                    System.out.println("ressss::month:"+ month);
                    System.out.println("ressss:year::"+ year);
                    System.out.println("ressss:cvv::"+ cvv);
                    System.out.println("ressss:address::"+ address);
                    System.out.println("ressss:state::"+ city);
                    System.out.println("ressss:zipcode::"+ state);
                    System.out.println("ressss:zipcode::"+ zipcode);
                System.out.println("ressss:zipcode::"+ getDefaultCard);
                card_name.setText(name);
                card_number.setText(card_no);
                mm.setText(month);
                yy.setText(year);
                cv.setText(cvv);
                add.setText(address);
                c.setText(city);
                s.setText(state);
                z.setText(zipcode);
                if(getDefaultCard.equals("1"))
                {
                    check.setChecked(true);
                }
                else
                {
                    check.setChecked(false);
                }
                dialog.dismiss();
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
