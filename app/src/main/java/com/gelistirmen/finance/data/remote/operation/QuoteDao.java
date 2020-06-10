package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Quote;

import org.json.JSONObject;

public class QuoteDao extends FMDao {

    public QuoteDao(VolleyDaoListener listener, String quoteId) {
        super(listener, Constants.ApiMethod.Quotes + "/" + quoteId);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(data instanceof JSONObject ? new Quote((JSONObject) data) : null);
    }
}
