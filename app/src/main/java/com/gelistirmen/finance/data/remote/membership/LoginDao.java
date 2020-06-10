package com.gelistirmen.finance.data.remote.membership;

import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.LoginAttempt;
import com.gelistirmen.finance.model.membership.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginDao extends FMDao {
    public LoginAttempt loginAttempt;

    public LoginDao(VolleyDaoListener listener, LoginAttempt loginAttempt) {
        super(listener, Constants.ApiMethod.Login, Request.Method.POST, false);
        this.loginAttempt = loginAttempt;
        try {
            JSONObject loginAttemptJsonObject = new JSONObject();
            loginAttemptJsonObject.put("phone", loginAttempt.phoneNumber);
            loginAttemptJsonObject.put("password", loginAttempt.password);

            JSONObject deviceJsonObject = new JSONObject();
            deviceJsonObject.put("manufacturer", loginAttempt.device.manufacturer);
            deviceJsonObject.put("model", loginAttempt.device.model);
            deviceJsonObject.put("os", loginAttempt.device.os);
            deviceJsonObject.put("osVersion", loginAttempt.device.osVersion);
            deviceJsonObject.put("token", loginAttempt.device.token);
            loginAttemptJsonObject.put("device", deviceJsonObject);

            super.bodyDataObject = loginAttemptJsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new LoginResponse((JSONObject) data));
    }
}
