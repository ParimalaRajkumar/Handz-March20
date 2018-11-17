
package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class JobHistoryAdapter extends BaseAdapter implements Filterable {
    private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    // Declare Variables
    Context mContext;
    Activity activity;
    LayoutInflater inflater;
    private List<WorldPopulation> worldpopulationlist = null;
    private ArrayList<WorldPopulation> arraylist;
    String jobId,job_status;
    String userId,employeeId;;
    String profile_name,channel_id,username,rating_value,msg_notification,star_notification,comments;
    Dialog dialog;
    HistoryFilter filter;

    public JobHistoryAdapter(Activity context, List<WorldPopulation> worldpopulationlist)  {
        mContext = context;
        activity=context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WorldPopulation>();
        this.arraylist.addAll(worldpopulationlist);
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new HistoryFilter();
        }

        return filter;
    }

    public class ViewHolder {
        Button job_details,leave_rating,edit_rating;
        Button chat,leave_comment,edit_comment;
        TextView job_name;
        TextView job_id;
        TextView employer_id;
        TextView employee_id;
        TextView user_name;
        TextView image_text,message,star;
        ImageView image;
        ImageView image1;
        LinearLayout rehire_layout;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public WorldPopulation getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.job_history_list, null);
            // Locate the TextViews in listview_item.xml
            holder.job_name = (TextView) view.findViewById(R.id.text1);
            holder.job_id = (TextView) view.findViewById(R.id.job_id);
            holder.employer_id = (TextView) view.findViewById(R.id.employer_id);
            holder.employee_id = (TextView) view.findViewById(R.id.employee_id);
            holder.image_text = (TextView) view.findViewById(R.id.image1);
            holder.user_name = (TextView) view.findViewById(R.id.text3);
            holder.image = (ImageView)view.findViewById(R.id.img1);
            holder.job_details = (Button) view.findViewById(R.id.job_detail_btn);
            holder.chat = (Button) view.findViewById(R.id.message_btn);
            holder.leave_rating = (Button) view.findViewById(R.id.leave_rating_btn);
            holder.edit_rating = (Button) view.findViewById(R.id.edit_rating_btn);
            holder.rehire_layout = (LinearLayout) view.findViewById(R.id.rehire_lay);
            holder.message = (TextView) view.findViewById(R.id.message_count);
            holder.star = (TextView) view.findViewById(R.id.rating_count);
            holder.leave_comment = (Button) view.findViewById(R.id.leave_comment_btn);
            holder.edit_comment = (Button) view.findViewById(R.id.edit_comment_btn);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position % 2 == 1) {
            // Set a background color for ListView regular row/item
            view.setBackgroundColor(Color.parseColor("#BF178487"));
        } else {
            // Set the background color for alternate row/item
            view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
        }
        // Set the results into TextViews
        holder.job_name.setText(worldpopulationlist.get(position).getName());
        holder.job_id.setText(worldpopulationlist.get(position).getJobId());
        holder.employer_id.setText(worldpopulationlist.get(position).getEmployerId());
        holder.employee_id.setText(worldpopulationlist.get(position).getEmployeeId());
        holder.image_text.setText(worldpopulationlist.get(position).getImage());
        holder.user_name.setText(worldpopulationlist.get(position).getUsername());
        msg_notification=worldpopulationlist.get(position).getMsg_notification();
        star_notification=worldpopulationlist.get(position).getStar_notification();
        userId = worldpopulationlist.get(position).getUserid();
        profile_name = worldpopulationlist.get(position).getProfilename();
        channel_id = worldpopulationlist.get(position).getChannel();
        username = worldpopulationlist.get(position).getUsername();
        rating_value=worldpopulationlist.get(position).getRatingValue();
        employeeId = worldpopulationlist.get(position).getEmployeeId();
        employeeId = worldpopulationlist.get(position).getEmployeeId();
        job_status = worldpopulationlist.get(position).getJobStatus();
        comments = worldpopulationlist.get(position).getComments();
        System.out.println("ssssssssssadapter:job_status:::"+job_status);
        System.out.println("ssssssssssadapter:rating_value:::"+rating_value);
        System.out.println("ssssssssssadapter:comments:::"+comments);

        holder.chat.setTag(position);
        holder.leave_rating.setTag(position);
        holder.job_details.setTag(position);
        holder.edit_rating.setTag(position);
        holder.rehire_layout.setTag(position);
        holder.leave_comment.setTag(position);
        holder.edit_comment.setTag(position);

        if(job_status.equals("job_canceled"))
        {
            if(comments!= null && comments.equals(""))
            {
                holder.edit_rating.setVisibility(View.GONE);
                holder.leave_rating.setVisibility(View.GONE);
                holder.leave_comment.setVisibility(View.VISIBLE);
                holder.edit_comment.setVisibility(View.GONE);
            }
            else
            {
                holder.edit_rating.setVisibility(View.GONE);
                holder.leave_rating.setVisibility(View.GONE);
                holder.leave_comment.setVisibility(View.GONE);
                holder.edit_comment.setVisibility(View.VISIBLE);
            }
        }
        if(job_status.equals("payment"))
        {
            if(rating_value.equals(""))
            {
                holder.edit_rating.setVisibility(View.GONE);
                holder.leave_rating.setVisibility(View.VISIBLE);
                holder.leave_comment.setVisibility(View.GONE);
                holder.edit_comment.setVisibility(View.GONE);
            }
            else
            {
                holder.leave_rating.setVisibility(View.GONE);
                holder.edit_rating.setVisibility(View.VISIBLE);
                holder.leave_comment.setVisibility(View.GONE);
                holder.edit_comment.setVisibility(View.GONE);
            }

        }

        Glide.with(mContext).load(worldpopulationlist.get(position).getImage()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(holder.image);
        if(msg_notification.equals("0"))
        {
            holder.message.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(msg_notification);
        }

        if(star_notification.equals("0"))
        {
            holder.star.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.star.setVisibility(View.VISIBLE);
            holder.star.setText(star_notification);

        }

        holder.leave_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);

                if(item.getProfilename().isEmpty())
                    username=item.getUsername();
                else
                    username=item.getProfilename();
                jobId = item.getJobId();
                getratingcount(item.getUserid());

                Intent intent = new Intent(mContext, LeaveRating.class);
                intent.putExtra("jobId", item.getJobId());
                intent.putExtra("employer_id",item.getEmployerId());
                intent.putExtra("employee_id",item.getEmployeeId());
                intent.putExtra("user_id",item.getUserid());
                intent.putExtra("image",item.getImage());
                intent.putExtra("profilename",username);
                intent.putExtra("ratingId",item.getRatingId());
                v.getContext().startActivity(intent);
            }
        });


        holder.edit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                jobId = item.getJobId();
                String employerId = item.getEmployerId();
                String employeeId = item.getEmployeeId();
                String profile_image =item.getImage();
                Intent intent = new Intent(mContext, EditRating.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("employer_id",employerId);
                intent.putExtra("employee_id",employeeId);
                intent.putExtra("user_id",item.getUserid());
                intent.putExtra("image",profile_image);
                intent.putExtra("ratingId",item.getRatingId());
                intent.putExtra("cat1",item.getCategory1());
                intent.putExtra("cat2",item.getCategory2());
                intent.putExtra("cat3",item.getCategory3());
                intent.putExtra("cat4",item.getCategory4());
                intent.putExtra("cat5",item.getCategory5());
                intent.putExtra("profilename",profile_name);
                intent.putExtra("username",username);
                v.getContext().startActivity(intent);
            }
        });

        holder.rehire_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                Intent i = new Intent(mContext,RehireJob.class);
                i.putExtra("userId",item.getUserid());
                i.putExtra("jobId", item.getJobId());
                i.putExtra("employeeId", item.getEmployeeId());
                v.getContext().startActivity(i);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int pos= (int) view.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);

                jobId = item.getJobId();
                channel_id=item.getChannel();
                if(item.getProfilename().isEmpty())
                    username=item.getUsername();
                else
                    username=item.getProfilename();
                userId=item.getUserid();

                getmsgcountjobhis(item.getUserid());

                Intent i = new Intent(mContext,ChatNeed.class);
                i.putExtra("jobId",jobId);
                i.putExtra("channel",channel_id);
                i.putExtra("username",username);
                i.putExtra("userId",userId);
                i.putExtra("message_type","job_history");
                i.putExtra("user_type","employer");
                i.putExtra("receiverid",item.getEmployeeId());
                view.getContext().startActivity(i);


                System.out.println("channel id "+channel_id);

            }
        });

        holder.job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                Intent i = new Intent(mContext,JobDetails.class);
                i.putExtra("jobId",item.getJobId());
                i.putExtra("userId",item.getUserid());
                i.putExtra("employeeId", item.getEmployeeId());
                v.getContext().startActivity(i);
            }
        });

        holder.edit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                jobId = item.getJobId();
                String employerId = item.getEmployerId();
                String employeeId = item.getEmployeeId();
                String profile_image =item.getImage();
                Intent intent = new Intent(mContext, JobCancelEditComments.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("employerId",employerId);
                intent.putExtra("employeeId",employeeId);
                intent.putExtra("userId",item.getUserid());
                intent.putExtra("image",profile_image);
                intent.putExtra("ratingId",item.getRatingId());
                intent.putExtra("category1","");
                intent.putExtra("category2","");
                intent.putExtra("category3","");
                intent.putExtra("category4","");
                intent.putExtra("category5","");
                intent.putExtra("rating","");
                intent.putExtra("name",profile_name);
                intent.putExtra("username",username);
                intent.putExtra("comments",item.getComments());
                v.getContext().startActivity(intent);
            }
        });

        holder.leave_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                jobId = item.getJobId();
                String employerId = item.getEmployerId();
                String employeeId = item.getEmployeeId();
                String profile_image =item.getImage();
                Intent intent = new Intent(mContext, JobCancelLeaveComments.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("employerId",employerId);
                intent.putExtra("employeeId",employeeId);
                intent.putExtra("userId",item.getUserid());
                intent.putExtra("image",profile_image);
                intent.putExtra("ratingId",item.getRatingId());
                intent.putExtra("category1","");
                intent.putExtra("category2","");
                intent.putExtra("category3","");
                intent.putExtra("category4","");
                intent.putExtra("category5","");
                intent.putExtra("rating","");
                intent.putExtra("name",profile_name);
                intent.putExtra("username",username);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }

  /*      // Filter Class
        public void filter(String charText,List<WorldPopulation> populationlist) {
            charText = charText.toLowerCase(Locale.getDefault());
            System.out.println(populationlist.size());
            worldpopulationlist.clear();
            if (charText.length() == 0) {
                worldpopulationlist.addAll(arraylist);
            }
            else
            {
                for (WorldPopulation wp : arraylist)
                {

                    System.out.println("charText "+charText);
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTransaction_date().toLowerCase(Locale.getDefault()).contains(charText)||wp.getProfilename().toLowerCase(Locale.getDefault()).contains(charText)||wp.getJob_category().toLowerCase(Locale.getDefault()).contains(charText)||wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        System.out.println("job name "+wp.getName());
                        worldpopulationlist.add(wp);
                    }
                }
            }
        *//*    adapter = new JobHistoryAdapter(mContext, worldpopulationlist) {
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
             list.setAdapter(adapter);
            System.out.println("length "+charText.length());
            System.out.println("size "+worldpopulationlist.size());*//*
            // DataBind ListView with items from ArrayAdapter

           this.notifyDataSetChanged();
        }*/

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



    private class HistoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charText) {
            FilterResults results = new FilterResults();
// We implement here the filter logic
            if (charText == null || charText.length() == 0) {
// No filter implemented we return all the list
                results.values = arraylist;
                results.count = arraylist.size();
            }
            else {
// We perform filtering operation
                List nPlanetList = new ArrayList();

                for (int i=0;i<arraylist.size();i++) {
                    WorldPopulation wp=arraylist.get(i);
                    // if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTransaction_date().toLowerCase(Locale.getDefault()).contains(charText)||wp.getProfilename().toLowerCase(Locale.getDefault()).contains(charText)||wp.getJob_category().toLowerCase(Locale.getDefault()).contains(charText)||wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText))
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))

                        nPlanetList.add(wp);
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
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                worldpopulationlist.clear();
                worldpopulationlist.addAll((List)results.values);
                notifyDataSetChanged();
                //worldpopulationlist = (List) results.values;
                for(int i=0;i<worldpopulationlist.size();i++)
                {
                    WorldPopulation wp=worldpopulationlist.get(i);
                    System.out.println("wp "+wp.getName());
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
