package com.gelistirmen.finance.data.remote.membership;

import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.RegisterAttempt;
import com.gelistirmen.finance.model.membership.RegisterResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterDao extends FMDao {
    public RegisterAttempt registerAttempt;

    public RegisterDao(VolleyDaoListener listener, RegisterAttempt registerAttempt) {
        super(listener, Constants.ApiMethod.Customers, Request.Method.POST, false);
        this.registerAttempt = registerAttempt;
        try {
            JSONObject registerAttemptJsonObject = new JSONObject();
            registerAttemptJsonObject.put("fullName", registerAttempt.name);
            registerAttemptJsonObject.put("phone", registerAttempt.phoneNumber);
            registerAttemptJsonObject.put("companyTitle", registerAttempt.companyName);
            registerAttemptJsonObject.put("password", registerAttempt.password);
            registerAttemptJsonObject.put("confirmPassword", registerAttempt.passwordRepeat);
            registerAttemptJsonObject.put("userAggrement", registerAttempt.userAgreementAccepted);
            registerAttemptJsonObject.put("permissionPersonalData", registerAttempt.dataProtectionPermisionAccepted);

            JSONObject deviceJsonObject = new JSONObject();
            deviceJsonObject.put("manufacturer", registerAttempt.device.manufacturer);
            deviceJsonObject.put("model", registerAttempt.device.model);
            deviceJsonObject.put("os", registerAttempt.device.os);
            deviceJsonObject.put("osVersion", registerAttempt.device.osVersion);
            deviceJsonObject.put("token", registerAttempt.device.token);
            registerAttemptJsonObject.put("device", deviceJsonObject);

            super.bodyDataObject = registerAttemptJsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new RegisterResponse((JSONObject) data));
    }
}
