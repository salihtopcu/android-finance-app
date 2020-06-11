package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class RenewTokenResponse {
    public String token;
    public String refreshToken;

    public RenewTokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public RenewTokenResponse(JSONObject jsonObject) {
        try {
            this.token = Method.getString(jsonObject, "token");
            this.refreshToken = Method.getString(jsonObject, "refreshToken");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
