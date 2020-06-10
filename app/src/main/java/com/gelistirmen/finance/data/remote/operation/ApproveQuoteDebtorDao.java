package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Fingerprint;

import org.json.JSONObject;

public class ApproveQuoteDebtorDao extends FMDao {

    public ApproveQuoteDebtorDao(VolleyDaoListener listener, String quoteId) {
        super(listener, Constants.ApiMethod.ApproveQuoteDebtor + "/" + quoteId, Request.Method.POST, true);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(data instanceof JSONObject ? new Fingerprint((JSONObject) data) : null);
    }
}

