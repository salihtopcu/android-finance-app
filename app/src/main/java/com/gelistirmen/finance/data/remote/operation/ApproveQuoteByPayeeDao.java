package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;

public class ApproveQuoteByPayeeDao extends FMDao {

    public ApproveQuoteByPayeeDao(VolleyDaoListener listener, String quoteId) {
        super(listener, Constants.ApiMethod.ApproveQuoteByPayee + "/" + quoteId, Request.Method.POST, true);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }
}
