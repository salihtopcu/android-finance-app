package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.DeclineQuoteRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DeclineQuoteDao extends FMDao {

    public DeclineQuoteDao(VolleyDaoListener listener, DeclineQuoteRequest request) {
        super(listener, Constants.ApiMethod.DeclineQuote, Request.Method.POST, true);
        JSONObject data = new JSONObject();
        try {
            data.put("id", request.quoteId);
            data.put("declineReason", request.note);
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
