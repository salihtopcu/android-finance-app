package com.gelistirmen.finance.data.remote.membership;

import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.Company;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyUpdateDao extends FMDao {
    public Company company;

    public CompanyUpdateDao(VolleyDaoListener listener, Company company) {
        super(listener, Constants.ApiMethod.Companies, Request.Method.PUT, true);
        super.acceptEmptyResponse = true;
        this.company = company;
        try {
            JSONObject companyUpdateJsonObject = new JSONObject();
            companyUpdateJsonObject.put("taxNo", company.taxNo);
            companyUpdateJsonObject.put("taxOffice", company.taxOffice);
            companyUpdateJsonObject.put("companyName", company.name);
            companyUpdateJsonObject.put("address", company.address);
            companyUpdateJsonObject.put("postalCode", company.postalCode);
            companyUpdateJsonObject.put("countyId", company.county.id);
            companyUpdateJsonObject.put("phone", company.phoneNumber);
            companyUpdateJsonObject.put("email", company.email);
            companyUpdateJsonObject.put("webSite", company.webSite);
            super.bodyDataObject = companyUpdateJsonObject;
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
    }
}
