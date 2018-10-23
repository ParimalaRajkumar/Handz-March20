package com.example.iz_test.handzforhire;

/**
 * Created by IZ-Parimala on 26-12-2017.
 */

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        Map<String, String> param = new HashMap<String, String>();
        param.put("X-APP-KEY","HandzForHire@~");
        param.put("search_type", "location");
                   /* params.put("lat", String.valueOf(MainActivity.lat));
                    params.put("lon", String.valueOf(MainActivity.lon));*/
        param.put("lat", "1.937");
        param.put("lon", "80.1223");
        param.put("miles","5");

        this.params = param;
        System.out.println("CustomReq ");
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        System.out.println("Param "+params);
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            System.out.println("response "+response);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            System.out.println("response "+e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            System.out.println("response "+je.getMessage());
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
        System.out.println("response "+response);
    }
}