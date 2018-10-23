package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.util.HashMap;
import java.util.Map;


/*
 * Created by IZ-parimala on 7/26/2015.
 */
public class RestClientPost {
    private String url;
    private int requestType;
    private static RequestQueue queue;
    private String TAG="Json Response";
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog pDialog;
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    ConnectionDetector cd;
    Context context;
    public enum RequestMethod
    {
        POST
    };


    public RestClientPost(Context context, int requestType) {
        //this.url = "http://162.144.41.156/~izaapinn/handzforhire/service/job_search";
       //this.url = "http://50.17.167.215/handz/service/job_search";
        this.url = Constant.SERVER_URL+"job_search";
        this.requestType = requestType;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        cd=new ConnectionDetector(context);
    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void execute(RequestMethod method, Activity activity,Fragment fragment)
            throws Exception {
             Log.d("", "Request params " + url);
        this.context=activity;
        postData(url, activity,(ResponseListener)fragment);
    }

    private void postData(String url, final Context activity, final ResponseListener replist) {

        if(cd.isConnectingToInternet()) {

            //queue = Volley.newRequestQueue(activity);
            if (queue == null) {
                queue = Volley.newRequestQueue(activity);
            }
            int timeout = 60000; // 60 seconds - time out

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("response on restclinet"+response);
                                JSONObject jsonResponse = new JSONObject(response);
                                replist.onResponseReceived(jsonResponse,requestType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                                Toast.makeText(context,"Not Connected",Toast.LENGTH_LONG).show();
                            }else if (error instanceof AuthFailureError) {
                                Toast.makeText(context,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                            }else if (error instanceof NetworkError) {
                                Toast.makeText(context,"Network error while performing the request",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    replist.onResponseReceived(jsonResponse, requestType);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-APP-KEY","HandzForHire@~");
                    params.put("search_type", "location");
                    params.put("lat", String.valueOf(FindJobMap.lat));
                    params.put("lon", String.valueOf(FindJobMap.lon));
                    params.put("miles","5");
                    params.put(Constant.DEVICE, Constant.ANDROID);
                    System.out.println("lat "+String.valueOf(FindJobMap.lat));
                    System.out.println("lon "+String.valueOf(FindJobMap.lon));
                    return params;
                }
            };
         /*   postRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
            queue.add(postRequest);
        }
    }

    public void execute1(RequestMethod method, Activity activity,Fragment fragment)
            throws Exception {
        Log.d("", "Request params " + url);
        this.context=activity;
        getjobdetail(url, activity,(ResponseListener)fragment);
    }
    private void getjobdetail(String url, final Context activity, final ResponseListener replist) {

        if(cd.isConnectingToInternet()) {

            //queue = Volley.newRequestQueue(activity);
            if (queue == null) {
                queue = Volley.newRequestQueue(activity);
            }
            int timeout = 60000; // 60 seconds - time out

            StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.SERVER_URL+"job_detail_view",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("response on restclinet"+response);
                                JSONObject jsonResponse = new JSONObject(response);
                                replist.onResponseReceived(jsonResponse,requestType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                                Toast.makeText(context,"Not Connected",Toast.LENGTH_LONG).show();
                            }else if (error instanceof AuthFailureError) {
                                Toast.makeText(context,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                            }else if (error instanceof NetworkError) {
                                Toast.makeText(context,"Network error while performing the request",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    replist.onResponseReceived(jsonResponse, requestType);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(APP_KEY, "HandzForHire@~");
                    params.put(JOB_ID, FindJobMap.job_id);
                    params.put(Constant.DEVICE, Constant.ANDROID);
                    return params;
                }
            };
          /*  postRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
            queue.add(postRequest);
        }
    }

}
