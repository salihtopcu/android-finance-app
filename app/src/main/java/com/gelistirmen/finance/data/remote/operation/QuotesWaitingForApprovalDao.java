package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Quote;

import org.json.JSONArray;

public class QuotesWaitingForApprovalDao extends FMDao {

    public QuotesWaitingForApprovalDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.QuotesWaitingForApproval);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        Quote.List quotes = null;
        if (data instanceof JSONArray)
            quotes = new Quote.List((JSONArray) data);
        super.onAfterSuccessRequest(quotes);
    }
}
