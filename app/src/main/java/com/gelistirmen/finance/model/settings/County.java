package com.gelistirmen.finance.model.settings;

import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class County {
    public String id;
    public String name;
    public City city;

    public County(String id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public County(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            JSONObject countyJsonObject = Method.getJsonObject(jsonObject, "county");
            this.name = Method.getString(countyJsonObject, "name");
            this.city = Method.exists(countyJsonObject, "city") ? new City(Method.getJsonObject(countyJsonObject, "city")) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class List extends ArrayList<County> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new County(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
