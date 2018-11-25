package com.example.iz_test.handzforhire;

import com.app.Config;

public class PayPalConfig {

    public static String PAYPAL_CLIENT_ID;
    public static String PAYPAL_SECRET_KEY;
    public static String PAYPAL_TOKEN_URL;
    public static String PAYPAL_ORDER_URL;
    public static String PAYPAL_CHECKOUT_URL;
    public static String PAYPAL_PARTNER_REFFERAL_URL;
    public static String PAYPAL_ENVIRONMENT;
    public static String PAYPAL_CUSTOMER_INFO_URL;
    public static String PAYPAL_MERCHANT_ID_URL;
    public static String PAYPAL_MERCHANT_STATUS_URL;
//Sandbox detail
//    public static final String PAYPAL_CLIENT_ID = "AZKGEIXjRP25L9gE8PZLI17F5BujtqTehLicuLknK1RUTmqErqBvIuJ84edzXOn5dOfNn67sTaUL3mgV";
    //   public static final String PAYPAL_SECRET_KEY="EGKKeic_hKniIdk2QgufeR9g6QKN-Fpkdb7_5etfqizNkewnRSoURacZMsAXp_K1U5StNNgGBEEBd53M";

   /* public static final String PAYPAL_LIVE_CLIENT_ID = "ATskwCRK75D2Cdz9c50u5dN-3sgLxTfEyEyTpQUVyrbBj7NpvB0nR488YwSivCZOyVwHSDvx_j2KmTq4";
    public static final String PAYPAL_LIVE_SECRET_KEY="ELYsVbRWIb1Uz0LiTGM5-mZUJibYd5UViSbixQaKY0LuKIFIsyU5OEONyEpfqG5ayWFMtQC_m2IdIMxv";*/

    //   public static final String PAYPAL_LIVE_CLIENT_ID = "AUJXB6Fw9-KIrL9Igu4ou05tf7mWydAqV8cHu-OPcWeZJfrUgwp0CXYo9-pG7GukKaM35WKGakqjoClj";
    //   public static final String PAYPAL_LIVE_SECRET_KEY="EHmI5Oa1F7OmG-z4YhzopbxkL7Ry4xcgjycG_cE5w44xghRlaSRGSZ8UMuxo6_5T5EX9mqgm5bnWYhBa";

    //   public static final String PAYPAL_URL="https://api.sandbox.paypal.com/v1/oauth2/token";
    //   public static final String PAYPAL_LIVE_URL="https://api.paypal.com/v1/oauth2/token";


    static void setBuild(Config.BUILD_TYPE buildType) {
        if (buildType == Config.BUILD_TYPE.DEVELOP) {
            PAYPAL_ENVIRONMENT="sandbox";
            PAYPAL_CLIENT_ID = "AZKGEIXjRP25L9gE8PZLI17F5BujtqTehLicuLknK1RUTmqErqBvIuJ84edzXOn5dOfNn67sTaUL3mgV";
            PAYPAL_SECRET_KEY = "EGKKeic_hKniIdk2QgufeR9g6QKN-Fpkdb7_5etfqizNkewnRSoURacZMsAXp_K1U5StNNgGBEEBd53M";
            PAYPAL_TOKEN_URL = "https://api.sandbox.paypal.com/v1/oauth2/token";
            PAYPAL_ORDER_URL=  "https://api.sandbox.paypal.com/v1/checkout/orders/";
           PAYPAL_CHECKOUT_URL="https://api.sandbox.paypal.com/v1/checkout/orders/%s/pay";
           PAYPAL_CUSTOMER_INFO_URL="https://api.sandbox.paypal.com/v1/identity/openidconnect/userinfo/?schema=openid";
           PAYPAL_PARTNER_REFFERAL_URL= "https://api.sandbox.paypal.com/v1/customer/partner-referrals/";
           PAYPAL_MERCHANT_ID_URL="https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations?tracking_id=";
            PAYPAL_MERCHANT_STATUS_URL= "https://api.sandbox.paypal.com/v1/customer/partners/2NPBRNULVL7GS/merchant-integrations/";
        } else if (buildType == Config.BUILD_TYPE.RELEASE) {
            PAYPAL_ENVIRONMENT="live";
            PAYPAL_TOKEN_URL = "https://api.paypal.com/v1/oauth2/token";
            PAYPAL_ORDER_URL ="https://api.paypal.com/v1/checkout/orders/";
            PAYPAL_CHECKOUT_URL="https://api.paypal.com/v1/checkout/orders/%s/pay";
            PAYPAL_CLIENT_ID = "AUJXB6Fw9-KIrL9Igu4ou05tf7mWydAqV8cHu-OPcWeZJfrUgwp0CXYo9-pG7GukKaM35WKGakqjoClj";
            PAYPAL_SECRET_KEY = "EHmI5Oa1F7OmG-z4YhzopbxkL7Ry4xcgjycG_cE5w44xghRlaSRGSZ8UMuxo6_5T5EX9mqgm5bnWYhBa";
            PAYPAL_PARTNER_REFFERAL_URL= "https://api.paypal.com/v1/customer/partner-referrals/";
            PAYPAL_CUSTOMER_INFO_URL="https://api.paypal.com/v1/identity/openidconnect/userinfo/?schema=openid";
            PAYPAL_MERCHANT_ID_URL="https://api.paypal.com/v1/customer/partners/QLMCC9XV8A6GS/merchant-integrations?tracking_id=";
            PAYPAL_MERCHANT_STATUS_URL="https://api.paypal.com/v1/customer/partners/QLMCC9XV8A6GS/merchant-integrations/";
        }

    }
}
