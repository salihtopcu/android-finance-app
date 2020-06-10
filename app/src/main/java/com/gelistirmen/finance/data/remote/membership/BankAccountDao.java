package com.gelistirmen.finance.data.remote.membership;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.BankAccount;

import org.json.JSONObject;

public class BankAccountDao extends FMDao {

    public BankAccountDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.BankAccounts + "/" + id, Request.Method.GET, true);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new BankAccount((JSONObject) data));
    }
}
