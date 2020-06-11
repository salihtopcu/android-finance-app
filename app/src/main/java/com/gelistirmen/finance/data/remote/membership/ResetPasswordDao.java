package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordDao extends FMDao {

    public ResetPasswordDao(VolleyDaoListener listener, String phoneNumber) {
        super(listener, Constants.ApiMethod.ResetPassword + "/" + phoneNumber, Request.Method.POST, false);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        try {
            super.onAfterSuccessRequest(Method.getString((JSONObject) data, "message"));
        } catch (JSONException e) {
            e.printStackTrace();
            super.onAfterFailedRequest(new Error(ErrorType.Client_ParseError, "Reset password parse error."));
        }
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return "Reset password mail has been sent.";
    }
}
