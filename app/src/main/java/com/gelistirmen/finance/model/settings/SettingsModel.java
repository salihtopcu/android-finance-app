package com.gelistirmen.finance.model.settings;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsModel {
    public Boolean allowNotification;
    public String language;

    public SettingsModel(Boolean allowNotification, String language) {
        this.allowNotification = allowNotification;
        this.language = language;
    }

    public SettingsModel(JSONObject jsonObject) {
        try {
            this.allowNotification = Method.getBoolean(jsonObject, "allowNotification");
            this.language = Method.getString(jsonObject, "language");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
