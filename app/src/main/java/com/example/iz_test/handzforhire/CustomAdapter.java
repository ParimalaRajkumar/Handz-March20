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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater=null;
    private static final String URL = Constant.SERVER_URL+"delete_credit_card";
    public static String KEY_CARDID = "card_id";
    String delete_card_id,user_id;
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    HashMap<String, String> finalItems = new HashMap<String, String>();
        public CustomAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
                vi = inflater.inflate(R.layout.custom_listview, null);

            TextView card = (TextView)vi.findViewById(R.id.card_type);
            TextView card_number = (TextView)vi.findViewById(R.id.card_no);
            TextView id = (TextView)vi.findViewById(R.id.card_id);
            TextView month = (TextView)vi.findViewById(R.id.month);
            TextView year = (TextView)vi.findViewById(R.id.year);
            Button edit_btn = (Button)vi.findViewById(R.id.edit);
            Button delete_btn = (Button)vi.findViewById(R.id.delete);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String employer_id = items.get("userId");
            final String card_id = items.get("id");
            String card_type = items.get("type");
            String mm = items.get("month");
            String yy = items.get("year");
            String card_no = items.get("card_number");
            String number = "**** **** **** " + card_no.substring(card_no.length() - 4);

            // Setting all values in listview
            card.setText(card_type);
            month.setText(mm);
            year.setText(yy);
            id.setText(card_id);
            card_number.setText(number);
            edit_btn.setTag(position);
            delete_btn.setTag(position);
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finalItems = data.get((Integer) v.getTag());
                    user_id = finalItems.get("userId");
                    String c_id = finalItems.get("id");
                    Intent intent = new Intent(activity, UpdateCard.class);
                    intent.putExtra("id", c_id);
                    intent.putExtra("userId", user_id);
                    v.getContext().startActivity(intent);

                }
            });

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalItems = data.get((Integer) v.getTag());
                    delete_card_id = finalItems.get("id");
                    System.out.println("OOOOOOOOOOOOOOO:delete_card_id:" + delete_card_id);
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

                            //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(APP_KEY, value);
                    map.put(KEY_CARDID, delete_card_id);
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
                        Intent i = new Intent(activity,ManagePaymentOptions.class);
                        i.putExtra("userId",user_id);
                        v.getContext().startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}