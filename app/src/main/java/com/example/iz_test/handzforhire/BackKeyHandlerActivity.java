package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class BackKeyHandlerActivity extends Activity {

    Intent intent ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(intent.getStringExtra("isfrom") !=null && intent.getStringExtra("isfrom").equals("paypal")) {
            if (this instanceof ApplyJob) {
                Intent i = new Intent(this, LendProfilePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            } else {
                Intent i = new Intent(this, ProfilePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

    }
}
