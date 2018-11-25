package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends Activity {

    Button need_hand,lend_hand;
    LinearLayout promo_video;
    public static final int RequestPermissionCode = 1;
    SessionManager session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        if(checkPermission()){
            //Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }
        else {

            requestPermission();
        }

        need_hand = (Button) findViewById(R.id.get_handz);
        lend_hand = (Button) findViewById(R.id.give_handz);
        promo_video = (LinearLayout) findViewById(R.id.promo_video);
        TextView text = (TextView) findViewById(R.id.text);
        TextView text1 = (TextView) findViewById(R.id.text1);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(tf);
        text1.setTypeface(tf);
        need_hand.setTypeface(tf);
        lend_hand.setTypeface(tf);

        ComputePackageHash();

        text.setText(Html.fromHtml("Handz is currently offered in the Jacksonville,FL \narea. Want Handz in your area?   <u>Click Here</u>"));

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.handzforhire.com/want-handz-in-your-area"));
                startActivity(browserIntent);
            }
        });

        need_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean loginstatus = session.getLoginStatus();
                Profilevalues.usertype="1";
                if(loginstatus == true)
                {
                    Intent i = new Intent(MainActivity.this, ProfilePage.class);
                    startActivity(i);
                    finish();
                    String a = "if_condition";
                    System.out.println("pppppppp:::::::"+a);
                }
                else
                {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                    String b = "else_condition";
                    System.out.println("pppppppp:::::::"+b);
                }
            }
        });

        lend_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean status = session.isLoggedIn();
                Profilevalues.usertype="2";
                System.out.println("ppppppppp:florist:" +status);
                if(status == true)
                {
                    Intent intent = new Intent(MainActivity.this,MapActivity.class);
                    startActivity(intent);
                    String a = "if_condition";
                    System.out.println("pppppppp:::::::"+a);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, LendLoginPage.class);
                    startActivity(i);
                    finish();
                    String b = "else_condition";
                    System.out.println("pppppppp:::::::"+b);
                }
            }
        });

        promo_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PromoVideo.class);
                startActivity(i);
            }
        });


      /*  LISessionManager.getInstance(getApplicationContext()).init(this, buildScope()//pass the build scope here
                , new Firebase.AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        // Authentication was successful. You can now do
                        // other calls with the SDK.
                        Toast.makeText(MainActivity.this, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        // Handle authentication errors
                        Log.e(TAG, "Auth Error :" + error.toString());
                        Toast.makeText(MainActivity.this, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }, true);*///if TRUE then it will show dialog if
        // any device has no LinkedIn app installed to download app else won't show anything

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                       //READ_PHONE_STATE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                   boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
/*
                    if (CameraPermission && ReadContactsPermission && ReadExternalStoragePermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }*/
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        //int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED ;
               //ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                //ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



   public void ComputePackageHash()
    {
        try
        {
        PackageInfo info=getPackageManager().getPackageInfo("com.example.iz_test.handzforhire",PackageManager.GET_SIGNATURES);
        for(Signature signature : info.signatures ){
            MessageDigest md=MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            System.out.println("keyHash  "+   Base64.encodeToString(md.digest(),Base64.DEFAULT));
        }
    }catch(Exception e)
    {

    }
   }
}
