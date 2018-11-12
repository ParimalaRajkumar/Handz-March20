package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by IZ-Parimala on 28-09-2018.
 */

public class PaypalReturnActivity extends Activity {
    SessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        session = new SessionManager(getApplicationContext());

        String paypalredirect=session.ReadPaypalRedirect();


        System.out.println("Paypal redirect Value "+paypalredirect);

        if(paypalredirect.equals("0"))
        {
            Intent in_reg=new Intent(PaypalReturnActivity.this,RegisterPage4.class);
            in_reg.putExtra("isfrom", "paypal");
            startActivity(in_reg);

        } else if(paypalredirect.equals("2"))
        {
            Intent in_reg=new Intent(PaypalReturnActivity.this,LendRegisterPage4.class);
            in_reg.putExtra("isfrom", "paypal");
            startActivity(in_reg);

        }
        else if(paypalredirect.equals("3"))
        {
            Intent in_reg=new Intent(PaypalReturnActivity.this,EditUserProfile.class);
            in_reg.putExtra("isfrom", "paypal");
            startActivity(in_reg);

        } else if(paypalredirect.equals("4"))
        {
            Intent in_reg=new Intent(PaypalReturnActivity.this,LendEditUserProfile.class);
            in_reg.putExtra("isfrom", "paypal");
            startActivity(in_reg);

        }else if(paypalredirect.equals("1")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,PayEmployee.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }else if(paypalredirect.equals("5")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,RegisterPage4.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }else if(paypalredirect.equals("6")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,LendRegisterPage4.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }else if(paypalredirect.equals("7")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,SummaryAdd.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }
        else if(paypalredirect.equals("8")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,SummarySubtract.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }
        else if(paypalredirect.equals("9")){

            Intent in_payment=new Intent(PaypalReturnActivity.this,ApplyJob.class);
            in_payment.putExtra("isfrom", "paypal");
            startActivity(in_payment);
        }
    }
}
