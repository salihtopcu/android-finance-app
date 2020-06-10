package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.model.settings.County;
import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class Company {
    public String id;
    public String name;
    public String phoneNumber;
    public String email;
    public String address;
    public String postalCode;
    public County county;
    public String taxNo;
    public String taxOffice;
    public String webSite;
    public BankAccount.List bankAccounts;

    public Company(String id, String name, String phoneNumber, String email, String address, String postalCode, County county, String taxNo, String taxOffice, String webSite, BankAccount.List bankAccounts) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this. address = address;
        this.postalCode = postalCode;
        this.county = county;
        this.taxNo = taxNo;
        this.taxOffice = taxOffice;
        this.webSite = webSite;
        this.bankAccounts = bankAccounts;
    }

    public Company(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            if (Method.exists(jsonObject, "companyProfile"))
                jsonObject = Method.getJsonObject(jsonObject, "companyProfile");
            this.name = Method.getString(jsonObject, "companyName");
            this.phoneNumber = Method.getString(jsonObject, "phone");
            this.email = Method.getString(jsonObject, "email");
            this.address = Method.getString(jsonObject, "address");
            this.postalCode = Method.getString(jsonObject, "postalCode");
            this.county = Method.exists(jsonObject, "county") ? new County(Method.getJsonObject(jsonObject, "county")) : null;
            this.taxNo = Method.getString(jsonObject, "taxNo");
            this.taxOffice = Method.getString(jsonObject, "taxOffice");
            this.bankAccounts = new BankAccount.List(Method.getJsonArray(jsonObject, "bankAccounts"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
