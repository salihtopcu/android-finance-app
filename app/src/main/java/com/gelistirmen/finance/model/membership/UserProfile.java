package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
    public String id;
    public String name;
    public String phoneNumber;
    public Company company;

    public UserProfile(String id, String name, String phoneNumber, Company company) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.company = company;
    }

    public UserProfile(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.name = Method.getString(jsonObject, "fullName");
            this.phoneNumber = Method.getString(jsonObject, "phone");
            this.company = new Company(Method.getJsonObject(jsonObject, "company"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
