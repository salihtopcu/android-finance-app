package com.gelistirmen.finance.model.settings;

import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    public String id;
    public String name;

    public City(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.name = Method.getString(jsonObject, "name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class List extends ArrayList<City> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new City(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
