package com.gelistirmen.finance.data.remote.membership;

import android.util.Log;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.BankAccount;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateBankAccountDao extends FMDao {

    public UpdateBankAccountDao(VolleyDaoListener listener, BankAccount bankAccount) {
        super(listener, Constants.ApiMethod.BankAccounts + "/" + bankAccount.id, Request.Method.PUT, true);
        JSONObject data = new JSONObject();
        try {
            data.put("bankName", bankAccount.bankName);
            data.put("branchCode", bankAccount.branchCode);
            data.put("accountNo", bankAccount.accountNo);
            data.put("iban", bankAccount.iban);
        } catch (JSONException e) {
            Log.e("JSON Create", e.toString());
        }
        super.bodyDataObject = data;
        super.acceptEmptyResponse = true;
    }
}
