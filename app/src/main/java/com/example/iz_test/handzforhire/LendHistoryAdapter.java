package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LendHistoryAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> tempdata;
    private static LayoutInflater inflater = null;
    String jobId;
   HistoryFilter filter;
    private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    public static String JOB_ID = "job_id";
    Dialog dialog;
    public LendHistoryAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        tempdata=new ArrayList<HashMap<String, String>>();
        tempdata.addAll(d);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
            vi = inflater.inflate(R.layout.lend_history_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.name);
        TextView amount = (TextView) vi.findViewById(R.id.amount);
        ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
        Button job_details = (Button) vi.findViewById(R.id.job_detail_btn);
        TextView date = (TextView) vi.findViewById(R.id.date);


        final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
        final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
        final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
        Button chat = (Button) vi.findViewById(R.id.message_btn);
        TextView message_count = (TextView) vi.findViewById(R.id.message_count);
        TextView rating_count = (TextView) vi.findViewById(R.id.rating_count);

        Button leave_rating = (Button) vi.findViewById(R.id.leave_rating_btn);
        Button edit_rating = (Button) vi.findViewById(R.id.edit_rating_btn);
        Button edit_comment = (Button) vi.findViewById(R.id.edit_comment_btn);
        Button leave_comment = (Button) vi.findViewById(R.id.leave_comment_btn);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        String fontPath1 = "fonts/calibri.ttf";
        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("name");
        final String get_image = items.get("image");
        final String get_profile = items.get("profile");
        final String get_amount = items.get("payment");
        final String get_id = items.get("user_id");
        final String get_jobid = items.get("jobId");
        final String get_employer = items.get("employer");
        final String get_employee = items.get("employee");
        final String channel_id=items.get("channel");
        final String user_name=items.get("user");
        final String rating_value=items.get("rating");
        final String msg_notification=items.get("message_count");
        final String star_notification=items.get("star_count");
        final String transaction_date=items.get("transaction_date");
        final String job_status =items.get("job_status");
        final String lend_status =items.get("lend_status");
        final String comments =items.get("comments");
        System.out.println("lllllllllllll:job_status:"+job_status+",lend_status,,"+lend_status+",,comments::"+comments);

        job_name.setText(get_name);
        job_name.setTypeface(font);
        String s1 = "1.00";
        String multi = String.valueOf(Float.valueOf(get_amount)*Float.valueOf(s1));
        String total_amount = String.format("%.2f", Float.valueOf(multi));
        amount.setText(total_amount);
        job_id.setText(get_jobid);
        employer_id.setText(get_employer);
        employee_id.setText(get_employee);
        date.setText(transaction_date);

        System.out.println("on comments "+items);

        if(msg_notification.equals("0"))
        {
            message_count.setVisibility(View.INVISIBLE);
        }
        else
        {
            message_count.setVisibility(View.VISIBLE);
            message_count.setText(msg_notification);
        }

        if(star_notification.equals("0"))
        {
            rating_count.setVisibility(View.INVISIBLE);
        }
        else
        {
            rating_count.setVisibility(View.VISIBLE);
            rating_count.setText(star_notification);
        }

        if(job_status.equals("job_canceled"))
        {
            if(rating_value.equals(""))
            {
                edit_rating.setVisibility(View.GONE);
                leave_rating.setVisibility(View.GONE);
                leave_comment.setVisibility(View.VISIBLE);
                edit_comment.setVisibility(View.GONE);
            }
            else
            {
                edit_rating.setVisibility(View.GONE);
                leave_rating.setVisibility(View.GONE);
                leave_comment.setVisibility(View.GONE);
                edit_comment.setVisibility(View.VISIBLE);
            }
        }
        if(job_status.equals("payment"))
        {
            if(rating_value.equals(""))
            {
                edit_rating.setVisibility(View.GONE);
                leave_rating.setVisibility(View.VISIBLE);
                leave_comment.setVisibility(View.GONE);
                edit_comment.setVisibility(View.GONE);
            }
            else
            {
                leave_rating.setVisibility(View.GONE);
                edit_rating.setVisibility(View.VISIBLE);
                leave_comment.setVisibility(View.GONE);
                edit_comment.setVisibility(View.GONE);
            }
        }
        chat.setTag(position);
        job_details.setTag(position);
        leave_rating.setTag(position);
        edit_rating.setTag(position);
        leave_comment.setTag(position);
        edit_comment.setTag(position);

        leave_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> items = new HashMap<String, String>();
                items = data.get((Integer) v.getTag());

                jobId = items.get("jobId");
                getratingcount(items.get("user_id"));
                Intent intent = new Intent(activity, LendLeaveRating.class);
                intent.putExtra("jobId",items.get("jobId"));
                intent.putExtra("employer_id", items.get("employer"));
                intent.putExtra("employee_id",items.get("employee"));
                intent.putExtra("user_id",items.get("user_id"));
                intent.putExtra("profilename",items.get("profile"));
                intent.putExtra("ratingId",items.get("ratingId"));
                intent.putExtra("rating",items.get("rating"));
                intent.putExtra("image",items.get("image"));
                intent.putExtra("category1",items.get("category1"));
                intent.putExtra("category2",items.get("category2"));
                intent.putExtra("category3",items.get("category3"));
                intent.putExtra("category4",items.get("category4"));
                intent.putExtra("category5",items.get("category5"));
                intent.putExtra("username",items.get("username"));
                intent.putExtra("lend_status",items.get("lend_status"));
                intent.putExtra("job_status",items.get("job_status"));
                v.getContext().startActivity(intent);
                System.out.println("ITem "+items);
            }
        });

        edit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> items = new HashMap<String, String>();
                items = data.get((Integer) v.getTag());
                Intent intent = new Intent(activity, LendEditRating.class);
                intent.putExtra("jobId",items.get("jobId"));
                intent.putExtra("employerId", items.get("employer"));
                intent.putExtra("employeeId",items.get("employee"));
                intent.putExtra("userId",items.get("user_id"));
                intent.putExtra("name",items.get("profile"));
                intent.putExtra("ratingId",items.get("ratingId"));
                intent.putExtra("rating",items.get("rating"));
                intent.putExtra("image",items.get("image"));
                intent.putExtra("category1",items.get("category1"));
                intent.putExtra("category2",items.get("category2"));
                intent.putExtra("category3",items.get("category3"));
                intent.putExtra("category4",items.get("category4"));
                intent.putExtra("category5",items.get("category5"));
                intent.putExtra("username",items.get("username"));
                intent.putExtra("job_status",items.get("job_status"));
                v.getContext().startActivity(intent);
                System.out.println("ITem "+items);
            }
        });

        edit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> items = new HashMap<String, String>();
                items = data.get((Integer) v.getTag());
                Intent intent = new Intent(activity, LendEditComments.class);
                intent.putExtra("jobId",items.get("jobId"));
                intent.putExtra("employerId", items.get("employer"));
                intent.putExtra("employeeId",items.get("employee"));
                intent.putExtra("userId",items.get("user_id"));
                intent.putExtra("name",items.get("profile"));
                intent.putExtra("ratingId",items.get("ratingId"));
                intent.putExtra("rating",items.get("rating"));
                intent.putExtra("image",items.get("image"));
                intent.putExtra("category1",items.get("category1"));
                intent.putExtra("category2",items.get("category2"));
                intent.putExtra("category3",items.get("category3"));
                intent.putExtra("category4",items.get("category4"));
                intent.putExtra("category5",items.get("category5"));
                intent.putExtra("username",items.get("username"));
                intent.putExtra("comments",items.get("comments"));
                v.getContext().startActivity(intent);
                System.out.println("ITem "+items);
            }
        });

        leave_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> items = new HashMap<String, String>();
                items = data.get((Integer) v.getTag());
                Intent intent = new Intent(activity, LendLeaveComments.class);
                intent.putExtra("jobId",items.get("jobId"));
                intent.putExtra("employerId", items.get("employer"));
                intent.putExtra("employeeId",items.get("employee"));
                intent.putExtra("userId",items.get("user_id"));
                intent.putExtra("name",items.get("profile"));
                intent.putExtra("ratingId",items.get("ratingId"));
                intent.putExtra("rating",items.get("rating"));
                intent.putExtra("image",items.get("image"));
                intent.putExtra("category1",items.get("category1"));
                intent.putExtra("category2",items.get("category2"));
                intent.putExtra("category3",items.get("category3"));
                intent.putExtra("category4",items.get("category4"));
                intent.putExtra("category5",items.get("category5"));
                intent.putExtra("username",items.get("username"));
                intent.putExtra("lend_status",items.get("lend_status"));
                v.getContext().startActivity(intent);
                System.out.println("ITem "+items);
            }
        });

        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                int pos= (int) view.getTag();
                HashMap<String, String> items =data.get(pos);
                String username="";
                if(items.get("profile").isEmpty())
                    username=items.get("user");
                else
                    username= items.get("profile");
                jobId = items.get("jobId");

                getmsgcountjobhis(items.get("user_id"));
                Intent i = new Intent(activity,ChatNeed.class);
                i.putExtra("jobId",items.get("jobId"));
                i.putExtra("channel",items.get("channel"));
                i.putExtra("username",username);
                i.putExtra("userId", items.get("user_id"));
                i.putExtra("message_type","job_history");
                i.putExtra("user_type","employee");
                i.putExtra("receiverid",items.get("employer"));
                view.getContext().startActivity(i);
            }
        });

        job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                HashMap<String, String> items =data.get(pos);

                Intent i = new Intent(activity,JobDetails.class);
                i.putExtra("jobId",items.get("jobId"));
                i.putExtra("userId",items.get("user_id"));
                v.getContext().startActivity(i);
            }
        });

        if(get_image.equals(""))
        {

        }
        else {
            Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

        }


        return vi;
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new HistoryFilter();
        }

        return filter;
    }


    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    private class HistoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charText) {
            FilterResults results = new FilterResults();
            System.out.println("temp data "+tempdata);
// We implement here the filter logic
            if (charText == null || charText.length() == 0) {
// No filter implemented we return all the list
                results.values = tempdata;
                results.count = tempdata.size();
            }
            else {
// We perform filtering operation
                List nPlanetList = new ArrayList();
                System.out.println("size "+tempdata.size());
                for (int i=0;i<tempdata.size();i++) {
                    HashMap<String,String> map =tempdata.get(i);
                    if (map.get("name").toLowerCase(Locale.getDefault()).contains(charText)||map.get("transaction_date").toLowerCase(Locale.getDefault()).contains(charText)||map.get("profile").toLowerCase(Locale.getDefault()).contains(charText)||map.get("job_category").toLowerCase(Locale.getDefault()).contains(charText)||map.get("user").toLowerCase(Locale.getDefault()).contains(charText)||map.get("description").toLowerCase(Locale.getDefault()).contains(charText))
                        nPlanetList.add(map);

                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

// Now we have to inform the adapter about the new list filtered
            System.out.println("Data Set "+results.count);
            System.out.println("Data Set "+results);
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                data.clear();
                data.addAll((List)results.values);
                notifyDataSetChanged();
                //worldpopulationlist = (List) results.values;
                for(int i=0;i<data.size();i++)
                {
                    HashMap<String,String> map=data.get(i);
                    System.out.println("map "+map);
                }

            }
        }
    }



    public void getmsgcountjobhis(final String id) {
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
                            final Dialog dialog = new Dialog(activity);
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
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");

                                // custom dialog
                                final Dialog dialog = new Dialog(activity);
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
                params.put(JOB_ID, jobId);
                params.put(TYPE,"notificationCountMsgJobhistory");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void getratingcount(final String id) {
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
                            final Dialog dialog = new Dialog(activity);
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
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");

                                // custom dialog
                                final Dialog dialog = new Dialog(activity);
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
                params.put(JOB_ID, jobId);
                params.put(TYPE,"notificationCountStarRating");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}