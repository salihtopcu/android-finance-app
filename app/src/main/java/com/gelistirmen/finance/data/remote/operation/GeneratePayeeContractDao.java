package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneratePayeeContractDao extends FMDao {

    public GeneratePayeeContractDao(VolleyDaoListener listener, Quote quote, String bankAccountId) {
        super(listener, Constants.ApiMethod.GeneratePayeeContract, Request.Method.POST, true);
        JSONObject data = new JSONObject();
        try {
            data.put("quoteId", quote.id);
            JSONObject bankAccountData = new JSONObject();
            bankAccountData.put("id", bankAccountId);
            data.put("bankAccount", bankAccountData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.bodyDataObject = data;
    }

    public GeneratePayeeContractDao(VolleyDaoListener listener, Quote quote, BankAccount bankAccount) {
        super(listener, Constants.ApiMethod.GeneratePayeeContract, Request.Method.POST, true);
        JSONObject data = new JSONObject();
        try {
            data.put("quoteId", quote.id);
            JSONObject bankAccountData = new JSONObject();
            if (Method.isNullOrEmpty(bankAccount.id)) {
                bankAccountData.put("bankName", bankAccount.bankName);
                bankAccountData.put("branchCode", bankAccount.branchCode);
                bankAccountData.put("accountNo", bankAccount.accountNo.trim().isEmpty() ? null : bankAccount.accountNo);
                bankAccountData.put("iban", bankAccount.iban.isEmpty() ? null : bankAccount.iban);
            } else {
                bankAccountData.put("id", bankAccount.id);
            }
            data.put("bankAccount", bankAccountData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.bodyDataObject = data;
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }
}
