package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;

public class DeleteBankAccountDao extends FMDao {

    public DeleteBankAccountDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.BankAccounts + "/" + id, Request.Method.DELETE, true);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return null;
    }
}
