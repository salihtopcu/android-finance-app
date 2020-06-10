package com.gelistirmen.finance.model.operation;

import android.util.Log;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Notification {
    public String id;
    public String iconUrl;
    public String message;
    public Date createdDate;
    public boolean isNotified;

    public Notification(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.iconUrl = Method.getString(jsonObject, "iconUrl");
            this.message = Method.getString(jsonObject, "message");
            this.createdDate = Method.getDate(jsonObject, "createdDate", Constants.webServiceDateTimeFormat);
            this.isNotified = Method.getBoolean(jsonObject, "isNotified");
        } catch (JSONException e) {
            Log.e("Notification", "Json parse error.");
        }
    }

    public static class List extends ArrayList<Notification> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new Notification(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
