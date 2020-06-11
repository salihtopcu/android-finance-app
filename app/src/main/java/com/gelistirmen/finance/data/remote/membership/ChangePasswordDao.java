package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.PasswordChangeAttempt;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordDao extends FMDao {

    public ChangePasswordDao(VolleyDaoListener listener, PasswordChangeAttempt passwordChangeAttempt) {
        super(listener, Constants.ApiMethod.ChangePassword, Request.Method.POST, true);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", passwordChangeAttempt.userId);
            jsonObject.put("currentPassword", passwordChangeAttempt.currentPassword);
            jsonObject.put("newPassword", passwordChangeAttempt.newPassword);
            jsonObject.put("confirmPassword", passwordChangeAttempt.confirmPassword);
            super.bodyDataObject = jsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return null;
    }
}
