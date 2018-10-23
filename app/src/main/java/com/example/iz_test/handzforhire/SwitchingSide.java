package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SwitchingSide extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    private static final String URL = Constant.SERVER_URL+"logout";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String KEY_USERID = "user_id";
    String user_id = "70";
    SessionManager session;
    LinearLayout promo;
    TextView need_txt,lend_txt;
    Button need_hand;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switching_side);

        session = new SessionManager(getApplicationContext());
        promo = (LinearLayout) findViewById(R.id.promo_video);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        Button lend_hand = (Button) findViewById(R.id.lend_hand);
        final TextView signOut = (TextView) findViewById(R.id.sign_out);
        lend_txt=(TextView)findViewById(R.id.lend_txt);
        need_txt=(TextView)findViewById(R.id.need_txt);
        need_hand=(Button)findViewById(R.id.need_hand);

        System.out.println("ssssssssssss:switchside::::"+Profilevalues.usertype);

        if(Profilevalues.usertype != null && Profilevalues.usertype.equals("1")){
            lend_txt.setVisibility(View.VISIBLE);
            lend_hand.setVisibility(View.VISIBLE);
            need_txt.setVisibility(View.GONE);
            need_hand.setVisibility(View.GONE);
        }
        if(Profilevalues.usertype != null && Profilevalues.usertype.equals("2")){
            lend_txt.setVisibility(View.GONE);
            lend_hand.setVisibility(View.GONE);
            need_txt.setVisibility(View.VISIBLE);
            need_hand.setVisibility(View.VISIBLE);
        }

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,PromoVideo.class);
                startActivity(i);
            }
        });

        lend_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,MapActivity.class);
                Profilevalues.usertype="2";
                startActivity(i);
            }
        });
        need_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,ProfilePage.class);
                Profilevalues.usertype="1";
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,LendProfilePage.class);
                startActivity(i);
                finish();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // session.logoutUser();
                signoutmethod();
            }
        });

        detector = new SimpleGestureFilter(this,this);
    }

    private void signoutmethod() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:signout::" + response);
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
                map.put(XAPP_KEY, value);
                map.put(KEY_USERID, user_id);
                map.put(Constant.DEVICE, Constant.ANDROID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String categories = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if(status.equals("success"))
            {
                session.logoutUser();
                Intent intent = new Intent(SwitchingSide.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
                finish();
                break;
           /* case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                Intent j = new Intent(getApplicationContext(), SwitchingSide.class);
                startActivity(j);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;*/

        }
        //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "doutap", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
