package com.gelistirmen.finance.data.remote.settings;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.settings.SettingsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsDao extends FMDao {
    public SettingsModel settingsModel;

    public SettingsDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.Settings, Request.Method.GET, true);
    }

    public SettingsDao(VolleyDaoListener listener, SettingsModel settingsModel) {
        super(listener, Constants.ApiMethod.Settings, Request.Method.PUT, true);
        this.settingsModel = settingsModel;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("allowNotification", this.settingsModel.allowNotification);
            jsonObject.put("language", this.settingsModel.language);
            super.bodyDataObject = jsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        if (super.method == Request.Method.GET)
            super.onAfterSuccessRequest(new SettingsModel((JSONObject) data));
        else if (super.method == Request.Method.PUT)
            super.onAfterSuccessRequest();
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.settingsModel();
    }
}
