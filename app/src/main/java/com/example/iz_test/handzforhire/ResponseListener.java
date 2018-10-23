package com.example.iz_test.handzforhire;

import org.json.JSONObject;

public interface ResponseListener {
    public void onResponseReceived(JSONObject responseObj, int requestType);
}

