package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*Hi for testing*/
public class ChatNeed extends Activity implements SimpleGestureFilter.SimpleGestureListener {


    private static final String SENDMSG_URL = Constant.SERVER_URL+"send_message";

    LinearLayout layout,lin_layoutmsg;
    RelativeLayout layout2;
    ImageView sendButton,close_btn;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String get_user,job_id,channel_id,user_id,user_type,child_id,key,receiverid,messagetype,usertype;
    public static String sender_id;
    TextView Tv,txt_sendmsg;
    SessionManager session;
    String current_user_id = "OGO6K8nyqKVJ8WQoE02WT5qFc1S2";

    //To upload images
    FirebaseStorage storage;
    public static StorageReference storageReference;
    //Live
     public static String storepath="gs://handz-8ac86.appspot.com";
    //dev
   // public static String storepath="gs://handzdev-9e758.appspot.com";

    ListView messagesContainer;
    ArrayList<ChatItems> messagelist;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    ChatAdapter adapter;
    String photourl="";
    Dialog dialog;

    public static String APP_KEY = "X-APP-KEY";
    public static String SENDER_ID = "sender_id";
    public static String MSG_TYPE = "message_type";
    public static String RECEIVER_ID = "receiver_id";
    public static String USER_TYPE = "user_type";
    public static String MSG = "message";
    public static String JOB_ID = "job_id";

    String value = "HandzForHire@~";
    int timeout = 60000;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        lin_layoutmsg=(LinearLayout)findViewById(R.id.lin_layoutmsg);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        close_btn = (ImageView) findViewById(R.id.img1);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Tv = (TextView) findViewById(R.id.txt1);
        txt_sendmsg=(TextView)findViewById(R.id.txt_sendmsg);
        messagesContainer=(ListView)findViewById(R.id.messagesContainer);
        messagesContainer.setStackFromBottom(true);

        messagelist=new ArrayList<ChatItems>();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(SessionManager.USER_TYPE);

        detector = new SimpleGestureFilter(this,this);
        dialog = new Dialog(ChatNeed.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        channel_id = i.getStringExtra("channel");
        get_user = i.getStringExtra("username");
        user_id = i.getStringExtra("userId");
        messagetype= i.getStringExtra("message_type");
        usertype= i.getStringExtra("user_type");
        receiverid=i.getStringExtra("receiverid");
        Tv.setText(get_user);
        sender_id = current_user_id + user_id;
        child_id = channel_id + job_id;
        Firebase.setAndroidContext(this);
       reference1 = new Firebase("https://handz-8ac86.firebaseio.com/channels");
       //reference1 = new Firebase("https://handzdev-9e758.firebaseio.com/channels");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(storepath);

        //reference2 = new Firebase("https://chatapp-85e06.firebaseio.com/Channels_lend/" + Chat_Need.channel_id + Chat_Need.job_id).child("Messages");

        txt_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("senderId", sender_id);
                    map.put("senderName", get_user);
                    map.put("text", messageText);
                    reference1.child(child_id).child("messages").push().setValue(map);
                   // addMessageBox(messageText,sender_id);
                    messageArea.setText("");

                    ChatItems item=new ChatItems();
                    item.setSenderId(sender_id);
                    item.setMessage(messageText);
                    item.setSenderName(get_user);
                    item.setPhotoURL("");
                    item.setHas_Attachemnt(true);
                    item.setHas_Attachemnt(false);
                    if(messagelist.size()==0){
                        messagelist.add(item);
                        adapter = new ChatAdapter(ChatNeed.this, messagelist);
                        messagesContainer.setAdapter(adapter);
                    }else {
                        displayMessage(item);
                    }
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog.show();
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messagelist.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    key = messageSnapshot.getKey();
                    if (key.equals(child_id)) {
                        for (DataSnapshot recipient : messageSnapshot.child("messages").getChildren()) {

                            ChatItems item=new ChatItems();



                            if(sender_id.equals(recipient.child("senderId").getValue()))
                            {
                                item.setMe(true);
                            }else{
                                item.setMe(false);
                            }

                            item.setSenderId(String.valueOf(recipient.child("senderId").getValue()));
                            item.setSenderName(String.valueOf(recipient.child("senderName").getValue()));
                            if(recipient.hasChild("photoURL")) {
                                String photoURL = String.valueOf(recipient.child("photoURL").getValue());
                                item.setMessage("");
                                item.setPhotoURL(photoURL);
                                item.setHas_Attachemnt(true);
                                messagelist.add(item);
                            }else if(recipient.hasChild("text")){

                                String text = String.valueOf(recipient.child("text").getValue());
                                if(!text.equals("null")) {
                                    item.setMessage(text);
                                    item.setPhotoURL("");
                                    item.setHas_Attachemnt(false);
                                    messagelist.add(item);
                                }
                            }


                        }
                    }
                }
                if(messagelist.size()>0) {
                    adapter = new ChatAdapter(ChatNeed.this, messagelist);
                    messagesContainer.setAdapter(adapter);
                }
                dialog.dismiss();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                dialog.dismiss();
            }

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //  imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(ChatItems message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
        sendmsg(message.getMessage());
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            dialog.show();
            String randomno=UUID.randomUUID().toString();
            final String photourl=storepath+"/"+child_id+"/"+randomno+".jpg";
            StorageReference ref = storageReference.child(child_id+"/"+randomno+".jpg");

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("senderId", sender_id);
                            map.put("senderName", get_user);
                            map.put("photoURL", photourl);
                            reference1.child(child_id).child("messages").push().setValue(map);

                            ChatItems item=new ChatItems();
                            item.setSenderId(sender_id);
                            item.setMessage("");
                            item.setSenderName(get_user);
                            item.setPhotoURL(photourl);
                            item.setHas_Attachemnt(true);

                            if(messagelist.size()==0){
                                messagelist.add(item);
                                adapter = new ChatAdapter(ChatNeed.this, messagelist);
                                messagesContainer.setAdapter(adapter);

                            }else {
                                displayMessage(item);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ChatNeed.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                        }
                    });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }


    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                Intent j = new Intent(ChatNeed.this, SwitchingSide.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                Intent i = new Intent(ChatNeed.this, ProfilePage.class);
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

    public void sendmsg( final String msg)
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SENDMSG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        System.out.println("response "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(ChatNeed.this);
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
                            //    if (status.equals("You are not allowed to apply for the job")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(ChatNeed.this);
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
                              //  }
                            } catch (JSONException e) {

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(APP_KEY, value);
                params.put(JOB_ID, job_id);
                params.put(USER_TYPE, usertype);
                params.put(SENDER_ID,user_id);
                params.put(RECEIVER_ID, receiverid);
                params.put(MSG_TYPE, messagetype);
                params.put(MSG, msg);
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("params "+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
