package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.FMDao;

import org.json.JSONException;
import org.json.JSONObject;

public class ResendVerificationDao extends FMDao {

    public ResendVerificationDao(VolleyDaoListener listener, String phoneNumber) {
        super(listener, Constants.ApiMethod.ResendVerification, Request.Method.POST, false);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", Cache.getUserId());
            jsonObject.put("phoneNumber", phoneNumber);
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
