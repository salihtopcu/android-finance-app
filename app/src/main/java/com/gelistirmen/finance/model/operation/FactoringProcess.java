package com.gelistirmen.finance.model.operation;

import android.util.Log;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class FactoringProcess {
    public String demandId;
    public String quoteId;
    public Date createdDate;
    public double amount;
    public int status;
    public int currency;

    public FactoringProcess(JSONObject jsonObject) {
        try {
            this.demandId = Method.getString(jsonObject, "demandId");
            this.quoteId = Method.getString(jsonObject, "quoteId");
            this.createdDate = Method.getDate(jsonObject, "createdDate", Constants.webServiceDateTimeFormat);
            this.amount = Method.getDouble(jsonObject, "amount");
            this.status = Method.getInt(jsonObject, "status");
            this.currency = Method.getInt(jsonObject, "currency");
        } catch (JSONException e) {
            Log.e("FactoringProcess", "Json parse error.");
        }
    }

    public static class List extends ArrayList<FactoringProcess> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new FactoringProcess(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
