package com.gelistirmen.finance.data.remote.base;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.membership.RenewTokenDao;
import com.gelistirmen.finance.model.membership.RenewTokenResponse;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class FMDao extends VolleyDao implements VolleyDao.DataHandler {

    private boolean requiresToken;

    /**
     * @param listener
     * @param url      Full URL of request
     * @param method   com.android.volley.Request.Method.GET | POST | ...
     */
    public FMDao(VolleyDaoListener listener, String url, int method, boolean requiresToken) {
        super(Constants.webServiceBaseUrl + url, method, listener, "error_code", "error_message");
        this.requiresToken = requiresToken;
        this.setHeaders();
    }

    public FMDao(VolleyDaoListener listener, String url) {
        this(listener, url, Request.Method.GET, true);
    }

    private void setHeaders() {
        if (requiresToken)
            super.headers.put("Authorization", "Bearer " + Cache.getAccessToken());
    }

//    protected void setBodyData(String type, JSONObject attributes) throws JSONException {
//        super.bodyDataObject = new JSONObject();
//        JSONObject dataJsonObject = new JSONObject();
//        dataJsonObject.put("type", type);
//        dataJsonObject.put("attributes", attributes);
//        super.bodyDataObject.put("data", dataJsonObject);
//        super.dataHandler = this;
//    }

    @Override
    public Object handleJsonObjectData(JSONObject data) {
        try {
            JSONObject dataObject = data.getJSONObject("data");
            Object attributesObject = dataObject.get("attributes");
            if (attributesObject instanceof JSONObject || attributesObject instanceof JSONArray)
                return attributesObject;
            else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    protected Error handleDataError(JSONObject jsonObject) {
        try {
            int statusCode = Method.getInt(jsonObject, "statusCode");
            if (statusCode >= 300) {
                String errorMessage = Method.getString(jsonObject, "detail");
                return new Error(ErrorType.getWithCode(statusCode), errorMessage);
            } else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Error getError(VolleyError volleyError) {
        String dataString = (volleyError.networkResponse == null || volleyError.networkResponse.data == null) ? null : new String(volleyError.networkResponse.data);
        if (Method.isNullOrEmpty(dataString))
            return super.getError(volleyError);
        else {
            dataString = dataString.replace("{", "");
            dataString = dataString.replace("}", "");
            dataString = dataString.replace("[", "");
            dataString = dataString.replace("]", "");
            dataString = dataString.replace("\"", "");
            dataString = dataString.replace("\r", "");
            dataString = dataString.replace("\n", " ");
            dataString = dataString.replace("   ", " ");
            return new Error(ErrorType.getWithCode(volleyError.networkResponse.statusCode), dataString);
        }
    }

    @Override
    protected void onAfterFailedRequest(Error error) {
        if (error.type == ErrorType.InvalidToken && !Cache.getRefreshToken().isEmpty() && !(this instanceof RenewTokenDao)) {
            new RenewTokenDao(new VolleyDaoListener() {
                @Override
                public void daoDidSuccess(VolleyDao dao) { }

                @Override
                public void daoDidSuccess(VolleyDao dao, Object data) {
                    RenewTokenResponse renewTokenResponse = (RenewTokenResponse) data;
                    Cache.setAccessToken(renewTokenResponse.token);
                    Cache.setRefreshToken(renewTokenResponse.refreshToken);
                    FMDao.this.setHeaders();
                    FMDao.this.execute();
                }

                @Override
                public void daoDidFail(VolleyDao dao, Error error) {
                    Cache.setAccessToken("");
                    Cache.setRefreshToken("");
                    FMDao.super.onAfterFailedRequest(new Error(ErrorType.InvalidToken, ""));
                }
            }).execute();
        } else
            super.onAfterFailedRequest(error);
    }
}
