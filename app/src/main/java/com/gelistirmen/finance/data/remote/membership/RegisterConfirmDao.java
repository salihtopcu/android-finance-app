package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.FMDao;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterConfirmDao extends FMDao {

    public RegisterConfirmDao(VolleyDaoListener listener,String phoneNumber, String code) {
        super(listener, Constants.ApiMethod.RegisterConfirm, Request.Method.POST, false);
        super.acceptEmptyResponse = true;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", Cache.getUserId());
//            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("code", code);
            super.bodyDataObject = jsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }

    @Nullable
    @Override
    protected Error handleNoResponseError() {
        return null;
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
