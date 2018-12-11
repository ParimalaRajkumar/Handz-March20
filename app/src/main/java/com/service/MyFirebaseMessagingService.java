package com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.app.Config;
import com.example.iz_test.handzforhire.ActiveJobs;
import com.example.iz_test.handzforhire.LendProfilePage;
import com.example.iz_test.handzforhire.ProfilePage;
import com.example.iz_test.handzforhire.Profilevalues;
import com.example.iz_test.handzforhire.R;
import com.example.iz_test.handzforhire.SessionManager;
import com.example.iz_test.handzforhire.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    SessionManager session;
    Intent resultIntent;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        session = new SessionManager(getApplicationContext());

        System.out.println("message type "+remoteMessage.getData());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            System.out.println("before try");
            try {
                System.out.println("on try");

                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                System.out.println("on try 2");
                handleDataMessage(object);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
                System.out.println("Error "+ e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        System.out.println("on handle message");

        try {
           // JSONObject data = json.getJSONObject("data");

            String title = json.getString("user_id");
            String message = json.getString("message");
            String type = json.getString("type");
            String timestamp =String.valueOf(System.currentTimeMillis());
             Boolean loginstatus = session.getLoginStatus();
            System.out.println("Type "+type);


            if(loginstatus==false)
                loginstatus = session.isLoggedIn();

            System.out.println("");
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }

            //else {
                // app is in background, show the notification in notification tray
                if(loginstatus==true) {
                    if (type.equals("notificationCountMakePayment")) {
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
                      resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
                }
                resultIntent.putExtra("message", message);
            resultIntent.putExtra("userId", title);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
         //   }
        } catch (JSONException e) {
            System.out.println("json exception "+e.getMessage());
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("exception "+e.getMessage());
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
      /*  notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);*/

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);

        System.out.println("I ntent "+intent);

       // Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
