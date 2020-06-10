package com.gelistirmen.finance.model.membership;

import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BankAccount {
    public String id;
    public String bankName;
    public String branchCode;
    public String accountNo;
    public String iban;

    public BankAccount(String bankName, String branchCode, String accountNo, String iban) {
        this.bankName = bankName;
        this.branchCode = branchCode;
        this.accountNo = accountNo;
        this.iban = iban;
    }

    public BankAccount(JSONObject jsonObject) {
        try {
            this.id = Method.getString(jsonObject, "id");
            this.bankName = Method.getString(jsonObject, "bankName");
            this.branchCode = Method.getString(jsonObject, "branchCode");
            this.accountNo = Method.getString(jsonObject, "accountNo");
            this.iban = Method.getString(jsonObject, "iban");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class List extends ArrayList<BankAccount> {
        public List(JSONArray jsonArray) {
            super();
            try {
                for (int i = 0; i < jsonArray.length(); i++)
                    this.add(new BankAccount(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
