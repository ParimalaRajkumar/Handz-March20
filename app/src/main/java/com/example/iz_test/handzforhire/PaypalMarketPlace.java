package com.example.iz_test.handzforhire;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IZ-Parimala on 10-08-2018.
 */

public class PaypalMarketPlace {

    static String[] hreflink;

   static Context mcontext;

    public static String getAccessToken() {

        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("https://api.paypal.com/v1/oauth2/token");
        HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/oauth2/token");

        try {
            String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
            // String text=PayPalConfig.PAYPAL_LIVE_CLIENT_ID":"+PayPalConfig.PAYPAL_LIVE_SECRET_KEY;
            byte[] data = text.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            httppost.addHeader("Accept","application/json");
            httppost.addHeader("Accept-Language", "en_US");
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.addHeader("Authorization", "Basic " + base64);

            StringEntity se=new StringEntity("grant_type=client_credentials&client_id="+PayPalConfig.PAYPAL_CLIENT_ID+"&client_secret="+PayPalConfig.PAYPAL_SECRET_KEY);

            httppost.setEntity(se);

// Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("response content "+responseContent);
            Log.d("Response", responseContent );
            try {
                JSONObject obj = new JSONObject(responseContent);
                System.out.println(obj.getString("access_token"));
                partnerReferralPrefillAPI(obj.getString("access_token"));
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }


    private static String partnerReferralPrefillAPI(String  accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("https://api.paypal.com/v1/oauth2/token");
        HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/customer/partner-referrals/");

        try {
            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);

            StringEntity se=  new StringEntity(readFromFile(mcontext));

            // StringEntity se=new StringEntity();
            httppost.setEntity(se);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response partner-referrals "+responseContent);

            try {
                JSONObject obj = new JSONObject(responseContent);
                JSONArray array= obj.getJSONArray("links");
                hreflink=new String[array.length()];
                for(int i=0;i<array.length();i++){
                    JSONObject obj1=array.getJSONObject(i);
                    hreflink[i]=obj1.getString("href");
                }
                partnerReferralPrefillData(hreflink[0],accesstoken);
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private static String partnerReferralPrefillData(String  hreflink,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(hreflink);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response partner-referrals Data"+responseContent);
            try {
                JSONObject obj = new JSONObject(responseContent);
                JSONObject obj1=obj.getJSONObject("referral_data");
                JSONObject obj2=obj1.getJSONObject("customer_data");
                JSONArray array=obj2.getJSONArray("partner_specific_identifiers");
                for(int i=0;i<array.length();i++){
                    JSONObject obj3=array.getJSONObject(i);
                    String trackiungvalue=obj3.getString("value");
                    System.out.println("tracking Value "+trackiungvalue);
                    getMerchantIdOfSeller(trackiungvalue,accesstoken);
                }


            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private static String getMerchantIdOfSeller(String  trackvalue,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations?tracking_id="+trackvalue);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response MerchantIdOfSeller"+responseContent);
            try {
                JSONObject obj = new JSONObject(responseContent);
                String merchant_id=obj.getString("merchant_id");
                System.out.println("merchant id "+merchant_id);
                getMerchantStatus(merchant_id,accesstoken);
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private static String getMerchantStatus(String  merchant_id,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations/"+merchant_id);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response getMerchantStatus"+responseContent);



        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;
    }

    private static String readFromFile(Context context) {

        String ret = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("PartnerReferralPrefilled.json")));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                stringBuilder.append(mLine);
            }

            ret = stringBuilder.toString();
            System.out.println("file "+ret);
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return ret;
    }
}
