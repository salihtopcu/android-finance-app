package com.gelistirmen.finance.model.operation;

import android.text.TextUtils;
import android.util.Log;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Quote {
    public String id;
    public int status;
    public Date createdDate;
    public Date updatedDate;
    public String payeeCompanyName;
    public double invoiceAmount;

    public Date maturityDate;
    public double interestAmount;
    public int interestRate;
    public Invoice.List invoices;

    public String debtorFullName;
    public String debtorTaxNo;
    public String companyRepresentativeFullName;
    public int maturity;
    public double totalAmount;
    public double paymentAmount;
    public int currency;

    public Quote(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.status = Method.getInt(jsonObject, "status");
            this.createdDate = Method.getDate(jsonObject, "createdDate", Constants.webServiceDateTimeFormat);
            this.updatedDate = Method.getDate(jsonObject, "updatedDate", Constants.webServiceDateTimeFormat);
            this.payeeCompanyName = Method.getString(jsonObject, "payeeCompanyName");
            if (TextUtils.isEmpty(this.payeeCompanyName))
                this.payeeCompanyName = Method.getString(jsonObject, "payeeCompany");
            this.invoiceAmount = Method.getDouble(jsonObject, "invoiceAmount");

            this.maturityDate = Method.getDate(jsonObject, "maturityDate", Constants.webServiceDateTimeFormat);
            this.interestAmount = Method.getDouble(jsonObject, "interestAmount");
            this.interestRate = Method.getInt(jsonObject, "interestRate");
            JSONArray invoicesJsonArray = Method.getJsonArray(jsonObject, "invoices");
            if (invoicesJsonArray != null)
                this.invoices = new Invoice.List(invoicesJsonArray);

            this.debtorFullName = Method.getString(jsonObject, "debtorFullName");
            this.debtorTaxNo = Method.getString(jsonObject, "debtorTaxNo");
            this.companyRepresentativeFullName = Method.getString(jsonObject, "companyRepresentativeFullName");
            this.maturity = Method.getInt(jsonObject, "maturity");
            this.totalAmount = Method.getDouble(jsonObject, "totalAmount");
            this.paymentAmount = Method.getDouble(jsonObject, "paymentAmount");
            this.currency = Method.getInt(jsonObject, "currency");


        } catch (JSONException e) {
            Log.e("Quote", "Json parse error.");
        }
    }

    public static class List extends ArrayList<Quote> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new Quote(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
