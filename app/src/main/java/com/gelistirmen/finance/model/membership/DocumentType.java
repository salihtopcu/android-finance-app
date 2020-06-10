package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocumentType {
    public String id;
    public String name;
    public boolean mandatory;
    public int validityPeriod;

    public DocumentType(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            jsonObject = Method.getJsonObject(jsonObject, "documentType");
            this.name = Method.getString(jsonObject, "documentTypeName");
            this.mandatory = Method.getBoolean(jsonObject, "mandatory");
            this.validityPeriod = Method.getInt(jsonObject, "validityPeriod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class List extends ArrayList<DocumentType> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new DocumentType(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
