package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterResponse {
    public String userId;
    public boolean changePasswordRequest;

    public RegisterResponse(String userId, boolean changePasswordRequest) {
        this.userId = userId;
        this.changePasswordRequest = changePasswordRequest;
    }

    public RegisterResponse(JSONObject jsonObject) {
        try {
            this.userId = Method.getString(jsonObject, "id");
            this.changePasswordRequest = Method.getBoolean(jsonObject, "changePasswordRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
