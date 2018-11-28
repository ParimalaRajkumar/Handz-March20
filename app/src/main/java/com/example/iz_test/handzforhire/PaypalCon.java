package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.listeners.ApiResponseListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IZ-Parimala on 24-09-2018.
 */

public class PaypalCon {


     public static SessionManager sessionmanager;
    static String[] hreflink;

    public PaypalCon(Context cntxt){

        sessionmanager =new SessionManager(cntxt);
    }

    public static String getAccessToken() {

        String access_token="";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(PayPalConfig.PAYPAL_TOKEN_URL);

        try {
            //String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
            String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
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
            Log.d("Response", responseContent );
            try {
                JSONObject obj = new JSONObject(responseContent);
                access_token=obj.getString("access_token");
               // partnerReferralPrefillAPI(obj.getString("access_token"));
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
        return access_token;
    }



    public static String OrderAPI(String accesstoken,String date,String orderapibody) {

        String returnulr="";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(PayPalConfig.PAYPAL_ORDER_URL);
        try {
            httppost.addHeader("Accept","application/json");
            httppost.addHeader("Accept-Language", "en_US");
            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);
            httppost.addHeader("PayPal-Partner-Attribution-Id", "HandzForHire_SP_PPM");
            httppost.addHeader("PayPal-Request-Id", date);
            httppost.addHeader("Paypal-Client-Metadata-Id", PayPalConfig.PAYPAL_CLIENT_ID);
            StringEntity se=  new StringEntity(orderapibody);
            httppost.setEntity(se);

     // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("response content "+responseContent);
            returnulr=responseContent;
            Log.d("Response", responseContent );
           


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return returnulr;
    }


    public static String payOrderAPI(String accesstoken,String date,String orderid) {

        String payorderapi="";

       // String ur="https://api.paypal.com/v1/checkout/orders/"+orderid+"/pay";

       // ur = String.format(PayPalConfig.PAYPAL_ORDER_URL,orderid);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost( String.format(PayPalConfig.PAYPAL_CHECKOUT_URL,orderid));
        try {

            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);
            httppost.addHeader("PayPal-Partner-Attribution-Id", "HandzForHire_SP_PPM");
            httppost.addHeader("PayPal-Request-Id", date);
            httppost.addHeader("Paypal-Client-Metadata-Id", PayPalConfig.PAYPAL_CLIENT_ID);

            JSONObject  objj= new JSONObject();
            try {
                objj.put("disbursement_mode", "INSTANT");
            }catch (Exception e){
                System.out.println("Exception "+e.getMessage());
            }

            StringEntity se=  new StringEntity(objj.toString());
            httppost.setEntity(se);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            payorderapi= EntityUtils.toString(response.getEntity());
            System.out.println("response content orderid pay "+payorderapi);
           // Log.d("Response", responseContent );
        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return payorderapi;
    }

    public static String orderstatusapi(String accesstoken,String oredestatusapi) {

        String payorderapi="";

       // String ur="https://api.sandbox.paypal.com/v1/checkout/orders/"+orderid+"/pay";

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(oredestatusapi);

        try {

            httpget.addHeader("content-type", "application/json");
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("Accept", "application/json");
            httpget.addHeader("Accept-Language", "en_US");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            payorderapi= EntityUtils.toString(response.getEntity());
            System.out.println("response orderstatus api"+payorderapi);
            // Log.d("Response", responseContent );
        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return payorderapi;
    }



    public static String OrderReqjson(String merchntid,String total,String referenceid){

        JSONObject finlobj =new JSONObject();
        JSONObject obj = new JSONObject();
        try {
            //Amount
            JSONObject obj1 = new JSONObject();
            obj1.put("subtotal", total);

            JSONObject amt = new JSONObject();
            amt.put("currency","USD");
            amt.put("details",obj1);
            amt.put("total",total);

            //Application context
            JSONObject appcntxt = new JSONObject();
            appcntxt.put("shipping_preference","NO_SHIPPING");
            appcntxt.put("user_action","commit");

            //Payee details
            JSONObject payee = new JSONObject();
            payee.put("merchant_id",merchntid);

            //partner_fee_details
            JSONObject email = new JSONObject();
            email.put("email","jay@handzforhire.com");

            JSONObject amnt = new JSONObject();
            amnt.put("value","1.00");
            amnt.put("currency","USD");

            JSONObject partner_fee_details = new JSONObject();
            partner_fee_details.put("receiver",email);
            partner_fee_details.put("amount",amnt);

            //purchase_units
            obj.put("reference_id", "HandzForHire_"+referenceid);
            obj.put("description", "Handz Job Payment");
            obj.put("amount", amt);
            obj.put("payee", payee);
            obj.put("partner_fee_details", partner_fee_details);

            JSONArray purchase_units =new JSONArray();
            purchase_units.put(obj);


            //redirect_urls
            JSONObject urls = new JSONObject();
            urls.put("return_url", "https://handzadmin.com");
            urls.put("cancel_url", "https://handzadmin.com");


            finlobj.put("purchase_units",purchase_units);
            finlobj.put("application_context", appcntxt);
            finlobj.put("redirect_urls",urls);
          //  System.out.println("Json format "+finlobj);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return finlobj.toString();
    }



    public static String[] partnerReferralPrefillAPI(String  accesstoken, Context mcontext) {

        String link="";
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("https://api.paypal.com/v1/oauth2/token");
        HttpPost httppost = new HttpPost(PayPalConfig.PAYPAL_PARTNER_REFFERAL_URL);

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

                link=hreflink[1];

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
        return hreflink;
    }


    public static void partnerReferralPrefillData(final String  hreflink, final String accesstoken , final ApiResponseListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String merchantid="";
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
                            merchantid=getMerchantIdOfSeller(trackiungvalue,accesstoken);
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
                final String result = merchantid;
                ((Activity)listener).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnResponseReceived(result, null);
                    }
                });

            }
        }).start();
    }

    public static String getMerchantIdOfSeller(String  trackvalue,String accesstoken) {

        String Merchantid="";

        HttpClient httpclient = new DefaultHttpClient();
       // HttpGet httpget = new HttpGet("https://api.paypal.com/v1/customer/partners/QLMCC9XV8A6GS/merchant-integrations?tracking_id="+trackvalue);
        HttpGet httpget = new HttpGet(PayPalConfig.PAYPAL_MERCHANT_ID_URL+trackvalue);

        try {
            httpget.addHeader("Authorization", "Bearer " + accesstoken);
            httpget.addHeader("content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response MerchantIdOfSeller"+responseContent);
            try {
                JSONObject obj = new JSONObject(responseContent);
                Merchantid=obj.getString("merchant_id");
                System.out.println("merchant id "+Merchantid);
                getMerchantStatus(Merchantid,accesstoken);
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
        return Merchantid;
    }

    public static String getMerchantStatus(String  merchant_id,String accesstoken) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(PayPalConfig.PAYPAL_MERCHANT_STATUS_URL+merchant_id);

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

    public static String readFromFile(Context context) {

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
