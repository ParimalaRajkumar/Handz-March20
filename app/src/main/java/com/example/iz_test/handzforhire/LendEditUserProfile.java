package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.listeners.ApiResponseListener;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LendEditUserProfile extends Activity implements SimpleGestureFilter.SimpleGestureListener,ApiResponseListener<String ,String> {

    ImageView image, photo_bg;
    Button home, email, update, paypal_login, terms_condition, logo,add_link;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap finalbitmap;
    TextView photo_text,rating_value;
    EditText profile_name;
    private static final String URL = Constant.SERVER_URL+"update_profile_image";
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String PAYPAL_UPDATEURL = Constant.SERVER_URL+"user_merchant_id_update?";

    public static String KEY_PROFILE_IMAGE = "profile_image";
    public static String KEY_PROFILE_NAME = "profile_name";
    public static String APP_KEY = "X-APP-KEY";
    public static String MERCHANTID = "merchant_id";
    String value = "HandzForHire@~";
    public static String id;
    private static final String LINKEDIN_URL = Constant.SERVER_URL+"linked_in ";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String EMAIL = "email";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String ID = "id";
    public static String PROF_URL = "profile_url";
    public static String PIC_URL = "picture_url";

    String average_rating, address, city, state, zipcode,profile_image,profilename,user_name;
    String filename = "";
    private String userChoosenTask;
    LinearLayout layout;
    String employee_rating,posted_notification,pending_notification,active_notification,jobhistory_notification;
    public static Activity activity;

    /*********
     * work only for Dedicated IP
     ***********/
    static final String FTP_HOST = "162.144.41.156";

    /*********
     * FTP USERNAME
     ***********/
    static final String FTP_USER = "server@izaapinnovations.com";

    /*********
     * FTP PASSWORD
     ***********/
    static final String FTP_PASS = "Y9+CW:K_o[";
    MarshMallowPermission marshMallowPermission;
    File mediaFile;
    private int PICK_FROM_CAMERA = 1;
    String name;
    ProgressDialog progress_dialog;
    RelativeLayout rating_lay;
    Dialog dialog;
    SessionManager session;
    private SimpleGestureFilter detector;
    String merchantid;
    String firstnmae,lastnmae,lin_email,lin_id,pictureurl,profileurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_edit_user_profile);

        marshMallowPermission = new MarshMallowPermission(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        detector = new SimpleGestureFilter(this,this);

        dialog = new Dialog(LendEditUserProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        home = (Button) findViewById(R.id.change_home_address);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        update = (Button) findViewById(R.id.update_email);
        rating_value = (TextView) findViewById(R.id.text3);
        image = (ImageView) findViewById(R.id.profile_image);
        photo_text = (TextView) findViewById(R.id.text_photo);
        profile_name = (EditText) findViewById(R.id.name);
        paypal_login = (Button) findViewById(R.id.login);
        terms_condition = (Button) findViewById(R.id.condition);
        logo = (Button) findViewById(R.id.h_icon);
        layout = (LinearLayout) findViewById(R.id.layout);
        add_link=(Button)findViewById(R.id.add_link);

        activity = LendEditUserProfile.this;
        FileUpload.activity = LendEditUserProfile.this;

        profile_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    name = profile_name.getText().toString().trim();
                    //Toast.makeText(getApplicationContext(),"action done",Toast.LENGTH_LONG).show();
                    profilenameUpload();
                }
                return false;
            }
        });

        Intent i = getIntent();
        average_rating = i.getStringExtra("ratingValue");
        rating_value.setText(average_rating);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        paypal_login.setTypeface(tf);
        update.setTypeface(tf);
        photo_text.setTypeface(tf);
        terms_condition.setTypeface(tf);
        home.setTypeface(tf);
        rating_value.setTypeface(tf);
        add_link.setTypeface(tf);

        String fontPath2 = "fonts/cambriab.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        profile_name.setTypeface(tf2);


        session = new SessionManager(getApplicationContext());
        String registrationdet  = session.Readreg();

        if(i.getStringExtra("isfrom").equals("edit")) {

        }else if(i.getStringExtra("isfrom").equals("paypal")){
            PaypalCon.partnerReferralPrefillData(session.readReraalapilink(),session.ReadAccessToekn(),this);
        }

        try {

            JSONObject obj = new JSONObject(registrationdet);
            id = obj.getString("userId");
            address = obj.getString("address");
            city = obj.getString("city");
            state = obj.getString("state");
            zipcode = obj.getString("zipcode");

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }

        getProfileimage();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LendEditUserProfile.this, ChangeCurrentAddress.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
            }
        });

        paypal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccessToken();
            }
        });
        add_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appInstalledOrNot()) {
                    linkedlogin();
                }else{
                    LinkedInActivity.userid=id;
                    Toast.makeText(getApplicationContext(),"App not installed ",Toast.LENGTH_LONG).show();
                    Intent in_linkedin=new Intent(LendEditUserProfile.this,LinkedInActivity.class);
                    startActivity(in_linkedin);
                }

            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
               Intent i = new Intent(LendEditUserProfile.this, LendProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LendEditUserProfile.this, TermsAndConditions.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LendEditUserProfile.this, ChangeCurrentEmailAddress.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendEditUserProfile.this,ReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });
    }

    public void getProfileimage() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
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
                map.put(APP_KEY, value);
                map.put(KEY_USERID, id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+ map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved2(String jsonobject, int requesttype) {
        String status = null;

        profile_image = null;

        profilename = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success")) {
                profile_image = jResult.getString("profile_image");
                profilename = jResult.getString("profile_name");
                user_name=jResult.getString("username");
                employee_rating = jResult.getString("employee_rating");
                posted_notification = jResult.getString("notificationCountPosted");
                pending_notification = jResult.getString("notificationCountPending");
                active_notification = jResult.getString("notificationCountActive");
                jobhistory_notification = jResult.getString("notificationCountJobHistory");

                rating_value.setText(employee_rating);
                if (!profile_image.equals("") && !profile_image.equals("null")) {
                    photo_text.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.VISIBLE);
                    if(profile_image.contains("http://graph.facebook.com/"))
                    profile_image = profile_image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
                    Glide.with(LendEditUserProfile.this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
                }

                if(profilename.equals(""))
                {
                    profile_name.setText(user_name);
                }
                else
                {
                    profile_name.setText(profilename);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LendEditUserProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(LendEditUserProfile.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        getPhotoFromCamera();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {

                }
            }
        });
        builder.show();
    }

    public void getPhotoFromCamera() {

        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {
                cameraIntent();
            }
        }
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                //onSelectFromGalleryResult(data);
                Uri selectedImageUri = data.getData();
                CropImage.activity(selectedImageUri)
                        .start(this);

            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = addBorderToBitmap(photo, 10, Color.BLACK);
                photo = addBorderToBitmap(photo, 3, Color.BLACK);
                image.setImageBitmap(photo);
                photo_text.setVisibility(View.INVISIBLE);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                CropImage.activity(tempUri)
                        .start(this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    image.setImageURI(resultUri);
                    image.setVisibility(View.VISIBLE);
                    String selectedImagePath = uriToFilename(resultUri);
                    System.out.println("filename:gallery "+selectedImagePath);
                    new FileUpload(selectedImagePath,id);
                    System.out.println("path:camera:" + selectedImagePath);
                    filename = FileUpload.firstRemoteFile;
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }else {
                LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
                System.out.println("Request code "+requestCode);
                System.out.println("Data "+data);
            }
        } else {

        }
    }

    private String uriToFilename(Uri uri) {
        String path = null;

        if (Build.VERSION.SDK_INT < 11) {
            path = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            path = RealPathUtil.getRealPathFromURI_API11to18(this, uri);
        } else {
            path = RealPathUtil.getRealPathFromURI_API19(this, uri);
        }

        return path;
    }

    @Override
    public void OnResponseReceived(String s, String s2) {
        merchantid = s;
        UpdatePaypal();
    }

    public static class RealPathUtil {
        public static String getRealPathFromURI_API19(Context context, Uri uri) {
            Log.e("uri", uri.getPath());
            String filePath = "";
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                Log.e("wholeID", wholeID);
// Split at colon, use second item in the array
                String[] splits = wholeID.split(":");
                if (splits.length == 2) {
                    String id = splits[1];

                    String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else {
                filePath = uri.getPath();
            }
            return filePath;
        }

        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;
            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                finalbitmap = rotateImageIfRequired(bm, LendEditUserProfile.this, data.getData());
                finalbitmap = addBorderToBitmap(finalbitmap, 10, Color.BLACK);
                finalbitmap = addBorderToBitmap(finalbitmap, 3, Color.BLACK);
                image.setImageBitmap(finalbitmap);
                photo_text.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Timber.d("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void profilenameUpload() {
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
                            final Dialog dialog = new Dialog(LendEditUserProfile.this);
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
                                // if (status.equals("Account number already exists.")) {
                                // custom dialog
                                final Dialog dialog = new Dialog(LendEditUserProfile.this);
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
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APP_KEY, value);
                map.put(KEY_PROFILE_NAME, name);
                map.put(KEY_USERID, id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditUserProfile.activity);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if (status.equals("success")) {
                //Toast.makeText(getApplicationContext(),"Profile Name Uploaded Successfully",Toast.LENGTH_LONG).show();
            } else {

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
                Intent i = new Intent(getApplicationContext(), LendProfilePage.class);
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

    protected Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth*2, // Width
                srcBitmap.getHeight() + borderWidth*2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                borderWidth / 2,
                borderWidth / 2,
                canvas.getWidth() - borderWidth / 2,
                canvas.getHeight() - borderWidth / 2
        );
        canvas.drawRect(rect,paint);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }


    private String getAccessToken() {

        String Access_Token=PaypalCon.getAccessToken();
        String[] href = PaypalCon.partnerReferralPrefillAPI(Access_Token,LendEditUserProfile.this);
        session.saveAccesstoken(Access_Token);
        session.savePaypalRedirect("4");
        session.saveReraalapilink(href[0]);
        Intent myIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(href[1]));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myIntent,1);

        return null;
    }


    public  void UpdatePaypal(){
        dialog.show();

        String url= PAYPAL_UPDATEURL+APP_KEY+"="+value+"&"+KEY_USERID+"="+id+"&"+MERCHANTID+"="+merchantid+"&device=android";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        dialog.dismiss();
                        Log.d("Response", response.toString());
                        System.out.println("updatea merchantd" + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendEditUserProfile.this);
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
                                    final Dialog dialog = new Dialog(LendEditUserProfile.this);
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
                                }

                            } catch (JSONException e) {
                                //Handle a malformed json response
                                System.out.println("volley error ::" + e.getMessage());
                            } catch (UnsupportedEncodingException errors) {
                                System.out.println("volley error ::" + errors.getMessage());
                            }
                        }
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
    }


    public void linkedlogin() {

        LISessionManager.getInstance(getApplicationContext()).init(LendEditUserProfile.this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess () {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                getPersonelinfo();
            }

            @Override
            public void onAuthError (LIAuthError error){
                // Handle authentication errors
                System.out.println("Error "+error.toString());
            }
        },true);

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }

    public void getPersonelinfo(){
        //  String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email,picture-url,profile_url)";
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,picture-url,picture-urls::(original),positions,date-of-birth,phone-numbers,location)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                System.out.println("json res "+json);
                try{

                    lin_email=json.getString("emailAddress");
                    firstnmae=json.getString("firstName");
                    lastnmae=json.getString("lastName");
                    lin_id=json.getString("id");
                    String pictureUrl="";
                    if(json.has("pictureUrl")){
                        pictureUrl=json.getString("pictureUrl");
                    }
                    pictureurl=pictureUrl;
                    getpublicprofileurl();
                } catch (Exception e)
                {

                }
                System.out.println("Json "+json);
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });
    }

    public void getpublicprofileurl(){
        final String profilurl = "https://api.linkedin.com/v1/people/~:(public-profile-url)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, profilurl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                try{
                    String publicProfileUrl=json.getString("publicProfileUrl");
                    System.out.println("publicProfileUrl "+publicProfileUrl);
                    profileurl=publicProfileUrl;
                    UpdatelinkedingData();
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });

    }
    private boolean appInstalledOrNot() {
        Boolean appinstalled=false;
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            System.out.println("packanme "+packageInfo.packageName);
            if(packageInfo.packageName.equals("com.linkedin.android")) {
                appinstalled = true;
                System.out.println("App installed ");
                break;
            }
        }
        return appinstalled;
    }

    public void UpdatelinkedingData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LINKEDIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  onResponserecieved(response, 1);
                        System.out.println("Response "+response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendEditUserProfile.this);
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
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure while performing the request", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network error while performing the request", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                                if (status.equals("This User Currently Does Not Have Any Ratings")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LendEditUserProfile.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("This User Currently Does Not Have Any Ratings");
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
                                }
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
                params.put(FIRST_NAME, firstnmae);
                params.put(LAST_NAME, lastnmae);
                params.put(ID, lin_id);
                params.put(PROF_URL, profileurl);
                params.put(PIC_URL, pictureurl);
                params.put(EMAIL, lin_email);
                params.put(Constant.DEVICE, Constant.ANDROID);

                System.out.println("Params " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}