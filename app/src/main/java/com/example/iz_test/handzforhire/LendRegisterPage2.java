package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class LendRegisterPage2 extends Activity{

        Button next;
        EditText first_name,last_name,address1,address2,city,state,zipcode,email,retype_email;
        String f_name,l_name,add1,add2,cit,stat,zip,em,re_email;
        RelativeLayout layout;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        private static final String URL = Constant.SERVER_URL+"user_email_check";
        public static String XAPP_KEY = "X-APP-KEY";
        String value = "HandzForHire@~";
        public static String KEY_EMAIL = "email";
        ImageView logo;
        SessionManager sessionManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lend_register_page2);

            next = (Button) findViewById(R.id.next1);
            first_name = (EditText) findViewById(R.id.first_name);
            last_name = (EditText) findViewById(R.id.last_name);
            address1 = (EditText)findViewById(R.id.address1);
            address2 = (EditText)findViewById(R.id.address2);
            city = (EditText)findViewById(R.id.city);
            state = (EditText)findViewById(R.id.state);
            zipcode = (EditText)findViewById(R.id.zipcode);
            email = (EditText)findViewById(R.id.email);
            retype_email = (EditText)findViewById(R.id.retype_email);
            layout = (RelativeLayout) findViewById(R.id.layout);
            logo = (ImageView) findViewById(R.id.logo);

            sessionManager=new SessionManager(LendRegisterPage2.this);

            state.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            });


            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LendRegisterPage2.this,LendLoginPage.class);
                    startActivity(i);
                    finish();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    f_name = first_name.getText().toString().trim();
                    l_name = last_name.getText().toString().trim();
                    add1 = address1.getText().toString().trim();
                    add2 = address2.getText().toString().trim();
                    cit = city.getText().toString().trim();
                    stat = state.getText().toString().trim();
                    zip = zipcode.getText().toString().trim();
                    em = email.getText().toString().trim();
                    re_email = retype_email.getText().toString().trim();

                    if(TextUtils.isEmpty(f_name))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"First Name\" Box");
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
                    if(TextUtils.isEmpty(l_name))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Last Name\" Box");
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
                    if(TextUtils.isEmpty(add1))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Street Address 1\" Box");
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
                    if(TextUtils.isEmpty(cit))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"City\" Box");
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
                    if(TextUtils.isEmpty(stat))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"State\" Box");
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
                    if(TextUtils.isEmpty(zip))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Zip Code\" Box");
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
                    if(TextUtils.isEmpty(em))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Email Address\" Box");
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
                    if(TextUtils.isEmpty(re_email))
                    {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Must Fill In \"Retype Email Address\" Box");
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
                    if (!em.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Please input \"Valid Email Address\"");
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
                    if(em.equals(re_email))
                    {
                        webservice();
                    }

                    else{
                        // custom dialog
                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Email and Retype Email Address does not match");
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
                }
            });
        }

        public void webservice() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("resssssssssssssssss:" + response);
                            onResponserecieved1(response, 2);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                                final Dialog dialog = new Dialog(LendRegisterPage2.this);
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
                                    System.out.println("eeeeeeeeeeeeeeeror:" + jsonObject);
                                    String status = jsonObject.getString("msg");
                                    if (!status.equals("")) {
                                        // custom dialog
                                        final Dialog dialog = new Dialog(LendRegisterPage2.this);
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
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(XAPP_KEY, value);
                    map.put(KEY_EMAIL, em);
                    map.put(Constant.DEVICE, Constant.ANDROID);
                    System.out.println("Params "+ map);
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        public void onResponserecieved1(String jsonobject, int requesttype) {
            String status = null;
            String email_id = null;
            try {

                JSONObject jResult = new JSONObject(jsonobject);

                status = jResult.getString("status");

                if(status.equals("success"))
                {
                    Intent i = new Intent(LendRegisterPage2.this, LendRegisterPage3.class);
                    i.putExtra("isfrom", "reg");
                    HashMap<String,String> map= new HashMap<String, String>();
                    map.put("firstname",f_name);
                    map.put("lastname",l_name);
                    map.put("address1",add1);
                    map.put("city",cit);
                    map.put("state",stat);
                    map.put("zip",zip);
                    map.put("email",em);
                    map.put("address2",add2);
                    map.put("retype_email",re_email);
                    JSONObject object = new JSONObject(map);
                    sessionManager.saveregistrationdet(object.toString());


                  /*  i.putExtra("firstname", f_name);
                    i.putExtra("lastname", l_name);
                    i.putExtra("address1", add1);
                    i.putExtra("city", cit);
                    i.putExtra("state", stat);
                    i.putExtra("zip", zip);
                    i.putExtra("email", em);
                    i.putExtra("address2", add2);
                    i.putExtra("retype_email", re_email);*/
                    startActivity(i);
                    finish();
                }
                else
                {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }