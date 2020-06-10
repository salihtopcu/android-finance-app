package com.gelistirmen.finance.model.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Invoice {
    public String id;
    public Date createdDate;
    public Date updatedDate;
    public double amount;
    public int currency;
    public boolean isWholeCost;
    public double paymentCost;
    public int interestResponsible;
    public String serialNo;
    public String no;
    public Date date;
    public String eInvoiceHash;
    public int type;
    public Date estimatedMaturityDate;
    public String payeeTaxNo;
    public String debtorTaxNo;
    public byte[] pictureData;

    public Invoice(String debtorTaxNo, Date createDate, Date date, String no) {
        this.debtorTaxNo = debtorTaxNo;
        this.createdDate = createDate;
        this.date = date;
        this.no = no;
    }

    public Invoice(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.createdDate = Method.getDate(jsonObject, "createdDate", Constants.webServiceDateTimeFormat);
            this.updatedDate = Method.getDate(jsonObject, "updatedDate", Constants.webServiceDateTimeFormat);
            if (Method.exists(jsonObject, "invoice"))
                jsonObject = Method.getJsonObject(jsonObject, "invoice");
            this.amount = Method.getDouble(jsonObject, "invoiceAmount"); // fixme:
            this.currency = Method.getInt(jsonObject, "currency");
            this.isWholeCost = Method.getBoolean(jsonObject, "isWholeCost");
            this.paymentCost = Method.getDouble(jsonObject, "paymentCost");
            this.interestResponsible = Method.getInt(jsonObject, "interestResponsible");
            this.serialNo = Method.getString(jsonObject, "serialNo");
            this.no = Method.getString(jsonObject, "invoiceNo");
            this.date = Method.getDate(jsonObject, "invoiceDate", Constants.webServiceDateTimeFormat);
            this.eInvoiceHash = Method.getString(jsonObject, "einvoiceHash");
            this.type = Method.getInt(jsonObject, "invoiceType");
            this.estimatedMaturityDate = Method.getDate(jsonObject, "estimatedMaturityDate", Constants.webServiceDateTimeFormat);
            this.payeeTaxNo = Method.getString(jsonObject, "payeeTaxNo");
            this.debtorTaxNo = Method.getString(jsonObject, "debtorTaxNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class List extends ArrayList<Invoice> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    this.add(new Invoice(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public List() {
            super();
        }
    }
}
