package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private static final String URL = Constant.SERVER_URL+"delete_checking_account";
    public static String KEY_ACCOUNT_ID = "checking_account_id";
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String delete_account_id,user_id;
    HashMap<String, String> finalItems = new HashMap<String, String>();
    public AccountAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.account_listview, null);

        TextView id = (TextView)vi.findViewById(R.id.acc_id);
        TextView routing = (TextView)vi.findViewById(R.id.text2);
        TextView account = (TextView)vi.findViewById(R.id.text4);
        Button edit_btn = (Button)vi.findViewById(R.id.edit);
        Button delete_btn = (Button)vi.findViewById(R.id.delete);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String employer_id = items.get("userId");
        final String acc_id = items.get("id");
        String routing_no = items.get("routing");
        String account_no = items.get("account");

        // Setting all values in listview
        id.setText(acc_id);
        routing.setText(routing_no);
        account.setText(account_no);
        edit_btn.setTag(position);
        delete_btn.setTag(position);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalItems = data.get((Integer) v.getTag());
                user_id = finalItems.get("userId");
                String acc_id = finalItems.get("id");
                Intent intent = new Intent(activity, UpdateAccount.class);
                intent.putExtra("id", acc_id);
                intent.putExtra("userId", user_id);
                v.getContext().startActivity(intent);

            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalItems = data.get((Integer) v.getTag());
                delete_account_id = finalItems.get("id");
                System.out.println("OOOOOOOOOOOOOOO:delete_account_id:" + delete_account_id);
                webService();
            }
        });


        return vi;
    }

    private void webService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                          //  Toast.makeText(activity,"Not Connected",Toast.LENGTH_LONG).show();
                            final Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.gray_custom);

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
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
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
                map.put(APP_KEY, value);
                map.put(KEY_ACCOUNT_ID, delete_account_id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {

                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Deleted Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        user_id = finalItems.get("userId");
                        System.out.println("OOOOOOOOOOOOOOO:" + user_id);
                        Intent i = new Intent(activity,ManagePaymentOptions.class);
                        i.putExtra("userId",user_id);
                        v.getContext().startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setDimAmount(0);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}