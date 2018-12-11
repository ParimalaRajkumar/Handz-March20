package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.app.Config;
import com.google.firebase.messaging.FirebaseMessaging;
import com.splunk.mint.Mint;
import com.util.NotificationUtils;

import static java.security.AccessController.getContext;

public class SplashScreen extends Activity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Intent resultIntent = null;
    SessionManager session;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash_screen);
        session = new SessionManager(getApplicationContext());

        Profilevalues.android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Mint.initAndStartSession(this.getApplication(), "21afe9a2");

            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(3000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            };
            if(getIntent().getExtras() == null || (getIntent().getExtras()!=null && getIntent().getExtras().getString("google.message_id")==null ))
            {
                timerThread.start();
            } else
            {
                if(session.getLoginStatus()) {
                    String type = getIntent().getExtras().getString("type");
                    if (type!=null && type.equals("notificationCountMakePayment")) {
                        resultIntent = new Intent(getApplicationContext(), ActiveJobs.class);
                    } else if (type.equals("hirejob") || type.equals("refusejob")) {
                        resultIntent = new Intent(getApplicationContext(), LendProfilePage.class);
                    }else if(type.equals("applyjob")){
                        resultIntent = new Intent(getApplicationContext(), ProfilePage.class);
                    }else if(type.equals("jobcanceled")||type.equals("send_message")||type.equals("paymentcompleted")){
                        if(session.getLoginStatus())
                            resultIntent = new Intent(getApplicationContext(), ProfilePage.class);
                        else
                            resultIntent = new Intent(getApplicationContext(), LendProfilePage.class);
                    }else{
                        if(session.getLoginStatus())
                            resultIntent = new Intent(getApplicationContext(), ProfilePage.class);
                        else
                            resultIntent = new Intent(getApplicationContext(), LendProfilePage.class);
                    }

                }else{
                    resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                }

                startActivity(resultIntent);
            }

//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                String value = getIntent().getExtras().getString(key);
//                Log.d("mohanbabu notification", "Key: " + key + " Value: " + value);
//            }
//        }



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
        }

        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            super.onPause();
            finish();
        }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    private void displayFirebaseRegId() {
      /*  SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");*/
    }

    }