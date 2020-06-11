package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse {
    public String userId;
    public String token;
    public String refreshToken;
    public boolean changePasswordRequest;

    public LoginResponse(String userId, String token, String refreshToken, boolean changePasswordRequest) {
        this.userId = userId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.changePasswordRequest = changePasswordRequest;
    }

    public LoginResponse(JSONObject jsonObject) {
        try {
            this.userId = Method.getString(jsonObject, "id");
            this.token = Method.getString(jsonObject, "token");
            this.refreshToken = Method.getString(jsonObject, "refreshToken");
            this.changePasswordRequest = Method.getBoolean(jsonObject, "changePasswordRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
