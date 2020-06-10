package com.gelistirmen.finance.model.operation;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class QuoteStatusResponse {

    public int status;
    public boolean isCancelledByDebtor;
    public boolean isCancelledByPayee;

    public QuoteStatusResponse(JSONObject jsonObject) {
        try {
            this.status = Method.getInt(jsonObject, "status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
