package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class LinkPaypal extends Activity {

    ImageView img_paypal;
    SessionManager session;
    TextView txt_skip;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_paypal);

        img_paypal=(ImageView)findViewById(R.id.img_paypal);
        txt_skip=(TextView)findViewById(R.id.txt_skip);

        session = new SessionManager(getApplicationContext());

        img_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccessToken();
            }
        });
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paypalredirect=session.ReadPaypalRedirect();
                if(paypalredirect.equals("0")) {
                    Intent in_pay = new Intent(LinkPaypal.this, RegisterPage4.class);
                    in_pay.putExtra("isfrom", "reg");
                    startActivity(in_pay);
                }else if(paypalredirect.equals("5")) {
                    Intent in_pay = new Intent(LinkPaypal.this, RegisterPage4.class);
                    in_pay.putExtra("isfrom", "reg");
                    startActivity(in_pay);
                }else if(paypalredirect.equals("2")){
                    Intent in_reg=new Intent(LinkPaypal.this,LendRegisterPage4.class);
                    in_reg.putExtra("isfrom", "reg");
                    startActivity(in_reg);
                }else if(paypalredirect.equals("6")){
                    Intent in_reg=new Intent(LinkPaypal.this,LendRegisterPage4.class);
                    in_reg.putExtra("isfrom", "reg");
                    startActivity(in_reg);
                }
            }
        });
    }



    private String getAccessToken() {

        String Access_Token=PaypalCon.getAccessToken();
        String[] href = PaypalCon.partnerReferralPrefillAPI(Access_Token,LinkPaypal.this);
        session.saveAccesstoken(Access_Token);
        session.saveReraalapilink(href[0]);
        Intent myIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(href[1]));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myIntent,1);

        return null;
    }
}
