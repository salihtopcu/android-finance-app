package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.QuoteStatusResponse;

import org.json.JSONObject;

public class QuoteStatusDao extends FMDao {

    public QuoteStatusDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.QuoteStatus.replace("{id}", id));
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new QuoteStatusResponse((JSONObject) data));
    }
}