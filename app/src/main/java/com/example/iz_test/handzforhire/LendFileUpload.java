package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
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

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LendFileUpload {

    public static String user_id;
    String type;
    public static String firstRemoteFile;
    private static final String URL = Constant.SERVER_URL+"update_profile_image";
    public static String KEY_USERID = "user_id";
    public static String KEY_PROFILE_IMAGE = "profile_image";
    public static String KEY_PROFILE_NAME = "profile_name";
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String id;
    Activity activity;

    /*********
     * work only for Dedicated IP
     ***********/
    static final String FTP_HOST = "52.89.130.200";

    /*********
     * FTP USERNAME
     ***********/
    static final String FTP_USER = "handz";

    /*********
     * FTP PASSWORD
     ***********/
    static final String FTP_PASS = "Y*b~{Zd]<uJ6BAzP";
    Dialog dialog;
    public LendFileUpload(String file) {

        System.out.println("save profile");
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        new UploadFile().execute(file);
    }

    public class UploadFile extends AsyncTask<String, Void, Boolean> {

        SimpleFTP ftp;
        //String filename;
        Boolean status = false;

        String ss;

        @Override
        protected Boolean doInBackground(String... params) {
            id = LendEditUserProfile.id;
            System.out.println("iiiiiiiiiiiid:fileupload::" + id);

            // ss=params[0];

            FTPClient ftpClient = new FTPClient();
            try {

                ftpClient.connect(FTP_HOST, 21);
                ftpClient.login(FTP_USER, FTP_PASS);
                ftpClient.enterLocalPassiveMode();

                ftpClient.changeWorkingDirectory("/var/www/html/assets/images/uploads/profie/");

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // APPROACH #1: uploads first file using an InputStream
                File firstLocalFile = new File(params[0]);
                System.out.println("firstLocalFile " + firstLocalFile);

                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
                Date date = new Date();

                firstRemoteFile = dateFormat.format(date) + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

                //firstRemoteFile ="large_"+mSharedData.getUserId()+".jpg";


                System.out.println("filename " + firstRemoteFile);
                InputStream inputStream = new FileInputStream(firstLocalFile);
                System.out.println("Start uploading first file");
                boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                System.out.println("Done " + done);
                inputStream.close();
                if (done) {
                    status = true;
                }


                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();

                }


            } catch (IOException ex) {
                System.out.println("IO Error: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                // status=false;
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Exception\t " + ex.getMessage());
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s = true) {
                imageUpload();
            } else {
            }
            dialog.dismiss();
            // hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //	txt_loading.setVisibility(View.VISIBLE);
            //  showProgressDialog();
            dialog.show();
        }


        private void showProgressDialog() {
           /* if (!pDialog.isShowing())
                pDialog.show();*/
        }

        private void hideProgressDialog() {
         /*   if (pDialog.isShowing())
                pDialog.dismiss();*/
        }

    }

    public void imageUpload() {
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
                            final Dialog dialog = new Dialog(LendEditUserProfile.activity);
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
                            Toast.makeText(LendEditUserProfile.activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(LendEditUserProfile.activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);

                                String status = jsonObject.getString("msg");
                                //  if (status.equals("Card number already exists.")) {
                                // custom dialog
                                final Dialog dialog = new Dialog(LendEditUserProfile.activity);
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
                map.put(KEY_PROFILE_IMAGE, firstRemoteFile);
                map.put(KEY_USERID, id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LendEditUserProfile.activity);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success")) {
                //Toast.makeText(activity,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
